import * as React from "react";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {DragDropContext, DropResult} from 'react-beautiful-dnd';

import {fetchKanban, moveTicket} from "./KanbanSlice";
import {RootState} from "../../store";

import {Loader} from "@ui/Loader";
import {Activity} from './components/Activity';
import {TicketModal} from './components/Modal';

export const Kanban = () => {
  const dispatch = useDispatch();
  const {loading, activities, activityOrder, ticket} = useSelector((state: RootState) => state.kanbanReducer);
  const {project} = useSelector((state: RootState) => state.appReducer);

  useEffect(() => {
    if (project) dispatch(fetchKanban(project.id))
  }, [dispatch, project]);

  const onDropHandler = (result: DropResult) => {
    const {destination, source, draggableId} = result;

    if (!destination) {
      return;
    }

    if (destination.droppableId == source.droppableId && destination.index == source.index) {
      return;
    }

    dispatch(moveTicket({kanbanId: project.id, ticketId: parseInt(draggableId), source, destination}));
  }

  if (loading) {
    return <Loader/>
  }

  return (
    <div className="p-8">
      <DragDropContext onDragEnd={onDropHandler}>
        <div className="flex flex-row">
          {
            activityOrder.map(activityId => <Activity key={activities[activityId].id}
                                                      name={activities[activityId].name} id={activityId.toString()}
                                                      tickets={activities[activityId].tickets}/>)
          }
        </div>
      </DragDropContext>
      <TicketModal/>
    </div>
  );
};
