import http from 'axios';
import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";

export const initialState = {
  loading: false,
  user: {}
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
