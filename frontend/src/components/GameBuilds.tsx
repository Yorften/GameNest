import { useEffect, useMemo } from 'react';
import { MaterialReactTable, MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';
import { Chip, createTheme, ThemeProvider } from '@mui/material';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { fetchBuilds, selectAllBuilds, selectBuildsLoading, selectBuildsError, Build } from '../features/builds/buildSlice';

type Props = {
    id?: number
}

export default function GameBuilds({ id }: Props) {
    const dispatch = useAppDispatch();
    const builds = useAppSelector(selectAllBuilds);
    const loading = useAppSelector(selectBuildsLoading);
    const error = useAppSelector(selectBuildsError);

    useEffect(() => {
        if (id) {
            dispatch(fetchBuilds(id));
        }
    }, [dispatch, id]);

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
        enableStickyFooter:true,
        // Define the detail panel to show logs.
        renderDetailPanel: ({ row }) => (
            <div>
                <strong>Logs:</strong>
                <pre style={{
                    maxHeight: '310px',   // Set a max height specifically for the log area
                    overflowY: 'auto',    // Make this specific area vertically scrollable
                    whiteSpace: 'pre-wrap', // Wrap long lines instead of horizontal scrolling
                    wordBreak: 'break-word', // Ensure long unbroken words also wrap
                    marginTop: '8px',     // Add some space below the "Logs:" title
                    padding: '8px',       // Add some padding inside the log box
                    background: '#111',  // Give the log box a distinct background
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
