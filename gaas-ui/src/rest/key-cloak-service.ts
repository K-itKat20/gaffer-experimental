import Keycloak from "keycloak-js";
// eslint-disable-next-line new-cap
const _kc:Keycloak.KeycloakInstance =  Keycloak("/keycloak.json");


const initKeycloak = (onAuthenticatedCallback:any) => {
    _kc.init({ onLoad: "login-required" })
      .success(() => onAuthenticatedCallback())
      .error((error) => console.log(error));
};

const doLogin = _kc.login;

const doLogout = _kc.logout;

const getToken = () => _kc.token;

const isLoggedIn = () => !!_kc.token;

const KeyCloakService = {
  initKeycloak,
  doLogin,
  doLogout,
  isLoggedIn,
  getToken
};

export default KeyCloakService;
