import React, { useState } from 'react';

interface PasswordRecoveryModalProps {
  show: boolean;
  onClose: () => void;
}

export const PasswordRecoveryModal: React.FC<PasswordRecoveryModalProps> = ({ show, onClose }) => {
  const [modalPage, setModalPage] = useState(1);
  const [recoveryEmail, setRecoveryEmail] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (modalPage === 1) {
      console.log("Email submitted:", recoveryEmail);
      setModalPage(2);
    } else {
      console.log("Code:", verificationCode, "New Password:", newPassword);
      onClose();
      setModalPage(1);
    }
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setNewPassword(value);

    if (!passwordRegex.test(value)) {
      setPasswordError('La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula, un número y un carácter especial.');
    } else {
      setPasswordError('');
    }
  };

  if (!show) return null;

  return (
    <div className="modal" style={{ display: 'block', backgroundColor: 'rgba(0, 0, 0, 0.5)' }}>
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Recuperar Contraseña</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            {modalPage === 1 ? (
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="recoveryEmail" className="form-label">Correo Electrónico:</label>
                  <input
                    type="email"
                    id="recoveryEmail"
                    value={recoveryEmail}
                    onChange={(e) => setRecoveryEmail(e.target.value)}
                    className="form-control"
                    required
                  />
                </div>
                <button type="submit" className="btn btn-primary">Enviar Código</button>
              </form>
            ) : (
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="verificationCode" className="form-label">Código de Verificación:</label>
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
                  <label htmlFor="newPassword" className="form-label">Nueva Contraseña:</label>
                  <input
                    type="password"
                    id="newPassword"
                    value={newPassword}
                    onChange={handlePasswordChange}
                    className="form-control"
                    required
                  />
                  {passwordError && <div className="text-danger">{passwordError}</div>}
                </div>
                <button type="submit" className="btn btn-primary" disabled={!!passwordError}>Restablecer Contraseña</button>
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
