import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "../../axios-client";
import { RootState } from "../../app/store"; // adjust import path as needed
import { Category } from "../categories/categorySlice";
import { Tag } from "../tags/tagSlice";
import { Repository } from "../repositories/repositorySlice";
import { Build } from "../builds/buildSlice";

/**
 * Matches the structure used by GameRequest on the backend.
 * The backend returns this same shape on create/update/fetch.
 */
export interface Game {
  id?: number;
  title: string;
  description: string;
  version: string;
  repository: Repository;
  category: Category;
  tags: Tag[];
  builds?: Build[];
  lastBuild?: Build;
  owner?: { username: string };
  createdAt?: string;
  updatedAt?: string;
}

export interface Error {
  message: string;
  status: number;
  timestamp: string;
}

// Define the slice state
interface GameState {
  games: Game[];
  selectedGame: Game | null;
  loading: boolean;
  error: Error | null;
}

// Initial state
const initialState: GameState = {
  games: [],
  selectedGame: null,
  loading: false,
  error: null,
};

/**
 * Thunks (CRUD)
 */
export const fetchGames = createAsyncThunk("games/fetchUserGames", async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get<Game[]>("/users/games");
    return response.data; // a list of games
  } catch (error: any) {
    return rejectWithValue(error.response?.data?.message || "Failed to fetch games.");
  }
});

export const fetchAllGames = createAsyncThunk("games/fetchAllGames", async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get<Game[]>("/games");
    return response.data; // a list of games
  } catch (error: any) {
    return rejectWithValue(error.response?.data?.message || "Failed to fetch games.");
  }
});

export const createGame = createAsyncThunk("games/createGame", async (newGame: Game, { rejectWithValue }) => {
  try {
    const response = await axios.post<Game>("/games", newGame);
    return response.data; // the newly created game
  } catch (error: any) {
    return rejectWithValue(error.response?.data || "Failed to create game.");
  }
});

export const updateGame = createAsyncThunk(
  "games/updateGame",
  async ({ id, ...updateData }: Game, { rejectWithValue }) => {
    if (!id) {
      return rejectWithValue("Game ID is required for update.");
    }
    try {
      const response = await axios.put<Game>(`/games/${id}`, updateData);
      return response.data; // the updated game
    } catch (error: any) {
      return rejectWithValue(error.response?.data || "Failed to update game.");
    }
  },
);

export const deleteGame = createAsyncThunk("games/deleteGame", async (gameId: number, { rejectWithValue }) => {
  try {
    await axios.delete(`/games/${gameId}`);
    return gameId; // so we can remove it from state
  } catch (error: any) {
    return rejectWithValue(error.response?.data || "Failed to delete game.");
  }
});

export const fetchGameById = createAsyncThunk("games/fetchGameById", async (gameId: number, { rejectWithValue }) => {
  try {
    const response = await axios.get<Game>(`/games/${gameId}`);
    return response.data;
  } catch (error: any) {
    return rejectWithValue(error.response?.data || "Failed to fetch game.");
  }
});

export const gameSlice = createSlice({
  name: "games",
  initialState,
  reducers: {
    setSelectedGame: (state, action: PayloadAction<Game | null>) => {
      state.selectedGame = action.payload;
    },
  },
  extraReducers: (builder) => {
    // Fetch User Games
    builder.addCase(fetchGames.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchGames.fulfilled, (state, action: PayloadAction<Game[]>) => {
      state.games = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchGames.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as Error;
    });

    // Fetch All
    builder.addCase(fetchAllGames.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchAllGames.fulfilled, (state, action: PayloadAction<Game[]>) => {
      state.games = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchAllGames.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as Error;
    });

    // Create
    builder.addCase(createGame.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(createGame.fulfilled, (state, action: PayloadAction<Game>) => {
      state.games.push(action.payload);
      state.loading = false;
    });
    builder.addCase(createGame.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as Error;
    });

    // Update
    builder.addCase(updateGame.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(updateGame.fulfilled, (state, action: PayloadAction<Game>) => {
      state.selectedGame = action.payload;
      state.loading = false;
    });
    builder.addCase(updateGame.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as Error;
    });

    // Delete
    builder.addCase(deleteGame.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(deleteGame.fulfilled, (state, action: PayloadAction<number>) => {
      state.games = state.games.filter((g) => g.id !== action.payload);
      state.loading = false;
    });
    builder.addCase(deleteGame.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as Error;
    });

    // (Optional) fetchGameById
    builder.addCase(fetchGameById.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(fetchGameById.fulfilled, (state, action: PayloadAction<Game>) => {
      state.selectedGame = action.payload;
      state.loading = false;
    });
    builder.addCase(fetchGameById.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as Error;
    });
  },
});

export const { setSelectedGame } = gameSlice.actions;

export const selectAllGames = (state: RootState) => state.games.games;
export const selectSelectedGame = (state: RootState) => state.games.selectedGame;
export const selectGamesLoading = (state: RootState) => state.games.loading;
export const selectGamesError = (state: RootState) => state.games.error;

export default gameSlice.reducer;
