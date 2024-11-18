import React, { useState } from 'react';
import { instance } from "../../services/BaseClient";
import { useNavigate } from 'react-router-dom';
import { AlertError, AlertOkRedirect } from '../../components/SweetAlert';
import './passwordRecovery.css';

export const PasswordRecoveryPage = () => {
  const navigate = useNavigate();

  const [modalPage, setModalPage] = useState(1);
  const [recoveryEmail, setRecoveryEmail] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [token, setToken] = useState('');
  const [errorVerificationEmail, setErrorVerificationEmail] = useState(false);
  const [errorVerificationCode, setErrorVerificationCode] = useState(false);
  const [newPassword, setNewPassword] = useState('');

  interface VerificationResponse {
    token: string;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (modalPage === 1) {
      console.log('Email submitted:', recoveryEmail);

    await instance.post<VerificationResponse>(`users/recovery`, { email:recoveryEmail })
    .then((response) => {
      setModalPage(2)
      setToken(response?.data?.token)
    })
      .catch((e: any) => {
        AlertError('Email Inválido', 'El correo ingresado es inválido', e);
        setErrorVerificationEmail(true);
      });

    } else {
      console.log('Code:', verificationCode, 'New Password:', newPassword);
      await instance.post<VerificationResponse>(`users/changePass`, 
        { email:recoveryEmail, token:token, newPassword: newPassword, code: verificationCode })
      .then(() => AlertOkRedirect('Contraseña cambiada', 'La contraseña se cambió correctamente','login'))
      .catch((e: any) => {
        AlertError('Código Inválido', 'El código ingresado es inválido', e);
        setErrorVerificationCode(true);
      });    
      
    }
  };

  return (
    <div className="recovery-container" style={{ backgroundColor: 'var(--primary-color-2)' }}>
      <div className="container mt-5">
        <div className="row justify-content-center">
          <div className="col-md-6">
          <div className={`alert alert-warning ${errorVerificationEmail ? 'd-block' : 'd-none'}`}>
              Error al validar el correo. Por favor, inténtalo de nuevo.
          </div>
          <div className={`alert alert-warning ${errorVerificationCode ? 'd-block' : 'd-none'}`}>
              Error al validar el código. Por favor, inténtalo de nuevo.
          </div>
            <div className="card">
              <div className="card-header">
                {modalPage === 1 ? 'Recuperar Contraseña' : 'Restablecer Contraseña'}
              </div>
              <div className="card-body">
                <form onSubmit={handleSubmit}>
                  {modalPage === 1 ? (
                    <>
                      <div className="mb-3">
                        <p>Para recuperar su contraseña por favor ingrese su correo electrónico y haga click en el botón "Enviar código".
                        <br/>Recibirá un código de recuperación en su correo.
                        </p>
                        <label htmlFor="recoveryEmail" className="form-label">
                          Correo Electrónico:
                        </label>
                        <input
                          type="email"
                          id="recoveryEmail"
                          value={recoveryEmail}
                          onChange={(e) => setRecoveryEmail(e.target.value)}
                          className="form-control"
                          required
                        />
                      </div>
                      <div className="d-grid">
                        <button
                          type="submit"
                          className="btn"
                          style={{
                            backgroundColor: 'var(--primary-color-1)',
                            color: 'var(--text-color-2)',
                          }}
                        >
                          Enviar Código
                        </button>
                      </div>
                    </>
                  ) : (
                    <>
                      <div className="mb-3">
                        <label htmlFor="verificationCode" className="form-label">
                          Código de Verificación:
                        </label>
                        <input
                          type="text"
                          id="verificationCode"
                          value={verificationCode}
                          onChange={(e) => setVerificationCode(e.target.value)}
                          className="form-control"
                          required
                        />
                      </div>
                      <div className="mb-3">
                        <label htmlFor="newPassword" className="form-label">
                          Nueva Contraseña:
                        </label>
                        <input
                          type="password"
                          id="newPassword"
                          value={newPassword}
                          onChange={(e) => setNewPassword(e.target.value)}
                          className="form-control"
                          required
                        />
                      </div>
                      <div className="d-grid">
                        <button
                          type="submit"
                          className="btn"
                          style={{
                            backgroundColor: 'var(--primary-color-1)',
                            color: 'var(--text-color-2)',
                          }}
                        >
                          Restablecer Contraseña
                        </button>
                      </div>
                    </>
                  )}
                </form>
              </div>
            </div>
            <div className="mt-3 text-center">
              <button
                type="button"
                className="btn btn-link"
                onClick={() => navigate('/login')}
              >
                Volver al inicio de sesión
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
