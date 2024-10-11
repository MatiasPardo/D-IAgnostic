import React, { useState, useEffect } from "react";
import { Tomography } from "../interfaces/Tomography";
import ModelTomography from "../components/ModalTomography";
import "./TomographyCard.css";

interface TomographyCardProps {
    tomography: Tomography;
}

export const TomographyCard: React.FC<TomographyCardProps> = ({ tomography }) => {
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    useEffect(() => {
        if (tomography && tomography.images && tomography.images.length > 0) {
            const encodedImageUrl = encodeURI(tomography.images[0].url);
            setImageUrl(encodedImageUrl);
        } else {
            setImageUrl(null);
        }
    }, [tomography]);

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    const [showTooltip, setShowTooltip] = useState(false);

    return (
        <div className="card card-tom m-3" style={{ cursor: "pointer" }} onClick={(e) => {
            if (!isModalOpen) {
                e.stopPropagation(); // Evita que el click se propague cuando el modal está cerrado
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
              marginBottom: '10px',
              color: 'var(--primary-color-1)',
              cursor: 'pointer',
            }}
          >
            {tomography.title.length > 20 ? `${tomography.title.substring(0, 20)}...` : tomography.title}
            {showTooltip && (
              <span className="tooltiptext">{tomography.title}</span>
            )}
          </h5>            <hr />
            <p className="card-text"><strong>Numero de Documento:</strong> {tomography?.patient?.document ? tomography.patient.document : '-'}</p>
            <p className="card-text"><strong>Historia Clinica:</strong> {tomography?.patient?.clinicHistory ? tomography.patient.clinicHistory : '-'}</p>
            <p className="card-text"><strong>Código de reporte:</strong> {tomography?.codeReport ? tomography.codeReport : '-'}</p>
            <p className="card-text"><strong>Estado del informe: </strong>
                <span className={`badge ${tomography.statusReport === 'INFORME_GENERADO' ? 'text-bg-success' : 'text-bg-danger'}`}>
                    {tomography.statusReport.replace("_", " ")}
                </span>
            </p>
            <p className="card-text"><strong>Fecha de creación:</strong> {tomography.createdDate}</p>

            {imageUrl ? (
                <div className="image-container image-container-tom">
                    <img src={imageUrl} alt="Tomografía" className="card-img-bottom" />
                </div>
            ) : (
                <p>No se puede mostrar la imagen</p>
            )}
        </div>
        {/* falta hacer handleDelete */}
        <button className="btn btn-danger delete-button" onClick={closeModal}> 
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

    {isModalOpen && (
        <ModelTomography
            isModalOpen={isModalOpen}
            closeModal={closeModal}
            tomography={tomography}
        />
    )}
    </div>
    );
};
