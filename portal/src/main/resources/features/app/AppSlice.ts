import http from 'axios';
import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Project} from "../projects/types";

type AppState = {
  loading: boolean
  project: Project
  user: {
    email: string
    first_name: string
    last_name: string,
    color?: string
  }
}

export const initialState: AppState = {
  loading: false,
  project: null,
  user: null
}

export const fetchUser = createAsyncThunk(
  'user/fetch',
  async (thunkAPI) => {
    const response = await http.get('/account/user');
    return response.data;
  }
)

const appSlice = createSlice({
  name: 'app',
  initialState,
  reducers: {
    setProject: (state, action: PayloadAction<Project>) => {
      state.project = action.payload;
    }
  },
  extraReducers: {
    [fetchUser.pending.toString()]: state => {
      state.loading = true
    },
    [fetchUser.fulfilled.toString()]: (state, action) => {
      state.loading = false;
      state.user = action.payload;
    },
    [fetchUser.rejected.toString()]: state => {
      state.loading = false;
    }
  }
});

export const {setProject} = appSlice.actions;

export default appSlice.reducer;
