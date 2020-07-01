import * as React from "react";
import {History, Location} from "history";
import {Link, match, withRouter} from "react-router-dom";

import {Kanban, Project} from '@ui/icons';

import './navigation.css'

interface INavigation {
  match: match
  location: Location,
  history: History
}

interface INavigationItems {
  link: string,
  icon: React.ReactElement
}

const items = (location: Location): INavigationItems[] => [
  {
    link: '/projects',
    icon: <Project className={`w-7 ${location.pathname === '/projects' ? 'stroke-primary' : 'stroke-black'}`}/>
  },
  {
    link: '/kanban',
    icon: <Kanban className={`w-7 ${location.pathname === '/kanban' ? 'fill-primary' : 'fill-black'}`}/>,
  }
];

const NavigationItem = (props: INavigation) => (
  <nav className="w-20 bg-white border-r-2 border-border fixed">
    <ul className="mt-8">
      {
        items(props.location).map(({link, icon}) => (
          <li className="w-full h-20" key={link}>
            <Link to={link} className="w-full h-full flex items-center justify-center">{icon}</Link>
          </li>
        ))
      }
    </ul>
  </nav>
);


export const Navigation = withRouter(NavigationItem);
