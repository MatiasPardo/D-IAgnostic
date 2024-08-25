import React, { useState, useEffect } from "react";
import { Tomography } from "../interfaces/Tomography";
import ModelTomography from "../components/ModalTomography";

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
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div className="card m-1" style={{ cursor: "pointer" }} onClick={openModal}>
            <div className="card-body">
                <h5 className="card-title">{tomography.title}</h5>
                <p className="card-text"><strong>Código de reporte:</strong> {tomography.codeReport}</p>
                <p className="card-text"><strong>Estado de la tomografía:</strong> {tomography.category}</p>
                <p className="card-text"><strong>Estado del informe:</strong> {tomography.statusReport}</p>

                {imageUrl ? (
                    <img
                        src={imageUrl}
                        alt="Tomografía"
                        className="card-img-top"
                        style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                    />
                ) : (
                    <p>No se puede mostrar la imagen</p>
                )}
            </div>

            <ModelTomography
                isModalOpen={isModalOpen}
                closeModal={closeModal}
                tomography={tomography}
            />
        </div>
    );
};
