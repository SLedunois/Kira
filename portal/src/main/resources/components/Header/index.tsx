import * as React from "react";
import {match, withRouter} from "react-router-dom";

import {Notification} from './Notification'
import {Profile} from "./Profile";

import Logo from '@assets/img/logo.svg';
import {useTranslation} from "react-i18next";
import {Project} from "../../features/projects/types";
import {Select} from "@ui/Select";
import {History, Location} from "history";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {Button} from "@ui/Button";

import {setProject} from "../../features/app/AppSlice";
import {openTicketModal} from "../../features/kanban/KanbanSlice";

interface IHeader {
  match: match
  location: Location,
  history: History
}

const HeaderComponent = ({location}: IHeader) => {
  const {t} = useTranslation();
  const dispatch = useDispatch();

  const {user} = useSelector((state: RootState) => state.appReducer);
  const {projects} = useSelector((state: RootState) => state.projectReducer);
  const {project} = useSelector((state: RootState) => state.appReducer);

  const onProjectChange = (project: Project) => {
    dispatch(setProject(project));
  }

  const onCreateButtonClick = () => {
    dispatch(openTicketModal({
      name: '',
      content: '',
      assignee: null,
      owner: null,
      activity_id: null
    }));
  }

  return (
    <div className="bg-white h-16 border-b-2 border-border flex items-center justify-between fixed w-full top-0 left-0">
      <div className="flex flex-row items-center justify-between">
        <img src={Logo} alt="Kyra" className="ml-6 mr-8"/>
        {
          projects.length > 0 && project && location.pathname !== '/projects' &&
          <div className="flex flex-row justify-center items-center">
            <div className="uppercase font-bold">
              {t('project')} :
            </div>
            <Select selected={project} options={projects} onChange={onProjectChange}/>
          </div>
        }
        {
          location.pathname === '/kanban' && <Button label={t('create')} onClick={onCreateButtonClick}/>
        }
      </div>
      <div className="h-full flex">
        <Notification/>
        {user && <Profile firstName={user.first_name} lastName={user.last_name} color={user.color}/>}
      </div>
    </div>
  )
};

export const Header = withRouter(HeaderComponent);
