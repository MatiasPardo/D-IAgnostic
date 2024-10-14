import { useContext } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';

export const Navbar = () => {

    const { login, handlerLogout } = useContext(AuthContext)

    const navigate = useNavigate();

    const onLogout = () => {
        
        handlerLogout();
        navigate('/login');
        localStorage.clear(); 
        sessionStorage.clear();
        localStorage.removeItem('tomographyFilters');
        localStorage.removeItem('totalTomographies');
        console.log("Cache cleared successfully");
        window.location.reload();
    }

    return (
        <nav className="navbar navbar-expand-sm navbar-dark bg-dark p-2" style={{ position: 'fixed', top: 0, width: '100%', zIndex: 1000 }}>
            <Link className="navbar-brand" to="/" style={{color: 'var(--primary-color-2)'}}>Home</Link>
            <div className="navbar-collapse">
                <div className="navbar-nav">
                    <NavLink className="nav-item nav-link" to="/tomographies">Tomografias</NavLink>
                    <NavLink className="nav-item nav-link" to="/orders">Solicitar Informe</NavLink>
                </div>
            </div>
            <div className="navbar-collapse collapse order-3 dual-collapse2 d-flex justify-content-end">
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
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            <NavLink className="nav-item nav-link btn" to="/register">Registro</NavLink>
                            <div style={{color: 'var(--primary-color-2)'}}> / </div>
                            <NavLink className="nav-item nav-link btn" to="/login">Login</NavLink>
                        </div>
                    }
                </ul>
            </div>
        </nav>
    )
}