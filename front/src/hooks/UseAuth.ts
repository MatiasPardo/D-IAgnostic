import { useReducer } from "react"
import { LoginReducer } from "../reducers/LoginReducer"
import { loginUser } from "../services/UsersApiClient"
import { Credentials } from "../interfaces/Credentials"

const initialLogin = JSON.parse(localStorage.getItem('login') || '{}') || {isAuth: false, user: undefined}

export const UseAuth = () => {
  const [login, dispatch] = useReducer(LoginReducer, initialLogin)

  const handlerLogin = async(credentials: Credentials) => {
    const userLogged = await loginUser(credentials)    
    dispatch({
        type: 'login',
        payload: userLogged.data
    })
    localStorage.setItem('token', userLogged.data.token);
    localStorage.setItem('login', JSON.stringify({
        isAuth: true,
        user: {
          userName: userLogged.data.userName,
          userId: userLogged.data.userId
        }
    }))
  }

  const handlerLogout = () => {
    dispatch({
        type: 'logout',
    })
    localStorage.removeItem('login')
    localStorage.removeItem('token');
  }

  return {
    login,
    handlerLogin,
    handlerLogout
  }
}
