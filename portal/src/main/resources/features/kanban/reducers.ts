import {PayloadAction} from "@reduxjs/toolkit";
import {IKanbanState} from "./KanbanSlice";
import {IActivity, ITicket} from "./types";

export const moveTicketReducer = (state: IKanbanState, action: PayloadAction<{ ticketId: number, source: any, destination: any }>) => {
  const {ticketId, source, destination} = action.payload;
  const {tickets, activities} = state;
  const ticket: ITicket = tickets[ticketId];

  if (!ticket) return;

  if (destination.droppableId === source.droppableId &&
    destination.index === source.index) {
    return;
  }

  const refreshIndex = (activity: IActivity) => activity.tickets.map((ticket: ITicket, index: number) => ticket.index = index);

  const start = activities[source.droppableId];
  const finish = activities[destination.droppableId];

  // Moving into the same list
  if (start === finish) {
    const newTickets = Array.from(start.tickets);
    newTickets.splice(source.index, 1);
    newTickets.splice(destination.index, 0, ticket);

    start.tickets = newTickets;
    refreshIndex(start);
    return;
  }

  // Moving from on list to another
  const startNewTickets = Array.from(start.tickets);
  startNewTickets.splice(source.index, 1);
  start.tickets = startNewTickets;
  refreshIndex(start);

  const finishNewTickets = Array.from(finish.tickets);
  finishNewTickets.splice(destination.index, 0, ticket);
  finish.tickets = finishNewTickets;
  refreshIndex(finish);
}
