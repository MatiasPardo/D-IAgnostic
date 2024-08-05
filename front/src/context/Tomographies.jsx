import React, { useEffect } from 'react';
import { useTomographies } from '../context/TomographiesContext'; // Asegúrate de importar el hook correcto
import { TomographyCard } from '../components/TomographyCard';

export const Tomographies = () => {
    const { tomographies, getTomographies } = useTomographies();

    useEffect(() => {
        getTomographies();
    }, [getTomographies]);

    return (
        <div className="p-4">
            <h1>Tomografías</h1>
            <hr />
            <div className="row rows-cols-1 row-cols-md-6">
                {tomographies.map((tomo) => (
                    <TomographyCard
                        key={tomo.codeReport}
                        tomography={tomo}
                    />
                ))}
            </div>
        </div>
    );
};
