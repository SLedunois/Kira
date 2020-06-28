import * as ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {App} from "./features/app/App";

import * as React from "react";
import './assets/tailwind.css';

import store from './store';

ReactDOM.render(
  <Provider store={store}>
    <App/>
  </Provider>, document.getElementById("kyra")
);
