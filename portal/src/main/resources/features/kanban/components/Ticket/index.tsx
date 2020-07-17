import React from 'react';
import {ITicket} from "../../types";
import {Draggable} from "react-beautiful-dnd";

type ITicketComponent = {
  ticket: ITicket
  index: number
}
export const Ticket = ({ticket, index}: ITicketComponent) => (
  <Draggable key={ticket.id} draggableId={ticket.id.toString()}
             index={index}>
    {(provided, snapshot) => (
      <div
        className={`bg-white w-full mb-2 rounded-lg min-h-4 p-2 transition duration-300 ease-in-out ${snapshot.isDragging ? 'shadow-2xl' : ''}`}
        {...provided.dragHandleProps}
        {...provided.draggableProps}
        ref={provided.innerRef}
      >
        <span className="text-lg">
          {ticket.name}
        </span>
      </div>
    )}
  </Draggable>
);
