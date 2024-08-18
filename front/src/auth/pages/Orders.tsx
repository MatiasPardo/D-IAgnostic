import { useContext, useEffect, useState } from 'react';
import { OrdersContext } from '../../context/OrdersContext';
import { InputNameModal } from '../../components/InputNameModal';
import { ImageUploader } from '../../components/ImageUploader';
import { fetchTomographyImage } from '../../services/TomographiesService';

export const Orders = () => {
    const { handleSaveTomography } = useContext(OrdersContext);
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<Blob | null>(null);
    const [imageUploaderRef, setImageUploaderRef] = useState<(() => void) | null>(null);
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    const handleAcceptModal = async (title: string, patientName: string) => {
        if (uploadedImage) {
            await handleSaveTomography({ title, patientName, tomography: uploadedImage });
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
            const imageBlob = await fetchTomographyImage('some-id'); // Replace 'some-id' with the actual ID
            const url = URL.createObjectURL(imageBlob);
            setImageUrl(url);
        };

        loadImage();

        // Clean up the URL object when the component unmounts
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