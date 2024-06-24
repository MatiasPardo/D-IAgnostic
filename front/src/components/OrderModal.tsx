import React, { useEffect, useState } from 'react';
import { Modal } from 'react-bootstrap';
import { findAll } from '../services/ProductsService';
import { ItemOrder } from "../interfaces/ItemOrder"

const OrderModal =  ({show, handleClose, productId} : {show: any, handleClose: any, productId: any}) => {
    const [products, setProducts] = useState([]);

    const getProducts = async ()=> {
        const p = await findAll(productId)
        setProducts(p)
    }
    
    useEffect(() => { 
        if (show) {
            getProducts();
        }
    }, [show]);

  return (
<Modal show={show} onHide={handleClose} centered>
  <Modal.Header closeButton>
    <Modal.Title>Detalles del Pedido</Modal.Title>
  </Modal.Header>
  <Modal.Body>
    <div className="container">
      <div className="row">
        <div className="col-md-6">
          <table className="table">
            <thead>
              <tr>
                <th style={{width: '25%'}}>Imagen</th>
                <th style={{width: '25%'}}>Nombre</th>
                <th style={{width: '25%'}}>Cantidad</th>
                <th style={{width: '25%'}}>Precio</th>
              </tr>
            </thead>
            <tbody>
              {products
                .filter((io: ItemOrder) => io.quantity > 0)
                .map((io: ItemOrder, index) => (
                  <tr key={index}>
                    <td>
                      <img
                        src={io.item.picture}
                        alt={io.item.name}
                        className="img-thumbnail"
                      />
                    </td>
                    <td>{io.item.name}</td>
                    <td>{io.quantity}</td>
                    <td>${io.item.price}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </Modal.Body>
</Modal>


  );
};

export default OrderModal;
