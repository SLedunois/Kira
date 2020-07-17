import React from 'react';
import {Droppable} from "react-beautiful-dnd";

import {TaskList} from '../TaskList'
import {ITicket} from "../../types";
import {Ticket} from "../Ticket";

type IActivity = {
  id: string
  name: string
  tickets: ITicket[]
}

export const Activity = ({id, name, tickets}: IActivity) => {
  return (
    <div className="flex-1 mr-10">
      <div className="font-bold text-2xl mb-4 p-2">{name}</div>
      <Droppable droppableId={id}>
        {
          (provided, snapshot) => (
            <TaskList innerRef={provided.innerRef} {...provided.droppableProps}
                      isDraggingOver={snapshot.isDraggingOver}>
              {
                tickets.map((ticket) => (
                  <Ticket ticket={ticket} index={ticket.index} key={ticket.id}/>
                ))
              }
              {
                provided.placeholder
              }
            </TaskList>
          )
        }
      </Droppable>
    </div>
  )
}
