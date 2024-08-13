import React from "react";
import { Tomography } from "../interfaces/Tomography";

interface TomographyCardProps {
    tomography: Tomography;
}

export const TomographyCard: React.FC<TomographyCardProps> = ({ tomography }) => {
    const byteArray = new Uint8Array(tomography.tomography);
    const blob = new Blob([byteArray], { type: "image/jpeg" });
    const imageUrl = URL.createObjectURL(blob);

    return (
        <div className="card m-2">
            <div className="card-body">
            <h5 className="card-title">{tomography.title}</h5>
                <p className="card-text">ID: {tomography.codeReport}</p>
                {tomography.tomography && (
                    <img src={`data:image/jpeg;base64,${tomography.tomography}`} alt="Tomografía" className="card-img-top" />
                )}
            </div>
        </div>
    );
};
