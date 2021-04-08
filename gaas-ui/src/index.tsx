import React from "react";
import ReactDOM from "react-dom";
import App from "./components/App";
import * as serviceWorker from "./serviceWorker";
import { BrowserRouter } from "react-router-dom";
import { CssBaseline } from "@material-ui/core";
import { StylesProvider } from "@material-ui/styles";
import GlobalStyles from "../src/components/GlobalStyles";
import "./styles/_App.scss";
import Keycloak from "keycloak-js";

// eslint-disable-next-line new-cap
const keycloak: Keycloak.KeycloakInstance = Keycloak();
keycloak.init({ onLoad: "login-required" })
  .success(() => {
ReactDOM.render(
    <React.StrictMode>
        <StylesProvider injectFirst>
            <GlobalStyles />
            <BrowserRouter>
                <CssBaseline />
                <App />
            </BrowserRouter>
        </StylesProvider>
    </React.StrictMode>,
    document.getElementById("root")
)})
.error((error) => console.log(error));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
