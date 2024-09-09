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
            console.log("Image String before encoding:", tomography.images[0]); 
            const encodedImageUrl = encodeURI(tomography.images[0]);
            console.log("Encoded Image URL:", encodedImageUrl);
            setImageUrl(encodedImageUrl);
        } else {
            setImageUrl(null);
        }
    }, [tomography]);

    const openModal = () => {
        setIsModalOpen(true);
        console.log("Modal opened");
    };

    const closeModal = () => {
        setIsModalOpen(false);
        console.log("Modal closed");
    };

    return (
        <div className="card m-3" style={{ cursor: "pointer" }} onClick={(e) => {
            if (!isModalOpen) {
                e.stopPropagation(); // Evita que el click se propague cuando el modal está cerrado
                openModal();
            }
        }}
    >
        <div className="card-body">
            <h5 className="card-title h4" style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', marginBottom: '10px', color: 'var(--primary-color-1)' }}>
                {tomography.title}
            </h5>
            <p className="card-text" style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', marginBottom: '10px' }}>
                <strong>Código de reporte:</strong> {tomography.codeReport}
            </p>
            <p className="card-text" style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', marginBottom: '10px' }}>
                <strong>Estado de la tomografía:</strong> {tomography.category}
            </p>
            <p className="card-text" style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', marginBottom: '10px' }}>
                <strong>Estado del informe:</strong> {tomography.statusReport}
            </p>
    
            {imageUrl ? (
                <div className="image-container">
                    <img
                        src={imageUrl}
                        alt="Tomografía"
                        className="card-img-bottom"
                    />
                </div>
            ) : (
                <p>No se puede mostrar la imagen</p>
            )}
        </div>
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
