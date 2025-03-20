import { createSlice, createAsyncThunk, PayloadAction } from "@reduxjs/toolkit";
import axios from "../../axios-client";
import { RootState } from "../../app/store";

export interface Tag {
  id: number;
  name: string;
  createdAt?: string;
  updatedAt?: string;
}

interface CreateTagPayload {
  name: string;
}

interface UpdateTagPayload {
  id: number;
  name?: string;
}

interface TagState {
  tags: Tag[];
  loading: boolean;
  error: string | null;
  selectedTag: Tag | null;
}

const initialState: TagState = {
  tags: [],
  loading: false,
  error: null,
  selectedTag: null,
};

/**
 * Thunks (CRUD)
 */
export const fetchTags = createAsyncThunk<Tag[]>("tags/fetchAll", async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get("/tags");
    return response.data; // should be an array of TagRequest objects
  } catch (err: any) {
    return rejectWithValue(err.response?.data?.message || "Failed to fetch tags");
  }
});

export const createTag = createAsyncThunk<Tag, CreateTagPayload>(
  "tags/create",
  async (payload, { rejectWithValue }) => {
    try {
      const response = await axios.post("/tags", payload);
      return response.data; // returns the newly-created tag
    } catch (err: any) {
      return rejectWithValue(err.response?.data || "Failed to create tag");
    }
  },
);

export const updateTag = createAsyncThunk<Tag, UpdateTagPayload>(
  "tags/update",
  async (payload, { rejectWithValue }) => {
    try {
      const { id, name } = payload;
      const response = await axios.put(`/tags/${id}`, { name });
      return response.data; // updated tag
    } catch (err: any) {
      return rejectWithValue(err.response?.data || "Failed to update tag");
    }
  },
);

export const deleteTag = createAsyncThunk<number, number>("tags/delete", async (tagId, { rejectWithValue }) => {
  try {
    await axios.delete(`/tags/${tagId}`);
    return tagId;
  } catch (err: any) {
    return rejectWithValue(err.response?.data?.message || "Failed to delete tag");
  }
});

export const tagSlice = createSlice({
  name: "tags",
  initialState,
  reducers: {
    setTagToEdit: (state, action: PayloadAction<Tag | null>) => {
      state.selectedTag = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // fetchTags
      .addCase(fetchTags.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchTags.fulfilled, (state, action: PayloadAction<Tag[]>) => {
        state.loading = false;
        state.tags = action.payload;
      })
      .addCase(fetchTags.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      // createTag
      .addCase(createTag.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createTag.fulfilled, (state, action: PayloadAction<Tag>) => {
        state.loading = false;
        state.tags.push(action.payload);
      })
      .addCase(createTag.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      // updateTag
      .addCase(updateTag.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateTag.fulfilled, (state, action: PayloadAction<Tag>) => {
        state.loading = false;
        const updated = action.payload;
        const idx = state.tags.findIndex((t) => t.id === updated.id);
        if (idx !== -1) {
          state.tags[idx] = updated;
        }
      })
      .addCase(updateTag.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      // deleteTag
      .addCase(deleteTag.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteTag.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        const deletedId = action.payload;
        state.tags = state.tags.filter((t) => t.id !== deletedId);
      })
      .addCase(deleteTag.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { setTagToEdit } = tagSlice.actions;

export const selectTags = (state: RootState) => state.tags.tags;
export const selectTagsLoading = (state: RootState) => state.tags.loading;
export const selectTagsError = (state: RootState) => state.tags.error;
export const selectTagToEdit = (state: RootState) => state.tags.selectedTag;

export default tagSlice.reducer;
