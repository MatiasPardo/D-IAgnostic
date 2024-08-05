import React, { useState, useEffect } from 'react';
import { TomographiesContext } from './TomographiesContext';
import { findTomographies } from '../services/TomographiesService';
import { Tomography } from '../interfaces/Tomography';

export const TomographiesProvider = ({ children }) => {
    const [tomographies, setTomographies] = useState<undefined>([]);

    const getTomographies = async () => {
        try {
            const data = await findTomographies();
            setTomographies(data);
        } catch (error) {
            console.error('Error al obtener las tomografías:', error);
        }
    };

    useEffect(() => {
        getTomographies();
    }, []);

    return (
        <TomographiesContext.Provider value={{ tomographies, getTomographies }}>
            {children}
        </TomographiesContext.Provider>
    );
};
