import {User} from "./user";
import {Role} from "./role";

export interface Profile {
  user?:User;
  roles?:Role;
  access_token?:string;
  refresh_token?:string;
}
export interface LoginState {
  dataState: DataState;
  loginSuccess?: boolean;
  error?: string;
  message?: string;
  isUsingMfa?: boolean;
  phone?: string;
}
export enum Key{
  TOKEN='[Key] TOKEN',
  REFRESH_TOKEN='[REFRESH] REFRESH_TOKEN'
}
export enum DataState {
  LOADING="LOADING_STATE",
  LOADED="LOADED_STATE",
  ERROR="ERROR_STATE",
}

export interface CustomHttpResponse<T>
{
  timestamp: Date;
  statusCode: number;
  status: string;
  message: string;
  reason?: string;
  developerMessage?: string;
  data?: T;
}
