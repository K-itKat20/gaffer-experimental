const _env = window as any;

export class Config {
    static REACT_APP_API_PLATFORM = _env.REACT_APP_API_PLATFORM;
    static REACT_APP_KAI_REST_API_HOST = _env.REACT_APP_KAI_REST_API_HOST;
    static REACT_APP_COGNITO_USERPOOLID = _env.REACT_APP_COGNITO_USERPOOLID;
    static REACT_APP_COGNITO_CLIENTID = _env.REACT_APP_COGNITO_CLIENTID;
    static REACT_APP_AUTH_ENDPOINT = _env.REACT_APP_AUTH_ENDPOINT;
}
