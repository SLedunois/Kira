import React from 'react';
import {Project} from "../../types";
import {UserBadge} from "@ui/UserBadge";
import {initials, prepareMembers} from "../../../../utils";
import {DotMenu, DotMenuItem} from "@ui/DotMenu";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store";
import {useTranslation} from "react-i18next";

type IProjectsList = {
  projects: Project[]
  edit: any
  drop: any
}

export const ProjectsList = ({projects, edit, drop}: IProjectsList) => {
  const {t} = useTranslation();
  const {user} = useSelector((state: RootState) => state.appReducer);

  return (
    <div className="projects-list">
      {
        projects.map(project => (
          <div
            className="bg-white rounded-md flex justify-between items-center h-20 mb-4 flex flex-row shadow-sm transition ease-in-out duration-200 hover:shadow-lg"
            key={project.id}>
            <div className="text-2xl ml-8 flex-1">{project.name}</div>
            <div className="flex flex-row flex-1">
              {
                prepareMembers(project.members).map(({first_name, last_name, color, email}) => (
                  <div className="-mr-2" key={email}>
                    <UserBadge label={initials(last_name, first_name)} color={color} border={true}/>
                  </div>
                ))
              }
            </div>
            <div className="flex-1 flex justify-end mr-8"> {
              user.email === project.owner ?
                <DotMenu>
                  <DotMenuItem label={t('edit')} onClick={() => edit(project)}/>
                  <DotMenuItem label={t('drop')} onClick={() => drop(project)}/>
                </DotMenu>
                : ''
            }
            </div>
          </div>
        ))
      }
    </div>
  )
}
