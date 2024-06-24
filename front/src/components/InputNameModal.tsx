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
    handleFunc(newOrderType, nameOrId)
    handleClose();
  }

  return (
  <Modal show={show} onHide={handleClose} centered>
    <Modal.Header closeButton>
      <Modal.Title>Creá o agregá un nuevo pedido</Modal.Title>
    </Modal.Header>
    <Modal.Body>
    <div className="form-check m-2">
      <input checked={newOrderType == 'new'} className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" value="new"  onChange={handleCheck}/>
      <label className="form-check-label"> Nuevo pedido </label>
    </div>
    <div className="form-check m-2">
      <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" value="other" onChange={handleCheck} />
      <label className="form-check-label"> Pedido ya existente </label>
    </div>

    <div className="input-group mb-3 mt-3">
      <input type="text"
        className="form-control"
        id="id"
        name="name" 
        value={nameOrId}
        onChange={setValue}
        required
        placeholder = {newOrderType == 'new' ? 'Ingresá un nombre para tu pedido' : 'Ingresá el ID de un pedido ya existente'}
        /> 
    </div>
    <button disabled={nameOrId.length == 0} type="submit" onClick={handleAccept} className="btn btn-success container"> Aceptar</button>
    </Modal.Body>
  </Modal>


    );
  };

