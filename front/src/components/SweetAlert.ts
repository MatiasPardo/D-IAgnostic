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

export const AlertError = (title: string, message: string, errorMessage: any) => {
  if (String(errorMessage).includes("400")) {
      message = "Error de contraseña o usuario";
  }
  alert.fire({
      title: title,
      text: message,
      icon: 'error'
  });
};


export const AlertOkRedirect = (title: string, message: string, route: string) => {
  alert.fire({
    title: title,
    text: message,
    icon: 'success',
    willClose: () => {
        window.location.href = route;
    }
  });
}