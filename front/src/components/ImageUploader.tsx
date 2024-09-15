import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import styled from 'styled-components';

const ButtonFile = styled.span`
  position: relative;
  overflow: hidden;

  input[type='file'] {
    position: absolute;
    top: 0;
    right: 0;
    min-width: 100%;
    min-height: 100%;
    font-size: 100px;
    text-align: right;
    filter: alpha(opacity=0);
    opacity: 0;
    outline: none;
    background: white;
    cursor: inherit;
    display: block;
  }
`;

const ImgUpload = styled.img`
  width: 100%;
`;

const InputGroup = styled.div`
  position: relative;
  display: flex;
  align-items: center;

  .delete-btn {
    position: absolute;
    right: 10px;
    border: none;
    background: none;
    font-size: 18px;
    cursor: pointer;
    color: #999;
  }

  .delete-btn:hover {
    color: #333;
  }
`;

interface ImageUploaderProps {
  setUploadedImage: (image: Blob | null) => void; // Actualiza el tipo aquí
  handleDelete: (deleteFunc: () => void) => void;
}

export const ImageUploader: React.FC<ImageUploaderProps> = ({ setUploadedImage, handleDelete }) => {
  const [image, setImage] = useState<Blob | null>(null);
  const [fileName, setFileName] = useState<string>('');

  useEffect(() => {
      handleDelete(handleDeleteClick);
  }, []);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const file = e.target.files?.[0];
      if (file) {
          setFileName(file.name);
          
          const reader = new FileReader();
          reader.onloadend = () => {
              const result = reader.result as ArrayBuffer;
              const blob = new Blob([result], { type: file.type });
              
              setImage(blob);
              setUploadedImage(blob);
          };
          reader.readAsArrayBuffer(file);
      }
  };

  const handleDeleteClick = () => {
      setImage(null);
      setFileName('');
      setUploadedImage(null);
      (document.getElementById('imgInp') as HTMLInputElement).value = '';
  };

  return (
      <div className="">
          <div className="col-md-6">
              <div className="form-group">
                  <label>Subir Imagen</label>
                  <InputGroup className="input-group">
                      <span className="input-group-btn">
                          <ButtonFile className="btn btn-file me-3" style={{backgroundColor:'var(--accent-color)'}}>
                              Importar... <input type="file" id="imgInp" onChange={handleFileChange} />
                          </ButtonFile>
                      </span>
                      <input type="text" className="form-control" value={fileName} readOnly />
                      {fileName && <button className="delete-btn" onClick={handleDeleteClick}>&times;</button>}
                      {image && <ImgUpload id='img-upload' src={URL.createObjectURL(image)} alt="uploaded" className="my-3" />}
                  </InputGroup>
              </div>
          </div>
      </div>
  );
};
