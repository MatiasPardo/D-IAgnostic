import { AuthContext } from './AuthContext'
import { UseAuth } from '../hooks/UseAuth'

export const AuthProvider = ({children}) => {

    const {login, handlerLogin, handlerLogout} = UseAuth()

    return (
        <AuthContext.Provider value={
            {
                login,
                handlerLogin,
                handlerLogout
            }
        }>
            {children}
        </AuthContext.Provider>
    )
}
