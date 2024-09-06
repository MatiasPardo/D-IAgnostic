import { useContext, useEffect, useState } from 'react';
import { OrdersContext } from '../../context/OrdersContext';
import { InputNameModal } from '../../components/InputNameModal';
import { ImageUploader } from '../../components/ImageUploader';
import { findTomographies } from '../../services/TomographiesService';

export const Orders = () => {
    const { handleSaveTomography } = useContext(OrdersContext);
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<Blob | null>(null);
    const [imageUploaderRef, setImageUploaderRef] = useState<(() => void) | null>(null);
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    const handleAcceptModal = async (title: string, patientName: string) => {
        if (uploadedImage) {
            await handleSaveTomography({ title, patientName, tomography: uploadedImage, lastImage: true  });
            setUploadedImage(null);
            if (imageUploaderRef) {
                imageUploaderRef();
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
                    const url = null;
                    setImageUrl(url);
                }
            } catch (error) {
                console.error("Error al cargar las tomografías:", error);
            }
        };


        return () => {
            if (imageUrl) {
                URL.revokeObjectURL(imageUrl);
            }
        };
    }, []);

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Solicitar un Informe</h1>
            
            <ImageUploader 
                setUploadedImage={handleFileUpload} 
                handleDelete={(deleteFunc: any) => setImageUploaderRef(() => deleteFunc)} 
            />

            {imageUrl && (
                <img
                    src={imageUrl}
                    alt="Tomografía"
                    className="img-fluid"
                />
            )}

            <button className="btn btn-success my-3"
                onClick={() => setShow(true)}
                disabled={!uploadedImage}
            >
                Solicitar Informe
            </button>

            <InputNameModal 
                show={show} 
                handleClose={() => setShow(false)} 
                handleFunc={handleAcceptModal} 
            />
        </div>
    );
};
