import { useEffect, useMemo, useState } from 'react';
import { MaterialReactTable, MRT_ColumnDef, useMaterialReactTable } from 'material-react-table';
import { ThemeProvider, createTheme } from '@mui/material/styles';

import { useAppDispatch, useAppSelector } from '../../app/hooks';
import {
  fetchCategories,
  selectCategories,
  deleteCategory,
  selectCategoriesLoading,
  selectCategoriesError,
  Category,
  setCategoryToEdit,
} from '../../features/categories/categorySlice';

import Button from '../../components/miscs/Button';
import { CategoryModal } from '../../components/CategoryModal';

export default function Categories() {
  const dispatch = useAppDispatch();
  const categories = useAppSelector(selectCategories);
  const loading = useAppSelector(selectCategoriesLoading);
  const error = useAppSelector(selectCategoriesError);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const darkTheme = createTheme({
    palette: {
      mode: 'dark',
    },
  });

  const handleEdit = (category: Category) => {
    dispatch(setCategoryToEdit(category));
    setIsModalOpen(true);
  };

  const handleDelete = (categoryId: number) => {
    if (window.confirm('Are you sure you want to delete this Category?')) {
      dispatch(deleteCategory(categoryId));
    }
  };

  useEffect(() => {
    dispatch(fetchCategories());
  }, [dispatch]);

  const columns = useMemo<MRT_ColumnDef<Category>[]>(
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
        accessorKey: 'description',
        header: 'Description',
        size: 250,
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
          const cat = row.original;
          return (
            <div className="flex gap-2">
              <Button
                onClick={() => handleEdit(cat)}
                className="!text-sm font-medium !bg-gray-50/10 border border-primary hover:!brightness-125 hover:!bg-slate-500/10"
                title="Edit"
              ></Button>
              <Button
                onClick={() => handleDelete(cat.id)}
                className="!text-sm font-medium !bg-gray-50/10 border border-primary hover:!brightness-125 hover:!bg-slate-500/10"
                title="Delete"
              ></Button>
            </div>
          );
        },
      },
    ],
    []
  );

  const table = useMaterialReactTable({
    columns,
    data: categories,
    state: {
      isLoading: loading,
      showProgressBars: loading,
    },
    mrtTheme: {
      baseBackgroundColor: '#0a0b0b',
    },
    muiTableContainerProps: {
      sx: {
        maxHeight: 500,
        overflowY: 'auto',
      },
    },
    enableStickyHeader: true,
  });

  return (
    <ThemeProvider theme={darkTheme}>
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-3xl font-semibold">Categories</h2>
        <Button
          onClick={() => {
            // ensure we're not in "edit" mode
            dispatch(setCategoryToEdit(null));
            setIsModalOpen(true);
          }}
          className="!text-sm font-medium !bg-gray-50/10 border border-primary"
          title="New Category"
        ></Button>
      </div>

      <CategoryModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} />

      {error && <p style={{ color: '#f44' }}>{error}</p>}

      <div className="border border-primary rounded-md">
        <MaterialReactTable table={table} />
      </div>
    </ThemeProvider>
  );
}
