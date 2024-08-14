import React, { useState } from 'react';
import { Modal } from 'react-bootstrap';

export const InputNameModal =  ({show, handleClose, handleFunc} : {show: any, handleClose: any, handleFunc: (title: string, file: File) => void}) => {
  const [title, setTitle] = useState('');
  const [file, setFile] = useState<File | null>(null);

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(event.target.value);
  }

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setFile(event.target.files[0]);
    }
  }

  const handleAccept = () => {
    if (file && title) {
      handleFunc(title, file);
      handleClose();
    }
  }

  return (
    <Modal show={show} onHide={handleClose} centered size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Completar los datos para generar el informe</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form>
          <div className="form-group row">
            <label className="col-4 col-form-label">Título para la tomografía</label> 
            <div className="col-8">
              <input 
                id="text" 
                name="text" 
                placeholder="Ingrese un título" 
                type="text" 
                className="form-control" 
                value={title}
                onChange={handleTitleChange}
                required
              />
            </div>
          </div>
          <br/>
          <div className="form-group row">
            <label className="col-4 col-form-label">Archivo de tomografía</label> 
            <div className="col-8">
              <input 
                id="file" 
                name="file" 
                type="file" 
                className="form-control" 
                onChange={handleFileChange}
                required
              />
            </div>
          </div>  
          <div className="form-group row">
            <div className="offset-4 col-8">
              <br/>
              <button 
                disabled={!title || !file} 
                type="button" 
                onClick={handleAccept} 
                className="btn btn-success container"
              >
                Aceptar
              </button>
            </div>
          </div>
        </form>
      </Modal.Body>
    </Modal>
  );
};