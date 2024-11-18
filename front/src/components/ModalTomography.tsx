import React, { useEffect, useState } from 'react';
import { Modal, Button, Form, Card, Spinner } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faChevronRight, faPlusSquare, faMinusSquare, faEdit, faSave } from '@fortawesome/free-solid-svg-icons';
import { Tomography } from '../interfaces/Tomography';
import { instance } from '../services/BaseClient';
import TextReport from './TextReport';
import { FeedbackModal } from '../components/FeedbackModal';
import { UseFeedback } from "../hooks/UseFeedback";
import Swal from 'sweetalert2';

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
  const [isEditable, setIsEditable] = useState(false); 

  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const images = tomography?.images ?? [];  

  const mostrarFechaBien = (fechaString: string): string => {
    if (!fechaString) return "Fecha no disponible";
    
    const [year, month, day] = fechaString.split(/[-,]/).map(Number);
    const date = new Date(year, month - 1, day);
    
    const dayFormatted = date.getDate().toString().padStart(2, '0');
    const monthFormatted = (date.getMonth() + 1).toString().padStart(2, '0');
    
    return `${dayFormatted}-${monthFormatted}-${date.getFullYear()}`;
  };
  
  
  const initialDate = mostrarFechaBien(String(tomography?.patient?.birthdate ?? ""));

  const [classification, setClassification] = useState<string>(images.length > 0 ? images[currentImageIndex].tomographyCategory : "Dato no disponible");
  const [patientDocument, setPatientDocument] = useState<string>(tomography?.patient?.document || "Dato no disponible");
  const [namePatient, setNamePatient] = useState<string>(tomography?.patient?.name || "Dato no disponible");
  const [lastNamePatient, setLastNamePatient] = useState<string>(tomography?.patient?.lastName || "Dato no disponible");
  const [clinicHistory, setClinicHistory] = useState<string>(tomography?.patient?.clinicHistory || "Dato no disponible");
  const [emailPatient, setEmailPatient] = useState<string>(tomography?.patient?.email || "Dato no disponible");
  const [patientDetails, setPatientDetails] = useState<string>(tomography?.patient?.detail || "Dato no disponible");
  const [birthdatePatient, setBirthdatePatient] = useState<string>( initialDate  || "Dato no disponible")
  const [hospital, sethospital] = useState<string>(tomography?.patient?.hospital || "Dato no disponible");
  const [patientTypeDocument, setTypeDocument] = useState<string>(tomography?.patient?.typeDocument || "Dato no disponible");

