import React, { useContext, useEffect, useState } from 'react';
import { OrdersContext } from '../../context/OrdersContext';
import { InputNameModal } from '../../components/InputNameModal';
import { ImageUploader } from '../../components/ImageUploader';
import { findTomographies } from '../../services/TomographiesService';
import './orders.css'

export const Orders = () => {
    const { handleSaveTomography } = useContext(OrdersContext);
    const [step, setStep] = useState(1); 
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<Blob | null>(null);
    const [imageUploaderRef, setImageUploaderRef] = useState<(() => void) | null>(null);
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    const handleAcceptModal = async (title: string, patientName: string) => {
        if (uploadedImage) {
            try {
                await handleSaveTomography({ title, patientName, tomography: uploadedImage, lastImage: true });
                setUploadedImage(null);
                if (imageUploaderRef) {
                    imageUploaderRef(); 
                }
            } catch (error) {
                console.error("Error al guardar la tomografía:", error);
            }
        }
        setShow(false);
    };

    const handleFileUpload = (image: Blob | null) => {
        setUploadedImage(image);
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
    <div className="background-overlay"></div> {/* Capa de fondo separada */}
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
                <h2 className="h4 mb-3">1): Subir la imagen de la tomografía</h2>
                <ImageUploader 
                    setUploadedImage={handleFileUpload} 
                    handleDelete={(deleteFunc: any) => setImageUploaderRef(() => deleteFunc)} 
                />

                {imageUrl && (
                    <div className="my-3">
                        <h3>Imagen seleccionada:</h3>
                        <img
                            src={imageUrl}
                            alt="Tomografía"
                            className="img-fluid"
                        />
                    </div>
                )}

                <button 
                    className="btn btn-sm mt-3" 
                    style={{ margin: '0px 50% 0px 0px', backgroundColor: 'var(--primary-color-1)', color: 'var(--text-color-2)'}}
                    disabled={!uploadedImage}
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
                        style={{backgroundColor: 'var(--primary-color-1)', color: 'var(--text-color-2)'}}
                        onClick={() => setShow(true)}
                        disabled={!uploadedImage}
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
