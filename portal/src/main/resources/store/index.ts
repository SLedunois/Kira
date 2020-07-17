import {configureStore} from "@reduxjs/toolkit"
import {combineReducers} from 'redux';

import appReducer from '../features/app/AppSlice';
import projectReducer from '../features/projects/ProjectSlice';
import kanbanReducer from '../features/kanban/KanbanSlice';

const reducer = combineReducers({
  appReducer,
  projectReducer,
  kanbanReducer
})

const store = configureStore({
  reducer
});

export type RootState = ReturnType<typeof reducer>;

export default store;
