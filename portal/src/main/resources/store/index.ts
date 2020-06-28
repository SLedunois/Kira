import {configureStore} from "@reduxjs/toolkit"
import {combineReducers} from 'redux';

import appReducer from '../features/app/AppSlice';

const reducer = combineReducers({
  appReducer
})

const store = configureStore({
  reducer
});

export type RootState = ReturnType<typeof reducer>;

export default store;
