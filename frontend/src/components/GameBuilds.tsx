import { useEffect, useMemo, useRef } from 'react';
import { MaterialReactTable, MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';
import { Chip, createTheme, ThemeProvider } from '@mui/material';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { fetchBuilds, selectAllBuilds, selectBuildsLoading, selectBuildsError, Build } from '../features/builds/buildSlice';

import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface BuildLogMessage {
    buildId: number;
    gameId: number;
    line: string;
}

type Props = {
    id?: number
}

export default function GameBuilds({ id }: Props) {
    const dispatch = useAppDispatch();
    const builds = useAppSelector(selectAllBuilds);
    const loading = useAppSelector(selectBuildsLoading);
    const error = useAppSelector(selectBuildsError);

    const stompClientRef = useRef<Client | null>(null);
    const subscriptionsRef = useRef<Map<string, any>>(new Map());


    useEffect(() => {
        if (id) {
            dispatch(fetchBuilds(id));
        }
    }, [dispatch, id]);

    useEffect(() => {
        if (id === undefined || id === null) {
            console.log("WebSocket: No game ID provided, skipping connection.");
            return;
        }

        const backendWsUrl = 'http://localhost:8085/ws';
        const gameId = id;
        const statusTopic = `/topic/builds/${gameId}/status`;

         const client = new Client({
             webSocketFactory: () => new SockJS(backendWsUrl),
             debug: function (str) {
                 console.log('STOMP Debug:', str);
             },
             reconnectDelay: 5000, // Try to reconnect every 5 seconds if connection is lost
             heartbeatIncoming: 4000,
             heartbeatOutgoing: 4000,
         });

        // --- Connection Logic ---
        client.onConnect = (frame) => {
            console.log('STOMP: Connected successfully!', frame);

            // *** Subscribe to Game Status Updates ***
            console.log(`STOMP: Subscribing to ${statusTopic}`);
            const statusSubscription = client.subscribe(statusTopic, (message: IMessage) => {
                try {
                    const statusUpdate: Build = JSON.parse(message.body);
                    console.log(`%c[WebSocket Status Update - Game ${gameId}]:`, 'color: blue; font-weight: bold;', statusUpdate);

                    // *** Dynamic Log Subscription (Example) ***
                    // If we get a RUNNING status, we might want to subscribe to its logs
                    if (statusUpdate.status === 'RUNNING' && statusUpdate.buildId) {
                        const buildId = statusUpdate.buildId;
                        const logTopic = `/topic/builds/${buildId}/logs`;

                        // Avoid duplicate subscriptions
                        if (!subscriptionsRef.current.has(logTopic)) {
                             console.log(`%cSTOMP: Dynamically subscribing to logs for build ${buildId} at ${logTopic}`, 'color: green;');
                             const logSubscription = client.subscribe(logTopic, (logMessage: IMessage) => {
                                 try {
                                     const logData: BuildLogMessage = JSON.parse(logMessage.body);
                                     // Only log if it matches the build we subscribed for (sanity check)
                                     if (logData.buildId === buildId) {
                                         console.log(`%c[WebSocket Log - Build ${buildId}]: ${logData.line}`, 'color: gray;');
                                     }
                                 } catch (e) {
                                     console.error("STOMP: Failed to parse log message:", e, logMessage.body);
                                 }
                             });
                             subscriptionsRef.current.set(logTopic, logSubscription); // Store subscription
                        }
                    }
                     // Optional: Unsubscribe from logs when build finishes?
                     if ((statusUpdate.status === 'SUCCESS' || statusUpdate.status === 'FAIL') && statusUpdate.buildId) {
                         const logTopic = `/topic/builds/${statusUpdate.buildId}/logs`;
                         if (subscriptionsRef.current.has(logTopic)) {
                             console.log(`%cSTOMP: Unsubscribing from logs for finished build ${statusUpdate.buildId}`, 'color: orange;');
                             subscriptionsRef.current.get(logTopic)?.unsubscribe();
                             subscriptionsRef.current.delete(logTopic);
                         }
                     }

                } catch (e) {
                    console.error("STOMP: Failed to parse status message:", e, message.body);
                }
            });
             subscriptionsRef.current.set(statusTopic, statusSubscription); // Store subscription

        };

        // --- Error Handling ---
        client.onStompError = (frame) => {
            console.error('STOMP: Broker reported error:', frame.headers['message']);
            console.error('STOMP: Additional details:', frame.body);
        };

         client.onWebSocketError = (event) => {
             console.error('WebSocket Error:', event);
         };

         client.onWebSocketClose = (event) => {
             console.warn('WebSocket Closed:', event);
             // Clean up subscriptions map on close
             subscriptionsRef.current.clear();
         };


        // --- Activation & Cleanup ---
        console.log("STOMP: Activating client...");
        client.activate(); // Start the connection process
        stompClientRef.current = client; // Store the client instance

        // Return the cleanup function to be run on component unmount or when 'id' changes
        return () => {
            console.log(`WebSocket: Cleaning up connection for game ID: ${gameId}`);
            if (stompClientRef.current?.active) {
                console.log("STOMP: Deactivating client...");
                try {
                    // Unsubscribe from all stored subscriptions explicitly
                     subscriptionsRef.current.forEach((sub, topic) => {
                         console.log(`STOMP: Cleaning up subscription to ${topic}`);
                         sub?.unsubscribe();
                     });
                     subscriptionsRef.current.clear();

                    stompClientRef.current.deactivate(); // Gracefully disconnect
                    console.log("STOMP: Client deactivated.");
                } catch (e) {
                    console.error("STOMP: Error during deactivation", e);
                }
            } else {
                console.log("STOMP: Client was not active, no deactivation needed.");
            }
             stompClientRef.current = null; // Clear the ref
        };

    }, [id]); 

    const darkTheme = createTheme({
        palette: {
            mode: 'dark',
        },
    });

    const columns = useMemo<MRT_ColumnDef<Build>[]>(() => [
        {
            accessorKey: 'id',
            header: 'ID',
            size: 50,
        },
        {
            accessorKey: 'buildStatus',
            header: 'Status',
            size: 100,
            Cell: ({ cell }) => {
                const status = cell.getValue<string>();
                let chipColor: "default" | "success" | "error" | "warning" | "info" = "default";
                let label = status;

                if (status === "SUCCESS") {
                    chipColor = "success";
                    label = "Success";
                } else if (status === "FAIL") {
                    chipColor = "error";
                    label = "Fail";
                } else if (status === "RUNNING") {
                    chipColor = "warning";
                    label = "Running";
                } else if (status === "PENDING") {
                    chipColor = "info";
                    label = "Pending";
                }

                return <Chip className='w-[40%]' label={label} color={chipColor} />;
            },
        },
        {
            accessorKey: 'createdAt',
            header: 'Created',
            Cell: ({ cell }) => {
                const dateVal = cell.getValue<string | null>();
                return dateVal ? new Date(dateVal).toLocaleString() : '';
            },
        },
        {
            accessorKey: 'updatedAt',
            header: 'Updated',
            Cell: ({ cell }) => {
                const dateVal = cell.getValue<string | null>();
                return dateVal ? new Date(dateVal).toLocaleString() : '';
            },
        },
        // Note: We do not add a column for logs because it will be rendered in the expandable detail panel.
    ], []);

    const table = useMaterialReactTable({
        columns,
        data: builds,
        state: {
            isLoading: loading,
        },
        mrtTheme: {
            baseBackgroundColor: '#0a0b0b',
        },
        muiTableContainerProps: {
            sx: {
                maxHeight: 400,
                minHeight: '50vh',
                overflowY: 'auto',
            },
        },
        enableStickyHeader: true,
        enableStickyFooter: true,
        // Define the detail panel to show logs.
        renderDetailPanel: ({ row }) => (
            <div>
                <strong>Logs:</strong>
                <pre style={{
                    maxHeight: '310px',
                    overflowY: 'auto',
                    whiteSpace: 'pre-wrap',
                    wordBreak: 'break-word',
                    marginTop: '8px',
                    padding: '8px',
                    background: '#111',
                    border: '1px solid #333',
                    borderRadius: '4px'
                }}>
                    {row.original.logs || 'No logs available.'}
                </pre>
            </div >
        ),
        // Use muiExpandButtonProps to handle row expansion.
        muiExpandButtonProps: ({ row, table }) => ({
            onClick: () => table.setExpanded({ [row.id]: !row.getIsExpanded() }),
        }),
    });

    return (
        <ThemeProvider theme={darkTheme}>
            <div className="px-4 py-4">
                <h2 className="text-3xl font-semibold">Game Builds</h2>
            </div>

            {error && <p style={{ color: '#f44' }}>{error}</p>}

            <div className="border border-primary rounded-md">
                <MaterialReactTable table={table} />
            </div>
        </ThemeProvider>
    );
}
