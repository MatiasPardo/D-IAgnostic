import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';

interface ImageNavigatorProps {
  images: { url: string; tomographyCategory?: string }[];
}

export const ImageNavigator: React.FC<ImageNavigatorProps> = ({ images }) => {
  const [currentIndex, setCurrentIndex] = useState(0);

  const nextImage = () => currentIndex < images.length - 1 && setCurrentIndex(currentIndex + 1);
  const prevImage = () => currentIndex > 0 && setCurrentIndex(currentIndex - 1);

  return (
    <div className="border p-3 bg-white">
      {images.length > 0 ? (
        <img src={images[currentIndex].url} alt="Tomografía" className="img-fluid" style={{ width: '100%' }} />
      ) : (
        <p>No image available</p>
      )}
      <div className="text-center mt-3">
        <Button onClick={prevImage} disabled={currentIndex === 0}>
          <FontAwesomeIcon icon={faChevronLeft} /> Anterior
        </Button>
        <Button onClick={nextImage} disabled={currentIndex === images.length - 1} className="ms-2">
          Siguiente <FontAwesomeIcon icon={faChevronRight} />
        </Button>
      </div>
    </div>
  );
};