const [isLoading, setIsLoading] = useState(false);

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
  };
  const mostrarFechaBienConHora = (fechaString: string): string => {
    if (!fechaString || fechaString === "null" || fechaString === "undefined") {
      return "Fecha no disponible";
    }
  
    const [day, month, year] = fechaString.split('-').map(Number);
  
    const adjustedMonth = month - 1; 
  
    const date = new Date(Date.UTC(year, adjustedMonth, day)); 
  
    if (isNaN(date.getTime())) {
      return "Fecha no válida"; 
    }
  
    const formattedDate = date.toISOString(); 
  
    return formattedDate; 
  };
  
  

  const saveChanges = async () => {
    setIsLoading(true);
  
    if (images.length > 0 && currentImageIndex !== null) {
      images[currentImageIndex].tomographyCategory = classification;
    }
  
    if (tomography?.patient) {
      tomography.patient.document = patientDocument;
      tomography.patient.clinicHistory = clinicHistory;
      tomography.patient.birthdate = birthdatePatient;
      tomography.patient.name = namePatient;
      tomography.patient.lastName = lastNamePatient;
      tomography.patient.email = emailPatient;
      tomography.patient.hospital = hospital;
      tomography.patient.typeDocument = patientTypeDocument;
      tomography.patient.detail = patientDetails;
    }
  
    const encodeValue = (value: string | undefined) => {
      if (value === "Dato no disponible") {
        return "";
      }
      return encodeURIComponent(value ?? ""); 
    };
  
    const isValidDate = (dateStr: string) => {
      const date = new Date(dateStr);
      return !isNaN(date.getTime()); 
    };
    
    const initialDateNew = mostrarFechaBienConHora(String(birthdatePatient ?? ""));

    const queryParams = new URLSearchParams({
      document: (patientDocument),
      typeDocument: (patientTypeDocument),
      clinicHistory: (clinicHistory),
      birthdate: (initialDateNew), 
      name: (namePatient),
      lastName: (lastNamePatient),
      email: (emailPatient),
      hospital: (hospital),
      detail: (patientDetails),
    }).toString();
  
    try {
      const response = await instance.patch(
        `tomographies/${tomography?.codeReport}?${queryParams}`
      );
  
  
      setTimeout(() => {
        toggleEditMode();
      }, 500);
    }catch (error: any) {
      const AlertError = (title: string, message: string) => {
        Swal.fire({
          icon: 'error',
          title: title,
          text: message,
          confirmButtonText: 'OK',
        });
      };
  
      AlertError('Error al guardar cambios', error?.message || 'Hubo un problema al actualizar los datos.');
    } finally {
      setIsLoading(false);
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
            <h3 style={{ color: 'var(--primary-color-1)' }}>Datos del paciente</h3>
  <div className="border p-3 mb-4 bg-white">
<h3 style={{ color: 'var(--primary-color-1)' }}>Numero del documento</h3>
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
<h3 style={{ color: 'var(--primary-color-1)' }}>Tipo de documento</h3>
<input
  type="text"
  value={patientTypeDocument}
  onChange={(e) => setTypeDocument(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }}
/>
<h3 style={{ color: 'var(--primary-color-1)' }}>Número de historia clínica</h3>
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
/><h3 style={{ color: 'var(--primary-color-1)' }}>Nombre</h3>
<input
  type="text"
  value={namePatient}
  onChange={(e) => setNamePatient(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }} 
/>
<h3 style={{ color: 'var(--primary-color-1)' }}>Apellido</h3>
<input
  type="text"
  value={lastNamePatient}
  onChange={(e) => setLastNamePatient(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }} 
/>
<h3 style={{ color: 'var(--primary-color-1)' }}>Email</h3>
<input
  type="text"
  value={emailPatient}
  onChange={(e) => setEmailPatient(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }}
/>
<h3 style={{ color: 'var(--primary-color-1)' }}>Fecha de nacimiento</h3>
<input
  type="text" 
  value={birthdatePatient}
  onChange={(e) => {
    setBirthdatePatient(e.target.value);
  }}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }}
/>
<h3 style={{ color: 'var(--primary-color-1)' }}>Detalles</h3>
<input
  type="text"
  value={patientDetails}
  onChange={(e) => setPatientDetails(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }}
/>
<h3 style={{ color: 'var(--primary-color-1)' }}>Hospital</h3>
<input
  type="text"
  value={hospital}
  onChange={(e) => sethospital(e.target.value)}
  readOnly={!isEditable}
  className={`${
    isEditable 
      ? 'form-control'  
      : 'border-0 bg-transparent'
  }`}
  style={{ color: 'black' }}
/>
              </div>
              <Button
  variant="outline-primary"
  onClick={isEditable ? saveChanges : toggleEditMode}
  disabled={isLoading} 
>
  {isLoading ? (
    <>
      <Spinner
        as="span"
        animation="border"
        size="sm"
        role="status"
        aria-hidden="true"
      />{" "}
      Guardando...
    </>
  ) : (
    <>
      <FontAwesomeIcon icon={isEditable ? faSave : faEdit} />{" "}
      {isEditable ? "Guardar Cambios" : "Editar"}
    </>
  )}
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
function setIsLoading(arg0: boolean) {
  throw new Error('Function not implemented.');
}
