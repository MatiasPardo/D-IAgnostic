import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content'

export const alert = withReactContent(Swal)



export const AlertOk = (title: string, message: string) => {
    alert.fire({
        title: title,
        text: message,
        icon: 'success'
      });
}

export const AlertError = (title: string, message: string, errorMessage: string) => {
    message = `${message}. Mensaje para el desarrollador: ${errorMessage}`
    alert.fire({
        title: title,
        text: message,
        icon: 'error'
      });
}