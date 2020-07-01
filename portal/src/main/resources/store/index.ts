import {configureStore} from "@reduxjs/toolkit"
import {combineReducers} from 'redux';

import appReducer from '../features/app/AppSlice';
import projectReducer from '../features/projects/ProjectSlice';

const reducer = combineReducers({
  appReducer,
  projectReducer
})

const store = configureStore({
  reducer
});

export type RootState = ReturnType<typeof reducer>;

export default store;
