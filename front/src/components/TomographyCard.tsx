import React, { useState, useEffect } from "react";
import { Tomography } from "../interfaces/Tomography";
import ModelTomography from "../components/ModalTomography";
import DeleteConfirmationModal from "../components/DeleteConfirmationModal";
import { instance } from "../services/BaseClient";
import Swal from "sweetalert2";
import "./TomographyCard.css";

interface TomographyCardProps {
  tomography: Tomography;
}

export const TomographyCard: React.FC<TomographyCardProps> = ({ tomography }) => {
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [isDeleteConfirmationOpen, setIsDeleteConfirmationOpen] = useState<boolean>(false);
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [showTooltip, setShowTooltip] = useState<boolean>(false);
  const images = tomography?.images ?? [];  
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [classification, setClassification] = useState<string>(images.length > 0 ? images[currentImageIndex].tomographyCategory : "Dato no disponible");

  useEffect(() => {
    if (tomography?.images?.length > 0) {
      const encodedImageUrl = encodeURI(tomography.images[0].url);
      setImageUrl(encodedImageUrl);
    } else {
      setImageUrl(null);
    }
  }, [tomography]);

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const openDeleteConfirmation = (e: React.MouseEvent) => {
    e.stopPropagation();
    setIsDeleteConfirmationOpen(true);
  };

  const closeDeleteConfirmation = () => setIsDeleteConfirmationOpen(false);

  const AlertOkRedirect = (title: string, message: string) => {
    Swal.fire({
      icon: 'success',
      title: title,
      text: message,
      confirmButtonText: 'OK',
      timer: 3000,
    }).then(() => {
      window.location.reload(); 
    });
  };

  const AlertError = (title: string, message: string) => {
    Swal.fire({
      icon: 'error',
      title: title,
      text: message,
      confirmButtonText: 'OK',
    });
  };

  const handleDelete = async () => {
    try {
      await deleteTomography(tomography.codeReport);
      setIsDeleteConfirmationOpen(false); 
      AlertOkRedirect('Eliminar', 'Se ha eliminado la tomografía.');
    } catch (error) {
      AlertError('Eliminar', 'No se pudo eliminar la tomografía.');
    }
  };

  const deleteTomography = async (codeReport: string) => {
    try {
    console.log(codeReport)
      const response = await instance.delete(`/tomographies/${codeReport}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  };

  return (
    <div
      className="card card-tom m-3"
      style={{ cursor: "pointer" }}
      onClick={(e) => {
        if (!isModalOpen && !isDeleteConfirmationOpen) {
          e.stopPropagation();
          openModal();
        }
      }}
    >
      <div className="card-body">
        <div className="card-content">
          <h5
            className="card-title h3"
            onMouseEnter={() => setShowTooltip(true)}
            onMouseLeave={() => setShowTooltip(false)}
            style={{
              marginBottom: "10px",
              color: "var(--primary-color-1)",
              cursor: "pointer",
            }}
          >
            {tomography.title.length > 20
              ? `${tomography.title.substring(0, 20)}...`
              : tomography.title}
            {showTooltip && <span className="tooltiptext">{tomography.title}</span>}
          </h5>
          <hr />
          <p className="card-text">
            <strong>Numero de Documento:</strong> {tomography?.patient?.document || "-"}
          </p>
          <p className="card-text">
            <strong>Clasificación de la tomografía:</strong> {images.length > 0 ? images[currentImageIndex].tomographyCategory : "Dato no disponible"}
          </p>
          <p className="card-text">
            <strong>Historia Clinica:</strong> {tomography?.patient?.clinicHistory || "-"}
          </p>
          <p className="card-text">
            <strong>Código de reporte:</strong> {tomography?.codeReport || "-"}
          </p>
          <p className="card-text">
            <strong>Estado del informe: </strong>
            <span
              className={`badge ${
                tomography.statusReport === "INFORME_GENERADO"
                  ? "text-bg-success"
                  : "text-bg-danger"
              }`}
            >
              {tomography.statusReport.replace("_", " ")}
            </span>
          </p>
          <p className="card-text">
            <strong>Fecha de creación:</strong> {tomography.createdDate}
          </p>

          {imageUrl ? (
            <div className="image-container image-container-tom">
              <img src={imageUrl} alt="Tomografía" className="card-img-bottom" />
            </div>
          ) : (
            <p>No se puede mostrar la imagen</p>
          )}
        </div>

        <button
          className="btn btn-danger delete-button"
          onClick={openDeleteConfirmation}
        >
          Eliminar
        </button>
      </div>

      {isModalOpen && (
        <ModelTomography
          isModalOpen={isModalOpen}
          closeModal={closeModal}
          tomography={tomography}
        />
      )}

      <DeleteConfirmationModal
        isOpen={isDeleteConfirmationOpen}
        onConfirm={handleDelete}
        onCancel={closeDeleteConfirmation}
      />
    </div>
  );
};
