import React, { useEffect, useState } from 'react';
import { Modal } from 'react-bootstrap';
import { findAll } from '../services/ProductsService';
import { ItemOrder } from "../interfaces/ItemOrder"

export const InputNameModal =  ({show, handleClose, handleFunc} : {show: any, handleClose: any, handleFunc: (type: string, nameOrId: string) => void}) => {
  const [newOrderType, setNewOrderType] = useState('new');
  const [nameOrId, setNameOrId] = useState('')

  const handleCheck = (event: any) => {
    setNewOrderType(event.target.value);
  }

  const setValue = (event: any) => setNameOrId(event.target.value);

  const handleAccept = () => {
    console.log('invocando la funcion handleFunc');
    handleFunc(newOrderType, nameOrId)
    handleClose();
  }
 
  return (
  <Modal show={show} onHide={handleClose} centered size="lg">
    <Modal.Header closeButton>
      <Modal.Title>Completar los datos para generar el informe</Modal.Title>
    </Modal.Header>
    <Modal.Body>
    <form>
  <div className="form-group row">
    <label className="col-4 col-form-label">Titulo para la tomografia</label> 
    <div className="col-8">
      <input 
      id="text" 
      name="text" 
      placeholder="Ingrese un titulo" 
      type="text" 
      className="form-control" 
      value={nameOrId}
      onChange={setValue}
      required/>
    </div>
  </div>
  <br/>
  <div className="form-group row">
    <label className="col-4 col-form-label" >Paciente</label> 
    <div className="col-8">
      <input 
      id="text1" 
      name="text1" 
      placeholder="Ingrese algun dato para identificar al paciente con la tomografia" 
      type="text" 
      value={nameOrId}
      className="form-control"
      onChange={setValue}
      required
      />
    </div>
  </div>  
  <div className="form-group row">
    <div className="offset-4 col-8">
    <br/>
    <button disabled={nameOrId.length == 0} type="submit" onClick={handleAccept} className="btn btn-success container"> Aceptar</button>
    </div>
  </div>
</form>


    </Modal.Body>
  </Modal>


    );
  };

