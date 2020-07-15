import http from 'axios';
import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";

type AppState = {
  loading: boolean
  user: {
    email: string
    first_name: string
    last_name: string
  }
}

export const initialState: AppState = {
  loading: false,
  user: {
    email: null,
    first_name: null,
    last_name: null
  }
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
  reducers: {},
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

export default appSlice.reducer;
