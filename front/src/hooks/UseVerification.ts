import { useReducer } from "react"
import { VerificationMailReducer } from "../reducers/VerificationMailReducer"
import { recoverySendEmail } from "../services/UsersApiClient"
import { Email } from "../interfaces/Email"

const initialEmail: Email = {
  email: ""
}

export const UseVerification = () => {
  const [email, dispatch] = useReducer(VerificationMailReducer, initialEmail)

  const handlerRecoveryEmail = async(email: Email) => {
    try {
    const emailVerificationSuccess = await recoverySendEmail(email)    
    dispatch({
        type: 'SEND_VERIFICATION_MAIL',
        payload: emailVerificationSuccess.data,
      });
    } catch (error) {
      console.error("Error al verificar el correo:", error);
    }
  };

  return {
    email,
    handlerRecoveryEmail
  }
}
