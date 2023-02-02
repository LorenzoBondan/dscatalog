import axios, { AxiosRequestConfig } from 'axios';
import qs from 'qs';
import history from './history';
import { getAuthData } from './storage';


export const BASE_URL = process.env.REACT_APP_BACKEND_URL ?? 'http://localhost:8080';

const CLIENT_ID = process.env.REACT_APP_CLIENT_ID ?? 'dscatalog';
const CLIENT_SECRET = process.env.REACT_APP_CLIENT_SECRET ?? 'dscatalog123';


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

    if(error.response.status === 401 ){
        history.push('/admin/auth');
    }
    console.log("INTERCEPTOR COM ERRO");
    return Promise.reject(error);
  });



