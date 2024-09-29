import React, { useContext, useEffect, useState } from 'react';
import { OrdersContext } from '../../context/OrdersContext';
import { InputNameModal } from '../../components/InputNameModal';
import { ImageUploader } from '../../components/ImageUploader';
import { findTomographies } from '../../services/TomographiesService';
import './orders.css';

export const Orders = () => {
    const { handleSaveTomography } = useContext(OrdersContext);
    const [step, setStep] = useState(1); 
    const [show, setShow] = useState(false);
    const [uploadedImages, setUploadedImages] = useState<Blob[]>([]);
    const [imageUploaderRef, setImageUploaderRef] = useState<(() => void) | null>(null);
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    const handleAcceptModal = async (title: string, patientName: string, clinicalHistory: string,
        dni: string, birthDate: string
    ) => {
        if (uploadedImages.length > 0) { 
            try {
                const firstResponse = await handleSaveTomography({
                    title,
                    patientName,
                    clinicalHistory,
                    dni,
                    birthDate,
                    tomography: uploadedImages[0],
                    lastImage: uploadedImages.length === 1
                });
    
                const codeReport = firstResponse.data.codeReport;
                for (let i = 1; i < uploadedImages.length; i++) {
                    await handleSaveTomography({
                        codeReport, 
                        title,
                        patientName,
                        clinicalHistory,
                        dni,
                        birthDate,
                        tomography: uploadedImages[i],
                        lastImage: i === uploadedImages.length - 1
                    });
                }
                
                setUploadedImages([]);
                if (imageUploaderRef) {
                    imageUploaderRef(); 
                }
            } catch (error) {
                console.error("Error al guardar la tomografía:", error);
            }
        }
        setShow(false);
    };
    

    const handleFileUpload = (images: Blob[]) => {
        setUploadedImages(images);
    };

    useEffect(() => {
        const loadImage = async () => {
            try {
                const tomographies = await findTomographies(1, 1);
                if (tomographies.length > 0) {
                    const selectedTomography = tomographies[0];
                    const url = ''; 
                    setImageUrl(url);
                }
            } catch (error) {
                console.error("Error al cargar las tomografías:", error);
            }
        };

        loadImage(); 
        return () => {
            if (imageUrl) {
                URL.revokeObjectURL(imageUrl);
            }
        };
    }, [imageUrl]);

    return (
        <div className="container mt-5 bg-doctor">
            <div className="background-overlay"></div>
            <div className="content">
                <div className="progress mb-4">
                    <div 
                        className="progress-bar" 
                        role="progressbar" 
                        style={{ width: `${step === 1 ? 50 : 100}%` }}
                        aria-valuenow={step === 1 ? 50 : 100} 
                        aria-valuemin={0} 
                        aria-valuemax={100}
                    >
                        {step === 1 ? 'Paso 1 de 2' : 'Paso 2 de 2'}
                    </div>
                </div>

                <h1 className="display-1 mb-4">Solicitar un Informe</h1>

                {step === 1 && (
                    <div className="card p-4 content-card">
                        <h2 className="h4 mb-3">1): Subir una imagen o varias de la tomografía</h2>
                        <ImageUploader 
                            setUploadedImages={handleFileUpload} // Update to use the new prop
                            handleDelete={(deleteFunc: any) => setImageUploaderRef(() => deleteFunc)} 
                        />

                        {uploadedImages.length > 0 && (
                            <div className="my-3">
                                <h3>Imágenes seleccionadas:</h3>
                                {uploadedImages.map((img, index) => (
                                    <img
                                        key={index}
                                        src={URL.createObjectURL(img)}
                                        alt={`Tomografía ${index + 1}`}
                                        className="img-fluid my-2"
                                    />
                                ))}
                            </div>
                        )}

                        <button 
                            className="btn btn-sm mt-3" 
                            style={{ margin: '0px 50% 0px 0px', backgroundColor: 'var(--primary-color-1)', color: 'var(--text-color-2)'}}
                            disabled={uploadedImages.length === 0} // Update condition
                            onClick={() => setStep(2)}
                        >
                            Continuar al siguiente paso
                        </button>
                    </div>
                )}

                {step === 2 && (
                    <div className="card p-4 content-card">
                        <h2 className="h4 mb-3">2): Completar los datos del nombre del paciente</h2>
                        <div className="d-flex justify-content-between">
                            <button 
                                className="btn btn-secondary my-3"
                                onClick={() => setStep(1)}
                            >
                                Volver al paso anterior
                            </button>
                            <button 
                                className="btn my-3"
                                style={{ backgroundColor: 'var(--primary-color-1)', color: 'var(--text-color-2)'}}
                                onClick={() => setShow(true)}
                                disabled={uploadedImages.length === 0} // Update condition
                            >
                                Completar Datos y Solicitar Informe
                            </button>
                        </div>
                    </div>
                )}
                
                <InputNameModal 
                    show={show} 
                    handleClose={() => setShow(false)} 
                    handleFunc={handleAcceptModal} 
                />
            </div>
        </div>
    );
};
