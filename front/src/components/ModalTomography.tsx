import React, { useEffect, useState } from 'react';
import { Modal, Button, Form, Card } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faChevronRight, faPlusSquare, faMinusSquare } from '@fortawesome/free-solid-svg-icons';
import { Tomography } from '../interfaces/Tomography';
import { instance } from '../services/BaseClient';
import TextReport from './TextReport';
import { FeedbackModal } from './FeedbackModal';
import { UseFeedback } from "../hooks/UseFeedback";

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
  const [isEditable, setIsEditable] = useState(false); // New state for edit mode

  const [currentImageIndex, setCurrentImageIndex] = useState(0);  // Current image index
  const images = tomography?.images ?? [];  // List of images

  const [classification, setClassification] = useState<string>(images.length > 0 ? images[currentImageIndex].tomographyCategory : "Dato no disponible");
  const [patientDocument, setPatientDocument] = useState<string>(tomography?.patient?.document || "Dato no disponible");
  const [clinicHistory, setClinicHistory] = useState<string>(tomography?.patient?.clinicHistory || "Dato no disponible");

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

  const toggleEditMode = () => {
    setIsEditable(!isEditable);
    
    console.log('Toggle Edit Mode:', {
      isEditable: !isEditable,
      classification: classification,
      patientDocument: patientDocument,
      clinicHistory: clinicHistory,
    });
  };

  const saveChanges = async () => {
    console.log("entre a guardar")
    if (images.length > 0 && currentImageIndex !== null) {
      images[currentImageIndex].tomographyCategory = classification;
    }
    
    if (tomography?.patient) {
      tomography.patient.document = patientDocument;
      tomography.patient.clinicHistory = clinicHistory;
    }
  
    const form = new FormData();
    
    form.append('classification', classification);
    form.append('patientDocument', patientDocument);
    form.append('clinicHistory', clinicHistory);
  
    try {
      // const response = await instance.post(`tomographies/${tomography?.codeReport}`, form);
      // console.log('Response from server:', response.data);
      closeModal();
      //window.location.reload();
    } catch (error) {
      console.error('Error while posting data:', error);
    }
  };
  
  const handleSendErrors = () => {
    console.log('Errors selected:', selectedErrors);
    setShowErrorSection(false);
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
                  src={images[currentImageIndex].url} 
                  alt="Tomografía"
                  className="img-fluid"
                  style={{ width: '100%' }}
                />
              ) : (
                <p>No image available</p>
              )}
              <div className="text-center mt-3">
                <Button onClick={prevImage} disabled={currentImageIndex === 0}>
                <FontAwesomeIcon icon={faChevronLeft} style={{ marginRight: '5px' }}/>
                  Anterior
                </Button>
                <Button onClick={nextImage} disabled={currentImageIndex === images.length - 1} className="ms-2">
                  Siguiente
                  <FontAwesomeIcon icon={faChevronRight} style={{ marginLeft: '5px' }}/>
                </Button>
              </div>
            </div>

            <div className="col-md-6">
  <div className="border p-3 mb-4 bg-white">
  <h3 style={{color: 'var(--primary-color-1)'}}>Clasificación de la tomografía</h3>
  <p>{images.length > 0 ? images[currentImageIndex].tomographyCategory : "Dato no disponible"}</p>

    <h3 style={{ color: 'var(--primary-color-1)' }}>Estado del informe</h3>
    <span className={`badge ${tomography?.statusReport === 'INFORME_GENERADO' ? 'text-bg-success' : 'text-bg-danger'}`}>
      {tomography?.statusReport ? (tomography.statusReport).replace("_", " ") : "Dato no disponible"}
    </span>

    <h3 style={{ color: 'var(--primary-color-1)' }}>Código del reporte</h3>
    <p>{tomography?.codeReport ? tomography.codeReport : "Dato no disponible"}</p>

<h3 style={{ color: 'var(--primary-color-1)' }}>Documento del paciente</h3>
<input
  type="text"
  value={patientDocument}
  onChange={(e) => setPatientDocument(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }}
/>

<h3 style={{ color: 'var(--primary-color-1)' }}>Número de historia clínica del paciente</h3>
<input
  type="text"
  value={clinicHistory}
  onChange={(e) => setClinicHistory(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }} 
/>
              </div>
              <Button variant="outline-primary" onClick={isEditable ? saveChanges : toggleEditMode}>
  <             FontAwesomeIcon icon={faPlusSquare} /> {isEditable ? "Guardar Cambios" : "Editar"}
              </Button>
            </div>
          </div>

          <div className="mt-4">
            <Button className="btn btn-primary" type="button" onClick={() => setShowReport(!showReport)}>
            {showReport ? (
        <>
          <FontAwesomeIcon icon={faMinusSquare} /> Ocultar informe
        </>
      ) : (
        <>
          <FontAwesomeIcon icon={faPlusSquare} /> Ver informe
        </>
      )}
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
