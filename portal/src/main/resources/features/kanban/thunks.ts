import http from 'axios';
import {createAsyncThunk} from "@reduxjs/toolkit";

export const fetchKanban = createAsyncThunk(
  'kanbans/fetch',
  async (projectId: number) => {
    const {data} = await http.get(`/api/v1/kanbans/${projectId}`);
    return data;
  }
)

export const moveTicket = createAsyncThunk(
  'kanbans/move_ticket',
  async (move: { kanbanId: number, ticketId: number, source: any, destination: any }) => {
    const {ticketId, kanbanId, destination} = move;
    const {data} = await http.patch(`/api/v1/kanbans/${kanbanId}/tickets/${ticketId}`, {
      activity_id: parseInt(destination.droppableId),
      index: parseInt(destination.index)
    });
    return {...move, destination: {droppableId: data.activity_id, index: data.index}};
  }
)
