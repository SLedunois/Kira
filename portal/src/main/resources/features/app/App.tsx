import React, {useEffect} from "react";
import {HashRouter as Router, Redirect, Route, Switch} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";

import {Header} from "@ui/Header";
import {Navigation} from "@ui/Navigation";
import {Projects} from '../projects/Projects';
import {Kanban} from "../kanban/Kanban";

import {fetchUser} from './AppSlice';

import './app.css';

export function App() {
  const dispatch = useDispatch();

  const {user} = useSelector((state: RootState) => state.appReducer);

  useEffect(() => {
    dispatch(fetchUser())
  }, [dispatch]);

  return (
    <div className="min-h-full">
      <Header user={user}/>
      <Router>
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
