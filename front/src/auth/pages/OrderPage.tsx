import { NavLink } from "react-router-dom"
import { Order } from "../../interfaces/Order"
import { useState } from 'react';
import { Clipboard } from 'react-bootstrap-icons';
import OrderModal from "../../components/OrderModal";
import { updateOrder } from "../../services/OrdersService";
import { AlertOk } from "../../components/SweetAlert";

export const OrderPage = (order: Order) => {
  const [showSnackbar, setShowSnackbar] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const openOrderModal = () => {
    setShowModal(true);
  };

  const closeOrderModal = () => {
    setShowModal(false);
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(order.id)
      .then(() => {
        setShowSnackbar(true);
        setTimeout(() => {
          setShowSnackbar(false); // Ocultar el Snackbar después de un tiempo
        }, 3000); // Duración del Snackbar en pantalla
      })
      .catch((error) => {
        console.error('Error al copiar al portapapeles: ', error);
      });
  };

  const closeOrder = () => {
    updateOrder(order.id).then(() => {
      //  dispatch({
      //    type: 'UPDATE_ORDERS',
      //    payload: order.id
      //  });
      AlertOk('Pedido', 'El pedido se cerró con éxito');
    });
  }

  const canCloseOrder = (orderUserId : string, orderStatus : string, orderHasItems: boolean) => {
    let login = localStorage.getItem("login");

    let userId = null;

    if (login !== null)
      userId = JSON.parse(login).user.userId;
    
    return orderHasItems 
      && orderStatus != 'CLOSED'
      && orderUserId == userId;
  }

  const canAddItems = (orderStatus : string) => {
    return orderStatus != 'CLOSED';
  }

  return (
     <li className="list-group-item border border-dark p-3 mb-3">
      <div className="d-flex justify-content-between align-items-center">
        <div>
          <p className="mb-1"><strong>Nombre:</strong> {order.name}</p>
          <div className="d-flex align-items-center"> {/* Agregamos un contenedor flex para alinear el botón junto a order.id */}
            <p className="mb-1"><strong>Orden:</strong> {order.id}</p>
              <button
                className="btn btn-secondary mx-2" // Reducimos el espacio entre el texto y el botón
                onClick={copyToClipboard}
              >
              <Clipboard/> Copiar
            </button>
          </div>
        </div>
        <div>
          <NavLink
            className={`btn ${canAddItems(order.status) ? 'btn-success' : 'btn-disabled'}`}
            to={canAddItems(order.status) ? `/items?order_id=${order.id}` : '#'}
            onClick={canAddItems(order.status) ? undefined : (e) => e.preventDefault()}>
            Modificar ítems
          </NavLink>

          <button 
            className="btn btn-success mx-2" 
            onClick={() => openOrderModal()}>
              Ver detalle orden
          </button>

          <OrderModal show={showModal} handleClose={closeOrderModal} productId={order.id}/>

          <button
            className="btn btn-danger mx-2"
            onClick={() => closeOrder()}
            disabled={!canCloseOrder(order.userId, order.status, order.hasItems)}>
              Cerrar Pedido
          </button>
        </div>
      </div>
      {showSnackbar && (
        <div className="position-absolute top-0 start-50 translate-middle-x alert alert-success" role="alert">
          Código copiado al portapapeles
        </div>
      )}
    </li>
  )
}
