import React, { useState, useEffect } from "react";
import { Tomography } from "../interfaces/Tomography";

interface TomographyCardProps {
    tomography: Tomography;
}

export const TomographyCard: React.FC<TomographyCardProps> = ({ tomography }) => {
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    useEffect(() => {
        if (tomography.tomography && tomography.tomography.size > 0) {
            const blob = new Blob([tomography.tomography], { type: "image/jpeg" });
            const url = URL.createObjectURL(blob);
            setImageUrl(url);

            return () => {
                URL.revokeObjectURL(url);
            };
        }
    }, [tomography]);

    return (
        <div className="card m-2">
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
        </div>
    );
};
