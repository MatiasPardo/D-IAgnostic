import { VerificationContext } from './VerificationContext'
import { UseVerification } from '../hooks/UseVerification'

export const VerificationProvider = ({children}) => {

    const { handlerRecoveryEmail } = UseVerification()

    return (
        <VerificationContext.Provider value={
            {
                handlerRecoveryEmail
            }
        }>
            {children}
        </VerificationContext.Provider>
    )
}
