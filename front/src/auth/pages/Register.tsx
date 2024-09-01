import React, { useState } from "react";
import { createUser } from "../../services/UsersApiClient";
import { CreateUser } from "../../interfaces/CreateUser";
import { NavLink, useNavigate } from "react-router-dom";
import { AlertError, AlertOk } from "../../components/SweetAlert";
import AppLogo from '../../components/AppLogo';

import './register.css';

export const Register = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const handleSubmit = async (event: any) => {
        event.preventDefault();
        const user: CreateUser = { userName: username, password, email };

        createUser(user)
            .then(r => {
                setMessage(`User ${r.username} created successfully!`);
                AlertOk('Registro', 'Tu usuario se creó correctamente!');
                navigate('/login');
            })
            .catch(e => {
                AlertError('Registro', 'Ocurrió un error la registrar tu usuario', e);
                setMessage('Error creating user. Please try again.');
            });
    }
    return (
        <div className="register-container" style={{ backgroundColor: 'var(--primary-color-2)'}}>
            <div className="container mt-5">
                <div className="row justify-content-center">
                    <div className="col-md-6">
                        <div className="card">
                        <div className='mx-auto'> <AppLogo size="325" transparent/> </div>
                            <div className="card-header">Registro</div>
                            <div className="card-body">
                                <form onSubmit={handleSubmit}>
                                    <div className="mb-3">
                                        <label htmlFor="username" className="form-label">
                                            Username:
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            id="username"
                                            value={username}
                                            onChange={(e) => setUsername(e.target.value)}
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="password" className="form-label">
                                            Password:
                                        </label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            id="password"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="email" className="form-label">
                                            Email:
                                        </label>
                                        <input
                                            type="email"
                                            className="form-control"
                                            id="email"
                                            value={email}
                                            onChange={(e) => setEmail(e.target.value)}
                                        />
                                    </div>
                                    <div className="d-grid">
                                        <button type="submit" className="btn" style={{backgroundColor: 'var(--primary-color-1)', color: 'var(--text-color-2)'}}>
                                            Registrarse
                                        </button>
                                    </div>
                                </form>
                                {message && <p>{message}</p>}
                            </div>
                        </div>
                        <div className="mt-3 text-center">
                            <p>¿Ya tienes una cuenta?</p>
                            <NavLink className="btn btn-link" to="/login">Inicia Sesión</NavLink>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
    
}