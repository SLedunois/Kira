import React from 'react';
import {Member, Project} from "../../types";
import {UserBadge} from "@ui/UserBadge";
import {initials, randomColor} from "../../../../utils";
import {DotMenu, DotMenuItem} from "@ui/DotMenu";

type IProjectsList = {
  projects: Project[],
  edit: any
}

export const ProjectsList = ({projects, edit}: IProjectsList) => {
  function prepareMembers(members: Member[]) {
    const arr = members.length > 5 ? members.slice(0, 5) : members;
    if (members.length > 5) arr.push({
      email: null,
      first_name: '+',
      last_name: (members.length - arr.length).toString(),
      color: 'grey'
    });

    return arr;
  }

  return (
    <div className="projects-list">
      {
        projects.map(project => (
          <div className="bg-white rounded-md flex justify-between items-center h-20 mb-4 flex flex-row"
               key={project.id}>
            <div className="text-2xl ml-8 flex-1">{project.name}</div>
            <div className="flex flex-row flex-1">
              {
                prepareMembers(project.members).map(({first_name, last_name, color, email}) => (
                  <div className="ml-3 mr-2" key={email}>
                    <UserBadge label={initials(last_name, first_name)} color={color || randomColor()}/>
                  </div>
                ))
              }
            </div>
            <div className="flex-1 flex justify-end mr-8">
              <DotMenu>
                <DotMenuItem label="Edit" onClick={() => edit(project)}/>
              </DotMenu>
            </div>
          </div>
        ))
      }
    </div>
  )
}
