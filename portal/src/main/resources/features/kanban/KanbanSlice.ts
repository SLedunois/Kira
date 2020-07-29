import {IActivity, ITicket} from "./types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";

import {createTicket, fetchKanban, moveTicket} from './thunks';
import {moveTicketReducer} from "./reducers";

export type IKanbanState = {
  loading: boolean,
  ticket: ITicket,
  tickets: any,
  activities: any,
  activityOrder: number[]
}

const initialState: IKanbanState = {
  loading: false,
  activities: {},
  activityOrder: [],
  tickets: {},
  ticket: null
}

const kanbanSlice = createSlice({
  name: 'kanban',
  initialState,
  reducers: {
    openTicketModal: (state, action: PayloadAction<ITicket>) => {
      state.ticket = action.payload;
    },
    cancelTicketCreation: state => {
      state.ticket = null;
    }
  },
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
    [moveTicket.fulfilled.toString()]: moveTicketReducer,
    [createTicket.fulfilled.toString()]: (state, action) => {
      const ticket = action.payload;
      state.activities[ticket.activity_id].tickets.push(ticket);
      state.tickets[ticket.id] = ticket;
      state.ticket = null;
    }
  }
});

export const {openTicketModal, cancelTicketCreation} = kanbanSlice.actions;

export {fetchKanban, moveTicket, createTicket};

export default kanbanSlice.reducer;
