import React, { createContext, useState, useContext, useEffect } from 'react';
import { findTomographies } from '../services/TomographiesService';

// Define el contexto
export const TomographiesContext = createContext();

// Proveedor del contexto
export const TomographiesProvider = ({ children }) => {
    const [tomographies, setTomographies] = useState([]);

    const getTomographies = async () => {
        try {
            const response = await findTomographies();
            setTomographies(response); // Ajuste para el formato de respuesta
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

// Hook personalizado para usar el contexto
export const useTomographies = () => {
    const context = useContext(TomographiesContext);
    if (!context) {
        throw new Error('useTomographies debe ser usado dentro de TomographiesProvider');
    }
    return context;
};
