import { createSlice, createAsyncThunk, PayloadAction } from "@reduxjs/toolkit";
import axios from "../../axios-client";
import { RootState } from "../../app/store";

export interface Category {
  id: number;
  name: string;
  description: string;
  createdAt?: string;
  updatedAt?: string;
}

interface CreateCategoryPayload {
  name: string;
  description: string;
}

interface UpdateCategoryPayload {
  id: number;
  name?: string;
  description?: string;
}

interface CategoryState {
  categories: Category[];
  selectedCategory: Category | null;
  loading: boolean;
  error: string | null;
}

const initialState: CategoryState = {
  categories: [],
  selectedCategory: null,
  loading: false,
  error: null,
};

/**
 * Thunks (CRUD)
 */
export const fetchCategories = createAsyncThunk("categories/fetchCategories", async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get("/categories");
    return response.data as Category[];
  } catch (error: any) {
    return rejectWithValue(error.response?.data?.message || "Failed to fetch categories");
  }
});

export const createCategory = createAsyncThunk(
  "categories/createCategory",
  async (newCategory: CreateCategoryPayload, { rejectWithValue }) => {
    try {
      const response = await axios.post("/categories", newCategory);
      return response.data as Category;
    } catch (error: any) {
      return rejectWithValue(error.response?.data || "Failed to create category");
    }
  },
);

export const updateCategory = createAsyncThunk(
  "categories/updateCategory",
  async (updatedCategory: UpdateCategoryPayload, { rejectWithValue }) => {
    const { id, ...body } = updatedCategory;
    try {
      const response = await axios.put(`/categories/${id}`, body);
      return response.data as Category;
    } catch (error: any) {
      return rejectWithValue(error.response?.data || "Failed to update category");
    }
  },
);

export const deleteCategory = createAsyncThunk(
  "categories/deleteCategory",
  async (categoryId: number, { rejectWithValue }) => {
    try {
      await axios.delete(`/categories/${categoryId}`);
      return categoryId;
    } catch (error: any) {
      return rejectWithValue(error.response?.data || "Failed to delete category");
    }
  },
);

export const categorySlice = createSlice({
  name: "categories",
  initialState,
  reducers: {
    setCategoryToEdit: (state, action: PayloadAction<Category | null>) => {
      state.selectedCategory = action.payload;
    },
  },
  extraReducers: (builder) => {
    // fetchCategories
    builder.addCase(fetchCategories.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchCategories.fulfilled, (state, action) => {
      state.categories = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchCategories.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });

    // createCategory
    builder.addCase(createCategory.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(createCategory.fulfilled, (state, action) => {
      state.categories.push(action.payload);
      state.loading = false;
    });
    builder.addCase(createCategory.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });

    // updateCategory
    builder.addCase(updateCategory.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(updateCategory.fulfilled, (state, action) => {
      const updatedCat = action.payload;
      const index = state.categories.findIndex((cat) => cat.id === updatedCat.id);
      if (index !== -1) {
        state.categories[index] = updatedCat;
      }
      state.loading = false;
    });
    builder.addCase(updateCategory.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });

    // deleteCategory
    builder.addCase(deleteCategory.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(deleteCategory.fulfilled, (state, action) => {
      const deletedId = action.payload;
      state.categories = state.categories.filter((cat) => cat.id !== deletedId);
      state.loading = false;
    });
    builder.addCase(deleteCategory.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });
  },
});

export const { setCategoryToEdit } = categorySlice.actions;

export const selectCategories = (state: RootState) => state.categories.categories;
export const selectCategoriesLoading = (state: RootState) => state.categories.loading;
export const selectCategoriesError = (state: RootState) => state.categories.error;
export const selectCategoryToEdit = (state: RootState) => state.categories.selectedCategory;

export default categorySlice.reducer;
