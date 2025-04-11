import { useEffect, useMemo, useRef } from 'react';
import { MaterialReactTable, MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';
import { Chip, createTheme, ThemeProvider } from '@mui/material';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { fetchBuilds, selectAllBuilds, selectBuildsLoading, selectBuildsError, Build, upsertBuild, updateBuildLog } from '../features/builds/buildSlice';

import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { selectCurrentToken } from '../features/auth/authSlice';

interface BuildStatusUpdateMessage {
    build: Build;
    buildId: number;
    gameId: number;
    status: string;
}

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
    const jwtToken = useAppSelector(selectCurrentToken);

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

        // --- Configuration ---
        const backendWsUrl = 'http://localhost:8085/ws';
        const gameId = id;
        const statusTopic = `/topic/builds/${gameId}/status`;
        // Note: Log topic is build-specific: `/topic/builds/{buildId}/logs`
        // We'll subscribe to status first, and potentially logs if we get a buildId

        console.log(`WebSocket: Attempting to connect for game ID: ${gameId}`);


        const client = new Client({
            webSocketFactory: () => new SockJS(backendWsUrl),
            debug: function (str) {
                console.log('STOMP Debug:', str);
            },
            connectHeaders: {
                'Authorization': `Bearer ${jwtToken}`
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });


        client.onConnect = (frame) => {
            console.log('STOMP: Connected successfully!', frame);

            console.log(`STOMP: Subscribing to ${statusTopic}`);
            const statusSubscription = client.subscribe(statusTopic, (message: IMessage) => {
                try {
                    const buildUpdate: BuildStatusUpdateMessage = JSON.parse(message.body);
                    console.log(`%c[WebSocket Status Update - Game ${gameId}]:`, 'color: blue; font-weight: bold;', buildUpdate);

                    // *** Dynamic Log Subscription (Example) ***
                    // If we get a RUNNING status, we might want to subscribe to its logs
                    if (buildUpdate.status === 'RUNNING' && buildUpdate.buildId) {
                        const build = buildUpdate.build;
                        const buildId = buildUpdate.buildId;
                        const logTopic = `/topic/builds/${buildId}/logs`;

                        if (!builds.includes(build)) {
                            dispatch(upsertBuild(build));
                        }

                        if (buildUpdate.status === 'RUNNING' && buildUpdate.buildId) {
                            const buildId = buildUpdate.buildId;
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
                                            dispatch(updateBuildLog({ buildId: logData.buildId, line: logData.line }));
                                        }
                                    } catch (e) {
                                        console.error("STOMP: Failed to parse log message:", e, logMessage.body);
                                    }
                                });
                                subscriptionsRef.current.set(logTopic, logSubscription);
                            }
                        }
                    }

                    // Optional: Unsubscribe from logs when build finishes
                    if ((buildUpdate.status === 'SUCCESS' || buildUpdate.status === 'FAIL') && buildUpdate.buildId) {
                        const logTopic = `/topic/builds/${buildUpdate.buildId}/logs`;
                        if (subscriptionsRef.current.has(logTopic)) {
                            console.log(`%cSTOMP: Unsubscribing from logs for finished build ${buildUpdate.buildId}`, 'color: orange;');
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
            subscriptionsRef.current.clear();
        };


        // --- Activation & Cleanup ---
        console.log("STOMP: Activating client...");
        client.activate();
        stompClientRef.current = client;

        return () => {
            console.log(`WebSocket: Cleaning up connection for game ID: ${gameId}`);
            if (stompClientRef.current?.active) {
                console.log("STOMP: Deactivating client...");
                try {
                    subscriptionsRef.current.forEach((sub, topic) => {
                        sub?.unsubscribe();
                    });
                    subscriptionsRef.current.clear();

                    stompClientRef.current.deactivate();
                    console.log("STOMP: Client deactivated.");
                } catch (e) {
                    console.error("STOMP: Error during deactivation", e);
                }
            } else {
                console.log("STOMP: Client was not active, no deactivation needed.");
            }
            stompClientRef.current = null;
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

                return <Chip className={`w-[40%] ${status === 'RUNNING' ? 'animate-pulse' : ''}`} label={label} color={chipColor} />;
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
