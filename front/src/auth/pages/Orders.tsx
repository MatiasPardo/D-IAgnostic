import { useContext, useEffect, useState } from 'react';
import { OrdersContext } from '../../context/OrdersContext';
import { InputNameModal } from '../../components/InputNameModal';
import ImageUploader from '../../components/ImageUploader';

export const Orders = () => {
    const { handleSaveTomography } = useContext(OrdersContext);
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<string | ArrayBuffer | null>(null); // El archivo ya está seleccionado y almacenado aquí.

    useEffect(() => { 
        const fetch = async () => {
            // await getOrders();  // Si necesitas cargar órdenes, descomenta esta línea.
        };
        fetch();
    }, []);

    const handleAcceptModal = async (title: string) => {
        if (uploadedImage) {
            const tomography = new Blob([uploadedImage], { type: 'image/jpeg' }); 
            await handleSaveTomography({ title, tomography });
        }
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Solicitar un Informe</h1>
            
            <ImageUploader setUploadedImage={setUploadedImage} />

            <button className="btn btn-success my-3"
                onClick={() => setShow(true)}
                disabled={!uploadedImage} // El botón se habilita solo si hay una imagen cargada.
            >
                Solicitar informe
            </button>

            <InputNameModal 
                show={show} 
                handleClose={() => setShow(false)} 
                handleFunc={handleAcceptModal} // La función ahora solo requiere el título.
            />
        </div>
    );
};
