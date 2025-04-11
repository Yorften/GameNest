import { useEffect, useMemo, useState } from 'react';
import { MaterialReactTable, type MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';

import { ThemeProvider, createTheme } from '@mui/material/styles';

import { useAppDispatch, useAppSelector } from '../../app/hooks';
import {
  fetchTags,
  selectTags,
  deleteTag,
  selectTagsLoading,
  selectTagsError,
  Tag,
  setTagToEdit,
} from '../../features/tags/tagSlice';
import Button from '../../components/miscs/Button';
import { TagModal } from '../../components/TagModal';

export default function Tags() {
  const dispatch = useAppDispatch();
  const tags = useAppSelector(selectTags);
  const loading = useAppSelector(selectTagsLoading);
  const error = useAppSelector(selectTagsError);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const darkTheme = createTheme({
    palette: {
      mode: 'dark', // full dark mode
    },
  });

  const handleEdit = (tag: Tag) => {
    dispatch(setTagToEdit(tag))
    setIsModalOpen(true);
  };

  const handleDelete = (tagId: number) => {
    if (window.confirm('Are you sure you want to delete this Tag?')) {
      dispatch(deleteTag(tagId));
    }
  };

  useEffect(() => {
    dispatch(fetchTags());
  }, [dispatch]);

  const columns = useMemo<MRT_ColumnDef<Tag>[]>(
    () => [
      {
        accessorKey: 'id',
        header: 'ID',
        size: 50,
      },
      {
        accessorKey: 'name',
        header: 'Name',
        size: 200,
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
      {
        id: 'actions',
        header: 'Actions',
        enableColumnFilter: false,
        enableSorting: false,
        Cell: ({ row }) => {
          const tag = row.original;
          return (
            <div className="flex gap-2">
              <Button
                onClick={() => handleEdit(tag)}
                className="!text-sm font-medium !bg-gray-50/10 border border-primary hover:!brightness-125 hover:!bg-slate-500/10"
                title="Edit"
              ></Button>

              <Button
                onClick={() => handleDelete(tag.id)}
                className="!text-sm font-medium !bg-gray-50/10 border border-primary hover:!brightness-125 hover:!bg-slate-500/10"
                title="Delete"
              ></Button>
            </div>
          );
        },
      },
    ],
    [],
  );

  const table = useMaterialReactTable({
    columns,
    data: tags,
    state: {
      isLoading: loading,
      showProgressBars: loading,
    },
    initialState: {
      pagination: {
        pageIndex: 0,
        pageSize: 5,
      },
    },
    mrtTheme: {
      baseBackgroundColor: "#0a0b0b",
    },
    muiTableContainerProps: {
      sx: {
        maxHeight: 500, // choose your desired max height
        overflowY: 'auto',
      },
    },
    enableStickyHeader: true,
    // muiPaginationProps: {
    //   rowsPerPageOptions: [
    //     {
    //       label: "5",
    //       value: 5
    //     },
    //     {
    //       label: "6",
    //       value: 6,
    //     },
    //     {
    //       label: "10",
    //       value: 10
    //     },
    //   ],
    // }
  });

  return (
    <ThemeProvider theme={darkTheme}>
      <div className="flex items-center justify-between mb-4 px-4 pt-4">
        <h2 className="text-3xl font-semibold">Tags</h2>
        <Button onClick={() => setIsModalOpen(true)} className='!text-sm font-medium !bg-gray-50/10 border border-primary' title="New Tag"></Button>
      </div>

      <TagModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} />

      {error && <p style={{ color: '#f44' }}>{error}</p>}
      <div className='border border-primary rounded-md'>
        <MaterialReactTable table={table} />
      </div>

    </ThemeProvider>
  );
}
