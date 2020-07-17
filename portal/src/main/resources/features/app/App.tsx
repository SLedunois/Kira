import React, {useEffect} from "react";
import {HashRouter as Router, Redirect, Route, Switch} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";

import {Header} from "@ui/Header";
import {Navigation} from "@ui/Navigation";
import {Projects} from '../projects/Projects';
import {Kanban} from "../kanban/Kanban";

import {fetchUser, setProject} from './AppSlice';

import './app.css';
import {fetchProjects} from "../projects/ProjectSlice";
import {RootState} from "../../store";

export function App() {
  const dispatch = useDispatch();
  const {project} = useSelector((state: RootState) => state.appReducer);
  const {projects} = useSelector((state: RootState) => state.projectReducer);

  useEffect(() => {
    dispatch(fetchUser())
    dispatch(fetchProjects(1));
  }, [dispatch]);

  useEffect(() => {
    if (!project) dispatch(setProject(projects[0]));
  }, [projects])

  return (
    <div className="min-h-full">
      <Router>
        <Header/>
        <div className="min-h-full">
          <Navigation/>
          <main className="content mt-16 ml-20" role="main">
            <Switch>
              <Route path="/projects">
                <Projects/>
              </Route>
              <Route path="/kanban">
                <Kanban/>
              </Route>
              <Redirect from='*' to='/projects'/>
            </Switch>
          </main>
        </div>
      </Router>
    </div>
  );
}
