import React, { useState } from 'react';
import { Modal } from 'react-bootstrap';

export const InputNameModal = ({ show, handleClose, handleFunc }: { show: any, handleClose: any, handleFunc: (title: string, patientName: string) => void }) => {
    const [title, setTitle] = useState('');
    const [patientName, setPatientName] = useState('');

    const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    }

    const handlePatientNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPatientName(event.target.value);
    }

    const handleAccept = () => {
        if (title && patientName) {
            handleFunc(title, patientName);
            handleClose();
        }
    }

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Completar los datos para generar el informe</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form>
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Título para la tomografía</label> 
                        <div className="col-8">
                            <input 
                                id="text" 
                                name="text" 
                                placeholder="Ingrese un título" 
                                type="text" 
                                className="form-control" 
                                value={title}
                                onChange={handleTitleChange}
                                required
                            />
                        </div>
                    </div>
                    <br/>
                    <div className="form-group row">
                        <label className="col-4 col-form-label">Nombre del paciente</label> 
                        <div className="col-8">
                            <input 
                                id="patientName" 
                                name="patientName" 
                                placeholder="Ingrese el nombre del paciente" 
                                type="text" 
                                className="form-control" 
                                value={patientName}
                                onChange={handlePatientNameChange}
                                required
                            />
                        </div>
                    </div>  
                    <div className="form-group row">
                        <div className="offset-4 col-8">
                            <br/>
                            <button 
                                disabled={!title || !patientName} 
                                type="button" 
                                onClick={handleAccept} 
                                className="btn btn-success container"
                            >
                                Aceptar
                            </button>
                        </div>
                    </div>
                </form>
            </Modal.Body>
        </Modal>
    );
};
