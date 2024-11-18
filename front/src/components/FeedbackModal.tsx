import React from 'react';
import { Modal } from 'react-bootstrap';

export const FeedbackModal = ({
    show,
    handleClose,
    handleFunc,
    feedback,
    codeReport,
    userId,
    isAnswerYesSelected,
    isAnswerNoSelected,
    selectedErrors
}: { 
    show: boolean, 
    handleClose: () => void, 
    handleFunc: (
        codeReport: string, 
        userId: string, 
        isAnswerYesSelected: boolean, 
        isAnswerNoSelected: boolean, 
        selectedErrors: string[], 
        feedback: string
    ) => void,
    feedback: string,
    codeReport: string,
    userId: string,
    isAnswerYesSelected: boolean,
    isAnswerNoSelected: boolean,
    selectedErrors: string[]
}) => {
    
    const handleAccept = () => {
        handleFunc(codeReport, userId, isAnswerYesSelected, isAnswerNoSelected, selectedErrors, feedback);
        handleClose();
    };

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>¿Está seguro que desea enviar el feedback?</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form>
                    <div className="form-group">
                        <label><strong>Código de Reporte:</strong> {codeReport}</label>
                    </div>
                    <div className="form-group">
                        <label><strong>¿El informe es correcto?:</strong> {isAnswerYesSelected ? 'Sí' : 'No'}</label>
                    </div>
                    <div className="form-group">
                        <label><strong>Feedback:</strong> {feedback || '-'}</label>
                    </div>
                    <div className="form-group">
                        <label><strong>Errores Seleccionados:</strong> {selectedErrors.join(', ') || '-'}</label>
                    </div>
                    <div className="form-group row">
                        <div className="col-12 d-flex justify-content-end">
                            <button 
                                type="button" 
                                onClick={handleAccept} 
                                className="btn" 
                                style={{ backgroundColor: 'var(--accent-color)' }}
                            >
                                Enviar
                            </button>
                        </div>
                    </div>
                </form>
            </Modal.Body>
        </Modal>
    );
};