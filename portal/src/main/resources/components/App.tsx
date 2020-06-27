import * as React from "react";
import {BrowserRouter as Router, Link, Redirect, Route, Switch} from "react-router-dom";

import {Projects} from './projects/Projects';
import {Kanban} from "./kanban/Kanban";

export function App() {
  return (
    <Router>
      <div>
        <nav>
          <ul>
            <li>
              <Link to="/projects">Project</Link>
            </li>
            <li>
              <Link to="/kanban">Kanban</Link>
            </li>
          </ul>
        </nav>

        {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
        <Switch>
          <Route path="/projects">
            <Projects/>
          </Route>
          <Route path="/kanban">
            <Kanban/>
          </Route>
          <Redirect from='*' to='/projects'/>
        </Switch>
      </div>
    </Router>
  );
}
