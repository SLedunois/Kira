import React from "react";
import ReactDOM from "react-dom";
import {Provider} from "react-redux";

import './i18n';

import {App} from "./features/app/App";
import './assets/tailwind.css';

import store from './store';

ReactDOM.render(
  <Provider store={store}>
    <App/>
  </Provider>, document.getElementById("kyra")
);
