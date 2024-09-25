import React, { useEffect, useState } from 'react';
import { Modal, Button, Form, Card } from 'react-bootstrap';
import { Tomography } from '../interfaces/Tomography';
import { instance } from '../services/BaseClient';
import TextReport from './TextReport';

interface ModelTomographyProps {
  isModalOpen: boolean;
  closeModal: () => void;
  tomography: Tomography | null;
}

const ModelTomography: React.FC<ModelTomographyProps> = ({ isModalOpen, closeModal, tomography }) => {
  const [showErrorSection, setShowErrorSection] = useState(false);
  const [selectedErrors, setSelectedErrors] = useState<string[]>([]);
  const [feedback, setFeedback] = useState<string>('');
  const [showReport, setShowReport] = useState(false);
  const [reportContent, setReportContent] = useState<string>(''); 
  const [isAnswerYesSelected, setIsAnswerYesSelected] = useState<boolean>(false); 
  const [isAnswerNoSelected, setIsAnswerNoSelected] = useState<boolean>(false); 

  useEffect(() => {
    if (showReport && tomography?.codeReport) {
      const fetchReport = async () => {
        try {
          const response = await instance.get(`tomographies/report/${tomography.codeReport}`);
          setReportContent(response.data.report); 
        } catch (error) {
          console.error('Error al cargar el reporte:', error);
        }
      };

      fetchReport();
    }
  }, [showReport, tomography?.codeReport]);

  const handleNoClick = () => {
    setShowErrorSection(true);
    setIsAnswerNoSelected(true);
    setIsAnswerYesSelected(false);
  };

  const handleYesClick = () => {
    setShowErrorSection(false);
    setIsAnswerYesSelected(true);
    setIsAnswerNoSelected(false);
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

  const images = tomography?.images ?? [];

  return (
    <Modal
      show={isModalOpen}
      onHide={closeModal}
      dialogClassName="modal-lg"
      aria-labelledby="model-tomography-modal"
    >
      <Modal.Header closeButton>
        <Modal.Title id="model-tomography-modal">{tomography?.title || 'Detalle de la Tomografía'}</Modal.Title>
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
                <h3 style={{color: 'var(--primary-color-1)'}}>Composición</h3>
                <p> "Dato no disponible"</p>
                <h3 style={{color: 'var(--primary-color-1)'}}>Tamaño</h3>
                <p> "Dato no disponible"</p>
                <h3 style={{color: 'var(--primary-color-1)'}}>Diagnóstico</h3>
                <p> "Dato no disponible"</p>
              </div>
            </div>
          </div>
          <div className="mt-4">
            <Button className="btn btn-primary" type="button" onClick={() => setShowReport(!showReport)}>
              {showReport ? 'Ocultar informe' : 'Ver informe +'}
            </Button>
            {showReport && (
              <Card className="mt-3">
                <Card.Body>
                  <h4 className="text-center" style={{color: 'var(--primary-color-1)'}}>Informe</h4>
                    {reportContent ? <TextReport report={reportContent} /> : "Cargando informe..."}
                </Card.Body>
              </Card>
            )}
            {showReport && (
              <div className="border p-3 bg-white">
                <h3>¿Este informe es Correcto?</h3>
                <Button variant="outline-primary" onClick={handleYesClick}>
                  Sí
                </Button>
                <Button variant="outline-danger" onClick={handleNoClick} className="ms-2">
                  No
                </Button>
              </div>
            )}
            <br/>
            {showReport && showErrorSection && (
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
            {showReport && (
              <div className="text-end">
                <Button
                  variant="success"
                  className="mt-3"
                  onClick={(e: React.MouseEvent<HTMLElement>) => {
                    e.stopPropagation();
                    handleCloseWithErrors();
                  }}
                  disabled={!(isAnswerYesSelected || (isAnswerNoSelected && selectedErrors.length > 0) || (isAnswerNoSelected && feedback.length > 0))}
                >
                  Enviar
                </Button>
              </div>
            )}
          </div>
        </div>
      </Modal.Body>
      <Modal.Footer />
    </Modal>
  );
};

export default ModelTomography;
