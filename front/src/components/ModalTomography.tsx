import React, { useEffect, useState } from 'react';
import { Modal, Button, Form, Card } from 'react-bootstrap';
import { Tomography } from '../interfaces/Tomography';
import { instance } from '../services/BaseClient';
import TextReport from './TextReport';
import { FeedbackModal } from './FeedbackModal';
import { UseFeedback } from "../hooks/UseFeedback";

import { OrdersContext } from '../../src/context/OrdersContext';

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
  const [show, setShow] = useState(false);
  const [reportContent, setReportContent] = useState<string>('');
  const [isAnswerYesSelected, setIsAnswerYesSelected] = useState<boolean>(false); 
  const [isAnswerNoSelected, setIsAnswerNoSelected] = useState<boolean>(false); 

  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const images = tomography?.images ?? [];

  const { handleSendFeedback } = useContext(OrdersContext);

  const handleAcceptModal = async () => {
    const { handleSendFeedback } = UseFeedback();

    if ((isAnswerYesSelected || (isAnswerNoSelected && selectedErrors.length > 0) || (isAnswerNoSelected && feedback.length > 0))) {
      try {
        await handleSendFeedback({
          codeReport: tomography?.codeReport || '',
          isRight: isAnswerYesSelected || isAnswerNoSelected,
          sectionError: selectedErrors.join(", "),
          feedback:feedback
        });

      } catch (error) {
        console.error("Error al enviar el Feedback:", error);
      }
    }
    setShow(false);
  };

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
    setFeedback('');
    setSelectedErrors([]);
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

  const nextImage = () => {
    if (currentImageIndex < images.length - 1) {
      setCurrentImageIndex(currentImageIndex + 1);
    }
  };

  const prevImage = () => {
    if (currentImageIndex > 0) {
      setCurrentImageIndex(currentImageIndex - 1);
    }
  };

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
                  style={{ width: '100%' }}
                />
              ) : (
                <p>No image available</p>
              )}
            </div>

            <div className="col-md-6">
              <div className="border p-3 mb-4 bg-white">
                <h3 style={{color: 'var(--primary-color-1)'}}>Clasificacion de la tomografia</h3>
                <p>{images.length > 0 ? images[0].tomographyCategory : "Dato no disponible"}</p>
                <h3 style={{color: 'var(--primary-color-1)'}}>Estado del informe</h3>
                <p>{tomography?.statusReport != null ? tomography.statusReport : "Dato no disponible"}</p>
                <h3 style={{color: 'var(--primary-color-1)'}}>Codigo del reporte</h3>
                <p>{tomography?.codeReport != null ? tomography.codeReport : "Dato no disponible"}</p>
                <h3 style={{color: 'var(--primary-color-1)'}}>Documento del paciente</h3>
                <p>{tomography?.patient != null ? tomography.patient.document : "Dato no disponible"}</p>
                <h3 style={{color: 'var(--primary-color-1)'}}>Numero de historia clinica del paciente</h3>
                <p>{tomography?.patient != null ? tomography.patient.clinicHistory : "Dato no disponible"}</p>
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
                  <h4 className="text-center" style={{ color: 'var(--primary-color-1)' }}>Informe</h4>
                  {reportContent ? <TextReport report={reportContent} /> : "Cargando informe..."}
                </Card.Body>
              </Card>
            )}
            <br/>
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
                    // handleCloseWithErrors();
                    setShow(true);
                  }}
                  disabled={!(isAnswerYesSelected || (isAnswerNoSelected && selectedErrors.length > 0) || (isAnswerNoSelected && feedback.length > 0))}
                >
                  Enviar
                </Button>
              </div>
            )}
          </div>
        </div>
        <FeedbackModal
          show={show}
          handleClose={() => setShow(false)}
          handleFunc={handleAcceptModal}
          feedback={feedback}
          codeReport={tomography!?.codeReport}
          userId={tomography!?.userId}
          isAnswerYesSelected={isAnswerYesSelected}
          isAnswerNoSelected={isAnswerNoSelected}
          selectedErrors={selectedErrors}
        />
      </Modal.Body>
      <Modal.Footer />
    </Modal>
  );
};

export default ModelTomography;
