import axios, { AxiosRequestConfig } from 'axios';
import jwtDecode from 'jwt-decode';
import qs from 'qs';
import history from './history';

/* JSON do endpoint Auth */
type LoginResponse = {
   access_token: string;
   token_type: string;
   expires_in: number;
   scope: string;
   userFirstName: string;
   userId: number;
}

type TokenData = {
    exp : number;
    user_name : string;
    authorities : Role[];
}

type Role = 'ROLE_OPERATOR' | 'ROLE_ADMIN';

export const BASE_URL = process.env.REACT_APP_BACKEND_URL ?? 'http://localhost:8080';

const CLIENT_ID = process.env.REACT_APP_CLIENT_ID ?? 'dscatalog';
const CLIENT_SECRET = process.env.REACT_APP_CLIENT_SECRET ?? 'dscatalog123';

const tokenKey = 'authData';

/* função requisição de login */

type LoginData = {
    username : string;
    password : string;
}

export const requestBackendLogin = (loginData : LoginData) => {

    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: 'Basic ' + window.btoa(CLIENT_ID + ':' + CLIENT_SECRET)
    }

    const data = qs.stringify({
        ...loginData, /*username e password */
        grant_type : 'password'
    });

    return axios({method: 'POST', baseURL: BASE_URL, url: '/oauth/token', data: data, headers: headers})
}

export const requestBackend = (config : AxiosRequestConfig) => {

    const headers = config.withCredentials ? {
        ...config.headers, // aproveita o que já tinha no headers, que foi passado, e acrescenta no authorization
        Authorization : "Bearer " + getAuthData().access_token
    } : config.headers;

    return axios({...config, baseURL: BASE_URL, headers});
}

/*  Salvando os dados de autenticação no localStorage */
export const saveAuthData = (obj : LoginResponse) => {
    localStorage.setItem(tokenKey, JSON.stringify(obj)); 
}

export const getAuthData = () => {
    const str = localStorage.getItem(tokenKey) ?? "{}";
    return JSON.parse(str) as LoginResponse;
}

/* axios interceptors */ 

// Add a request interceptor
axios.interceptors.request.use(function (config) {
    console.log("INTERCEPTOR ANTES DA REQUISIÇÃO");
    return config;
  }, function (error) {
    console.log("INTERCEPTOR ERRO NA REQUISIÇÃO");
    return Promise.reject(error);
  });

// Add a response interceptor
axios.interceptors.response.use(function (response) {
    console.log("INTERCEPTOR RESPOSTA COM SUCESSO");
    return response;
  }, function (error) {

    if(error.response.status === 401 || error.response.status === 403){
        history.push('/admin/auth');
    }
    console.log("INTERCEPTOR COM ERRO");
    return Promise.reject(error);
  });


  // função para decodificar o token
  export const getTokenData = () : TokenData | undefined => {
    // yarn add jwt-decode @types/jwt-decode
    try {
        return jwtDecode(getAuthData().access_token) as TokenData;
    }
    catch(error){
        return undefined;
    }
  }

  // função para testar se o usuário está autenticado
  export const isAuthenticated = () : boolean => {
    const tokenData = getTokenData();

    return (tokenData && tokenData.exp * 1000 > Date.now()) ? true : false;
  }