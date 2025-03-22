import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "../../axios-client";
import { RootState } from "../../app/store"; // adjust import path as needed

/**
 * Matches the structure used by RepositoryRequest on the backend.
 * The backend returns this same shape on create/update/fetch.
 */
export interface Repository {
  ghId: number;
  name: string;
  fullName: string;
  htmlUrl: string;
  privateRepository: boolean;
  language?: string;
}

// Define the slice state
interface RepositoryState {
  repositories: Repository[];
  loading: boolean;
  error: string | null;
}

// Initial state
const initialState: RepositoryState = {
  repositories: [],
  loading: false,
  error: null,
};

/**
 * Thunks
 */
export const fetchRepositories = createAsyncThunk("repositories/fetchRepositories", async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get<Repository[]>("/users/repositories");
    console.log(response.data);
    return response.data; // a list of repositories
  } catch (error: any) {
    return rejectWithValue(error.response?.data?.message || "Failed to fetch repositories.");
  }
});

export const repositorySlice = createSlice({
  name: "fetchRepositories",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    // Fetch All
    builder.addCase(fetchRepositories.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchRepositories.fulfilled, (state, action: PayloadAction<Repository[]>) => {
      state.repositories = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchRepositories.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });
  },
});

export const selectAllRepositoriess = (state: RootState) => state.repositories.repositories;
export const selectRepositorysLoading = (state: RootState) => state.repositories.loading;
export const selectRepositorysError = (state: RootState) => state.repositories.error;

export default repositorySlice.reducer;
