import { useContext, useEffect, useState } from 'react';
import { OrdersContext } from '../../context/OrdersContext';
import { InputNameModal } from '../../components/InputNameModal';
import ImageUploader from '../../components/ImageUploader';

export const Orders = () => {
    const { handleSaveTomography } = useContext(OrdersContext);
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<string | ArrayBuffer | null>(null);
    const [imageUploaderRef, setImageUploaderRef] = useState<(() => void) | null>(null); // Estado para almacenar la referencia a handleDelete

    const handleAcceptModal = async (title: string, patientName: string) => {
        if (uploadedImage) {
            const tomography = new Blob([uploadedImage], { type: 'image/jpeg' });
            await handleSaveTomography({ title, patientName, tomography });
            setUploadedImage(null); // Limpia la imagen del estado después de guardarla
            if (imageUploaderRef) {
                imageUploaderRef(); // Llama a handleDelete de ImageUploader para limpiar el input
            }
        }

        setShow(false);
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Solicitar un Informe</h1>
            
            <ImageUploader 
                setUploadedImage={setUploadedImage} 
                handleDelete={(deleteFunc) => setImageUploaderRef(() => deleteFunc)} // Guarda la referencia de handleDelete
            />

            <button className="btn btn-success my-3"
                onClick={() => setShow(true)}
                disabled={!uploadedImage}
            >
                Solicitar informe
            </button>

            <InputNameModal 
                show={show} 
                handleClose={() => setShow(false)} 
                handleFunc={handleAcceptModal} 
            />
        </div>
    );
};
