import React, { useContext, useEffect } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography"; // Asegúrate de importar el tipo correcto

export const Tomographies = () => {
    const { tomographies, getTomographies } = useContext(TomographiesContext);

    useEffect(() => {
        getTomographies();
    }, [getTomographies]);

    return (
        <div className="p-4">
            <h1>Tomografías</h1>
            <hr />
            <div className="row rows-cols-1 row-cols-md-6">
                {tomographies.map((tomo: Tomography) => (
                    <TomographyCard
                        key={tomo.codeReport}
                        tomography={tomo}
                    />
                ))}
            </div>
        </div>
    );
};
