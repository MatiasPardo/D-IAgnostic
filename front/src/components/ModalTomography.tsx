import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { Tomography } from '../interfaces/Tomography';

interface ModelTomographyProps {
  isModalOpen: boolean;
  closeModal: () => void;
  tomography: Tomography | null; // Updated to handle possible null
}

const ModelTomography: React.FC<ModelTomographyProps> = ({ isModalOpen, closeModal, tomography }) => {
  const [showErrorSection, setShowErrorSection] = useState(false);
  const [selectedErrors, setSelectedErrors] = useState<string[]>([]);
  const [feedback, setFeedback] = useState<string>('');

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

  const images = tomography?.images ?? []; // Default to empty array if null

  return (
    <Modal
      show={isModalOpen}
      onHide={closeModal}
      dialogClassName="modal-lg"
      aria-labelledby="model-tomography-modal"
    >
      <Modal.Header closeButton onClick={(e: React.SyntheticEvent) => { e.stopPropagation(); closeModal(); }}>
        <Modal.Title id="model-tomography-modal">Detalles de la tomografía</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="container">
          <div className="row">
            <div className="col-md-6 border p-3 bg-white">
              {images.length > 0 ? (
                <img
                  src={images[0]}
                  alt="Tomografía"
                  className="img-fluid"
                />
              ) : (
                <p>No image available</p>
              )}
            </div>

            <div className="col-md-6">
              <div className="border p-3 mb-4 bg-white">
                <h3>Composición</h3>
                <p> "Dato no disponible"</p>
                <h3>Tamaño</h3>
                <p> "Dato no disponible"</p>
                <h3>Diagnóstico</h3>
                <p> "Dato no disponible"</p>
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
                  <Form.Group className="mt-3">
                    <Form.Label>Feedback Informe</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={3}
                      value={feedback}
                      onChange={(e) => setFeedback(e.target.value)}
                      placeholder="Escriba su feedback aquí..."
                    />
                  </Form.Group>
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
          onClick={(e: React.MouseEvent<HTMLElement>) => {
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
