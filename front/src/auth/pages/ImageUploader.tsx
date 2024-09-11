import React, { useRef } from 'react';

export const ImageUploader = ({ setUploadedImage, handleDelete }: { setUploadedImage: (image: Blob | null) => void, handleDelete: (deleteFunc: any) => void }) => {
    const fileInputRef = useRef<HTMLInputElement | null>(null);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (file) {
            setUploadedImage(file);
        }
    };

    const handleDeleteClick = () => {
        setUploadedImage(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = ''; // Ensure this is only called if the ref is not null
        }
    };

    // Pass the delete function to the parent component
    handleDelete(handleDeleteClick);

    return (
        <div>
            <input
                type="file"
                accept=".dicom"
                ref={fileInputRef}
                onChange={handleFileChange}
                className="form-control"
            />
            <button onClick={handleDeleteClick} className="btn btn-danger mt-2">
                Eliminar Imagen
            </button>
        </div>
    );
};
