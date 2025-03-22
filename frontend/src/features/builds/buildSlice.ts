import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "../../axios-client";
import { RootState } from "../../app/store"; // adjust import path as needed

/**
 * Matches the structure used by BuildRequest on the backend.
 * The backend returns this same shape on create/update/fetch.
 */
export interface Build {
  id: number;
  buildStatus: "PENDING" | "RUNNING" | "SUCCESS" | "FAIL";
  logs?: string;
  path?: string;
  createdAt?: string;
  updatedAt?: string;
}

// Define the slice state
interface BuildState {
  builds: Build[];
  lastBuild: Build | null;
  loading: boolean;
  error: string | null;
}

// Initial state
const initialState: BuildState = {
  builds: [],
  lastBuild: null,
  loading: false,
  error: null,
};

/**
 * Thunks
 */
export const fetchBuilds = createAsyncThunk("builds/fetchBuilds", async (gameId: number, { rejectWithValue }) => {
  try {
    const response = await axios.get<Build[]>(`/builds/game/${gameId}`);
    console.log(response.data);
    return response.data; // a list of builds
  } catch (error: any) {
    return rejectWithValue(error.response?.data?.message || "Failed to fetch builds.");
  }
});

export const fetchLatestBuild = createAsyncThunk(
  "builds/fetchLastBuild",
  async (gameId: number, { rejectWithValue }) => {
    try {
      const response = await axios.get<Build>(`/builds/game/${gameId}/latest-success`);
      console.log(response.data);
      return response.data; // a list of builds
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || "Failed to fetch builds.");
    }
  },
);

export const buildSlice = createSlice({
  name: "fetchBuilds",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    // Fetch All
    builder.addCase(fetchBuilds.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchBuilds.fulfilled, (state, action: PayloadAction<Build[]>) => {
      state.builds = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchBuilds.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });

    builder.addCase(fetchLatestBuild.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchLatestBuild.fulfilled, (state, action: PayloadAction<Build>) => {
      state.lastBuild = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchLatestBuild.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });
  },
});

export const selectAllBuilds = (state: RootState) => state.builds.builds;
export const selectLastBuild = (state: RootState) => state.builds.lastBuild;
export const selectBuildsLoading = (state: RootState) => state.builds.loading;
export const selectBuildsError = (state: RootState) => state.builds.error;

export default buildSlice.reducer;
