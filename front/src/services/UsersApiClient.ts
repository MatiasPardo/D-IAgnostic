import {CreateUser} from "../interfaces/CreateUser";
import {Credentials} from "../interfaces/Credentials";
import {User} from "../interfaces/User";
import {instance} from "./BaseClient";

const endpoint = 'users'

export let user = {
  user: <User>{},
  onUserChanges: (user: User) => {}
}

export const createUser = (user: CreateUser): Promise<any> => {
    return instance.post(endpoint, user);
}

export const loginUser = (credentials: Credentials): Promise<any> => {
    return instance.post(`${endpoint}/login`, credentials);   
}

export const logoutUser = () => {
  user.user = <User>{};
  user.onUserChanges(user.user);
  localStorage.removeItem('token');
}
