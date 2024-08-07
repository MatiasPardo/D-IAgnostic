import React, { useState } from 'react';
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
  setUploadedImage: (image: string | ArrayBuffer | null) => void;
}

const ImageUploader: React.FC<ImageUploaderProps> = ({ setUploadedImage }) => {
  const [image, setImage] = useState<string | ArrayBuffer | null>(null);
  const [fileName, setFileName] = useState<string>('');

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        const result = reader.result;
        setImage(result);
        setUploadedImage(result);
      };
      reader.readAsDataURL(file);
      setFileName(file.name);
    }
  };

  const handleDelete = () => {
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
              <ButtonFile className="btn btn-info btn-file me-3">
                Importar... <input type="file" id="imgInp" onChange={handleFileChange} />
              </ButtonFile>
            </span>
            <input type="text" className="form-control" value={fileName} readOnly />
            {fileName && <button className="delete-btn" onClick={handleDelete}>&times;</button>}
            </InputGroup>
          {image && <ImgUpload id='img-upload' src={image as string} alt="uploaded" className="my-3" />}
        </div>
      </div>
    </div>
  );
};

export default ImageUploader;
