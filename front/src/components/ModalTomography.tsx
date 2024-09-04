import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { Tomography } from '../interfaces/Tomography';

interface ModelTomographyProps {
  isModalOpen: boolean;
  closeModal: () => void;
  tomography: Tomography;
}

const ModelTomography: React.FC<ModelTomographyProps> = ({ isModalOpen, closeModal, tomography }) => {
  const [showErrorSection, setShowErrorSection] = useState(false);
  const [selectedErrors, setSelectedErrors] = useState<string[]>([]);

  const handleNoClick = () => {
    setShowErrorSection(true);
  };

  const handleYesClick = () => {
    setShowErrorSection(false);
  };

  const handleErrorOptionChange = (option: string) => {
    setSelectedErrors((prevSelected) =>
      prevSelected.includes(option)
        ? prevSelected.filter((err) => err !== option)
        : [...prevSelected, option]
    );
  };

  const handleSendErrors = () => {
    console.log('Errors selected:', selectedErrors);
    setShowErrorSection(false);
  };

  const handleCloseWithErrors = () => {
    handleSendErrors();
    closeModal();
  };

  return (
    <Modal
      show={isModalOpen}
      dialogClassName="modal-lg"
      aria-labelledby="model-tomography-modal"
    >
      <Modal.Header closeButton onClick={(e: React.SyntheticEvent) => { e.stopPropagation(); closeModal(); }}>
        <Modal.Title id="model-tomography-modal">Detalles de la tomografia</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="container">
          <div className="row">
            <div className="col-md-6 border p-3 bg-white">
              <img
                src={tomography.images[0]}
                alt="Tomografía"
                className="img-fluid"
              />
            </div>

            <div className="col-md-6">
              <div className="border p-3 mb-4 bg-white">
                <h3>Composición</h3>
                <p> "No data available"</p>
                <h3>Tamaño</h3>
                <p>"No data available"</p>
                <h3>Diagnóstico</h3>
                <p> "No data available"</p>
              </div>

              <div className="border p-3 bg-white">
                <h3>¿Este informe es Correcto?</h3>
                <Button variant="outline-primary" onClick={handleYesClick}>
                  Sí
                </Button>
                <Button variant="outline-danger" onClick={handleNoClick}>
                  No
                </Button>
              </div>

              {showErrorSection && (
                <div className="border p-3 bg-white mt-3">
                  <h3>Por favor indique la/s sección/es con errores:</h3>
                  <div className="btn-group">
                    <Button
                      variant="outline-dark"
                      className={selectedErrors.includes('Composición') ? 'active' : ''}
                      onClick={() => handleErrorOptionChange('Composición')}
                    >
                      Composición
                    </Button>
                    <Button
                      variant="outline-dark"
                      className={selectedErrors.includes('Tamaño') ? 'active' : ''}
                      onClick={() => handleErrorOptionChange('Tamaño')}
                    >
                      Tamaño
                    </Button>
                    <Button
                      variant="outline-dark"
                      className={selectedErrors.includes('Diagnóstico') ? 'active' : ''}
                      onClick={() => handleErrorOptionChange('Diagnóstico')}
                    >
                      Diagnóstico
                    </Button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </Modal.Body>
      <Modal.Footer>
      <Button
          variant="success"
          className="mt-3"
          onClick={(e: React.MouseEvent<HTMLElement>) => {  // Changed to HTMLElement
            e.stopPropagation();
            handleCloseWithErrors();
          }}
        >
          Enviar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModelTomography;
