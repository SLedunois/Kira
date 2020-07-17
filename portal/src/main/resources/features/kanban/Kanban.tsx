import * as React from "react";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {fetchKanban, moveTicket} from "./KanbanSlice";
import {RootState} from "../../store";

import {Loader} from "@ui/Loader";
import {Activity} from './components/Activity';
import {initials, prepareMembers, randomColor} from "../../utils";
import {UserBadge} from "@ui/UserBadge";
import {DragDropContext, DropResult} from 'react-beautiful-dnd';

export const Kanban = () => {
  const dispatch = useDispatch();
  const {loading, activities, activityOrder} = useSelector((state: RootState) => state.kanbanReducer);
  const {project} = useSelector((state: RootState) => state.appReducer);

  useEffect(() => {
    if (project) dispatch(fetchKanban(project.id))
  }, [dispatch, project]);

  const onDropHandler = (result: DropResult) => {
    console.warn(result);
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
      <div className="mb-6 flex flex-row justify-end">
        {
          project && prepareMembers(project.members).map(({first_name, last_name, color, email}) => (
            <div className="ml-1 mr-1" key={email}>
              <UserBadge label={initials(last_name, first_name)} color={color || randomColor()}/>
            </div>
          ))
        }
      </div>
      <DragDropContext onDragEnd={onDropHandler}>
        <div className="flex flex-row">
          {
            activityOrder.map(activityId => <Activity key={activities[activityId].id}
                                                      name={activities[activityId].name} id={activityId.toString()}
                                                      tickets={activities[activityId].tickets}/>)
          }
        </div>
      </DragDropContext>
    </div>
  );
};
