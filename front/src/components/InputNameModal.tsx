import React, { useState } from 'react';
import { Modal } from 'react-bootstrap';

interface InputNameModalProps {
    show: boolean;
    handleClose: () => void;
    handleFunc: (
        title?: string,
        name?: string,
        lastName?: string,
        clinicalHistory?: string,
        dni?: string,
        birthDate?: string,
        sex?: string
    ) => void;
}

export const InputNameModal = ({ show, handleClose, handleFunc }: InputNameModalProps) => {
    const [title, setTitle] = useState('');
    const [name, setName] = useState('');
    const [lastName, setLastName] = useState('');
    const [clinicalHistory, setClinicalHistory] = useState('');
    const [dni, setDni] = useState('');
    const [birthDate, setBirthDate] = useState('');
    const [sex, setSex] = useState<string | undefined>(undefined);
    const [titleError, setTitleError] = useState(false);  // Estado para manejar el error del título

    const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTitle(e.target.value);
        setTitleError(false); // Limpiar error al escribir
    };

    const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => setName(e.target.value);
    const handleLastNameChange = (e: React.ChangeEvent<HTMLInputElement>) => setLastName(e.target.value);
    const handleClinicalHistoryChange = (e: React.ChangeEvent<HTMLInputElement>) => setClinicalHistory(e.target.value);
    const handleDniChange = (e: React.ChangeEvent<HTMLInputElement>) => setDni(e.target.value);
    const handleBirthDateChange = (e: React.ChangeEvent<HTMLInputElement>) => setBirthDate(e.target.value);
    const handleSexChange = (e: React.ChangeEvent<HTMLInputElement>) => setSex(e.target.value);

    const handleAccept = () => {
        if (!title) {
            setTitleError(true); // Marcar como error si no hay título
            return;
        }

        handleFunc(
            title,
            name || undefined,
            lastName || undefined,
            clinicalHistory || undefined,
            dni || undefined,
            birthDate || undefined,
            sex || undefined
        );
        handleClose();
    };

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Completar los datos para generar el informe</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form>
                    <div className="form-group row">
                        <label className="col-4 col-form-label">
                            Título para la tomografía <span className="text-danger">*</span>
                        </label>
                        <div className="col-8">
                            <input
                                type="text"
                                className={`form-control ${titleError ? 'is-invalid' : ''}`}
                                value={title}
                                onChange={handleTitleChange}
                                placeholder="Ingrese un título"
                                required
                            />
                            {titleError && <div className="invalid-feedback">El título es obligatorio.</div>}
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Nombre del paciente</label>
                        <div className="col-8">
                            <input
                                type="text"
                                className="form-control"
                                value={name}
                                onChange={handleNameChange}
                                placeholder="Ingrese el nombre del paciente"
                            />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Apellido del paciente</label>
                        <div className="col-8">
                            <input
                                type="text"
                                className="form-control"
                                value={lastName}
                                onChange={handleLastNameChange}
                                placeholder="Ingrese el apellido del paciente"
                            />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Historia clínica</label>
                        <div className="col-8">
                            <input
                                type="text"
                                className="form-control"
                                value={clinicalHistory}
                                onChange={handleClinicalHistoryChange}
                                placeholder="Ingrese la historia clínica"
                            />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <label className="col-4 col-form-label">DNI</label>
                        <div className="col-8">
                            <input
                                type="text"
                                className="form-control"
                                value={dni}
                                onChange={handleDniChange}
                                placeholder="Ingrese el DNI"
                            />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Fecha de nacimiento</label>
                        <div className="col-8">
                            <input
                                type="date"
                                className="form-control"
                                value={birthDate}
                                onChange={handleBirthDateChange}
                            />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Sexo</label>
                        <div className="col-8 d-flex">
                            <div className="form-check me-3">
                                <input
                                    className="form-check-input"
                                    type="radio"
                                    name="sex"
                                    value="masculino"
                                    onChange={handleSexChange}
                                    checked={sex === 'masculino'}
                                />
                                <label className="form-check-label">Masculino</label>
                            </div>
                            <div className="form-check">
                                <input
                                    className="form-check-input"
                                    type="radio"
                                    name="sex"
                                    value="femenino"
                                    onChange={handleSexChange}
                                    checked={sex === 'femenino'}
                                />
                                <label className="form-check-label">Femenino</label>
                            </div>
                        </div>
                    </div>
                    <br />
                    <div className="form-group row">
                        <div className="col-12 d-flex justify-content-end">
                            <button
                                type="button"
                                onClick={handleAccept}
                                className="btn"
                                style={{ backgroundColor: 'var(--accent-color)' }}
                            >
                                Solicitar Informe
                            </button>
                        </div>
                    </div>
                </form>
            </Modal.Body>
        </Modal>
    );
};
