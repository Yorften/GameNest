import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import axios from 'axios'
import type { RootState } from '../../app/store'

interface AuthState {
  user: any | null
  token: string | null
  status: 'idle' | 'loading' | 'succeeded' | 'failed'
  error: string | null
}

const storedToken = localStorage.getItem('token')
const storedUser = localStorage.getItem('user')

const initialState: AuthState = {
  user: storedUser ? JSON.parse(storedUser) : null,
  token: storedToken || null,
  status: 'idle',
  error: null,
}

// Async thunk for logging in
// The credentials argument is typically { username, password } or { email, password }.
export const login = createAsyncThunk(
  'auth/login',
  async (credentials: { username: string; password: string }, thunkAPI) => {
    try {
      const response = await axios.post('/api/auth/login', credentials)
      return response.data
    } catch (error: any) {
      return thunkAPI.rejectWithValue(
        error.response?.data?.message || 'Failed to login'
      )
    }
  }
)

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout(state) {
      state.user = null
      state.token = null
      state.error = null
      state.status = 'idle'
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
  extraReducers: builder => {
    builder
      // Pending state for login
      .addCase(login.pending, state => {
        state.status = 'loading'
        state.error = null
      })
      // Fulfilled state for login
      .addCase(login.fulfilled, (state, action) => {
        state.status = 'succeeded'
        state.token = action.payload.accessToken
        state.user = action.payload.user
        state.error = null

        localStorage.setItem('token', action.payload.accessToken)
        localStorage.setItem('user', JSON.stringify(action.payload.user))
      })
      // Rejected state for login
      .addCase(login.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload as string
      })
  },
})

export const { logout } = authSlice.actions

// Selectors to access pieces of state
export const selectAuthStatus = (state: RootState) => state.auth.status
export const selectAuthError = (state: RootState) => state.auth.error
export const selectCurrentUser = (state: RootState) => state.auth.user
export const selectCurrentToken = (state: RootState) => state.auth.token

export default authSlice.reducer
