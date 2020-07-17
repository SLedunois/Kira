import {IActivity, ITicket} from "./types";
import {createSlice} from "@reduxjs/toolkit";

import {fetchKanban, moveTicket} from './thunks';
import {moveTicketReducer} from "./reducers";

export type IKanbanState = {
  loading: boolean,
  tickets: any,
  activities: any,
  activityOrder: number[]
}

const initialState: IKanbanState = {
  loading: false,
  activities: {},
  activityOrder: [],
  tickets: {}
}

const kanbanSlice = createSlice({
  name: 'kanban',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchKanban.pending.toString()]: state => {
      state.loading = true;
    },
    [fetchKanban.fulfilled.toString()]: (state, action) => {
      state.loading = false;
      state.activities = {};
      const activities = action.payload;
      state.activityOrder = new Array(activities.length);
      activities.forEach((activity: IActivity) => {
        state.activities[activity.id] = activity;
        state.activityOrder[activity.position] = activity.id;
        activity.tickets.forEach((ticket: ITicket) => state.tickets[ticket.id] = ticket);
      });
    },
    [fetchKanban.rejected.toString()]: state => {
      state.loading = false;
    },
    [moveTicket.fulfilled.toString()]: moveTicketReducer
  }
});

export {fetchKanban, moveTicket};

export default kanbanSlice.reducer;
