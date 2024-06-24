import { useContext } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';

export const Navbar = () => {

    const { login, handlerLogout } = useContext(AuthContext)

    const navigate = useNavigate();

    const onLogout = () => {
        handlerLogout();
        navigate('/login');
    }

    return (
        <nav className="navbar navbar-expand-sm navbar-dark bg-dark p-2">
            <Link className="navbar-brand" to="/">Home</Link>
            <div className="navbar-collapse">
                <div className="navbar-nav">
                    <NavLink className="nav-item nav-link" to="/products">Productos</NavLink>
                    <NavLink className="nav-item nav-link" to="/orders">Pedidos</NavLink>
                </div>
            </div>
            <div className="navbar-collapse collapse w-100 order-3 dual-collapse2 d-flex justify-content-end">
                <ul className="navbar-nav ml-auto">
                    { login.isAuth ?
                        <>
                            <span className="nav-item nav-link text-primary mx-3">
                                {login.user.userName}
                            </span>
                            <button
                                className="nav-item nav-link btn"
                                onClick={ onLogout }>
                                Logout
                            </button>
                        </> 
                        :
                            <NavLink className="nav-item nav-link btn" to="/login">Login</NavLink>
                    }
                </ul>
            </div>
        </nav>
    )
}