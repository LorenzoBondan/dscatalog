import { Method } from "axios";

// PARÂMETROS DA REQUISIÇÃO DO AXIOS
export type AxiosParams = {
    method?: Method;
    url: string;
    data?: object;
    params?: object;
  };

  
  