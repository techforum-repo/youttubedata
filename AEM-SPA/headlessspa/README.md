# AEM - SPA(React) Headless

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

Change the AEM server details into .env.development(create seperate file for different environments)

REACT_APP_HOST_URI=http://localhost:4502 <br />
REACT_APP_GRAPHQL_ENDPOINT=/content/_cq_graphql/global/endpoint.json <br />
REACT_APP_AEM_USER=admin <br />
REACT_APP_AEM_PASS=admin

Recommended to use AEM publish instance for consuming the headless content from external App

### `npm install`

Run the npm install command at the root folder

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser. The AEM server should be runnning and the content fragments should be enabled along with other required configurations e.g CORS

The page will reload if you make edits.\
You will also see any lint errors in the console.

