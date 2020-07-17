import React from 'react';

type ITaskList = {
  isDraggingOver: any
  innerRef: any
  children?: any
}

export const TaskList = (props: ITaskList) => (
  <div ref={props.innerRef}
       className={`min-h-12 p-2 rounded-lg transition duration-300 ease-in-out ${props.isDraggingOver ? 'bg-secondary-50' : ''}`}>
    {props.children}
  </div>
);
