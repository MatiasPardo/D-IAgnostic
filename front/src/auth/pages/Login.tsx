import {useContext, useState} from 'react';
import {NavLink, useNavigate} from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { AlertError } from '../../components/SweetAlert';

const initialLogin = {
  userName: '',
  password: '',
}

export const Login = () => {
    const navigate = useNavigate();

    const {handlerLogin} = useContext(AuthContext)
  
    const [formData, setFormData] = useState(initialLogin);

    const [errorLogin, setErrorLogin] = useState(false)

    const onInputChange = (e: any) => {
      const { name, value } = e.target;
      setFormData({
        ...formData,
        [name]: value,
      });
    };
  
    const onFormSubmit = async (e: any) => {
      e.preventDefault();
      
      handlerLogin({
          userName: formData.userName,
          password: formData.password
        })
        .then(() => navigate("/home"))        
        .catch((e:any) => {
          AlertError('Inicio de Sesión', 'Ocurrió un error al iniciar sesión', e);
          setErrorLogin(true)
        });
      setFormData(initialLogin)
    }

    
    

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className={`alert alert-warning ${errorLogin ? 'd-block' : 'd-none'}`}>
                      Error al iniciar sesión. Por favor, inténtalo de nuevo.
                    </div>
                    <div className="card">
                        <div className="card-header">Iniciar Sesión</div>
                        <div className="card-body">
                            <form onSubmit={onFormSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="userName" className="form-label">Nombre de Usuario:</label>
                                    <input
                                        type="text"
                                        id="userName"
                                        name="userName"
                                        value={formData.userName}
                                        onChange={onInputChange}
                                        className="form-control"
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="password" className="form-label">Contraseña:</label>
                                    <input
                                        type="password"
                                        id="password"
                                        name="password"
                                        value={formData.password}
                                        onChange={onInputChange}
                                        className="form-control"
                                        required
                                    />
                                </div>
                                <div className="d-grid">
                                    <button type="submit" className="btn btn-primary">Iniciar Sesión</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div className="mt-3 text-center">
                        <p>¿No tienes una cuenta?</p>
                        <NavLink className="btn btn-link" to="/register">Registrarse</NavLink>
                    </div>
                </div>
            </div>
        </div>
    );
};