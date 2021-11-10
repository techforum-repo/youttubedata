import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import { ModelManager, ModelClient } from "@adobe/aem-spa-page-model-manager";

// Initialize the ModelManager before invoking ReactDOM.render(...).

//const modelClient = new ModelClient(process.env.REACT_APP_HOST_URI);

ModelManager.initializeAsync();


//ModelManager.initializeAsync({ modelClient});

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
