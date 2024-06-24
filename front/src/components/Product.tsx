import { useContext, useEffect, useState } from "react";
import "./Product.css";
import { OrdersContext } from "../context/OrdersContext";
import { Item } from "../interfaces/Item";
import { addItem, removeItem, updateQuantity } from "../services/OrdersService";
import { AlertError, AlertOk } from "./SweetAlert";
import { ItemOrder } from "../interfaces/ItemOrder";

const defaultProduct: Item = {
  id: "",
  name: "Nombre del Producto",
  description: "Descripción del Producto",
  price: 2000,
  picture: "https://i.postimg.cc/NfR2yhNs/image-equilibrium.jpg",
  quantity: 0
}

const defaultItemOrder: ItemOrder = {
  item: defaultProduct,
  quantity: 0,
  total: 0
}

export const Product = (itemOrder: ItemOrder = defaultItemOrder ) => {

  const [quantity, setQuantity] = useState(itemOrder.quantity);
  const [total, setTotal] = useState(itemOrder.total);
  const [debounceActive, setDebounceActive] = useState(false);
  const queryString = window.location.search;
  const orderId = new URLSearchParams(queryString).get('order_id');

  useEffect(() => {
      setQuantity(itemOrder.quantity)
      setTotal(itemOrder.total)
  }, [itemOrder])
  

  const handleChange = (event: any) => {
    setQuantity(parseInt(event.target.value));
    if (debounceActive) return;
    setDebounceActive(true);    
    let timeoutId = setInterval( () => {
      internalUpdateQuantity(parseInt(event.target.value))
      clearTimeout(timeoutId);
      setDebounceActive(false);
    }, 1000)
  }

  const handleAddProduct = () => {
    if (debounceActive) return;
    setDebounceActive(true);    
    let timeoutId = setInterval( () => {
      internalUpdateQuantity(quantity + 1);
      clearTimeout(timeoutId);
      setDebounceActive(false);
    }, 500)
    setQuantity(quantity + 1)
  }

  const handleRemoveProduct = () => {
    if (quantity == 0) return;
    if (debounceActive) return;
    setDebounceActive(true);    
    let timeoutId = setInterval( () => {
      internalUpdateQuantity(quantity - 1);
      clearTimeout(timeoutId);
      setDebounceActive(false);
    }, 500)
    setQuantity(quantity  - 1);
  }

  const internalUpdateQuantity = (value: number) => {
    if (value == 0)  {
      removeItem(orderId!, itemOrder.item.id)
        .then(r => AlertOk('Eliminación', 'El item se eliminó correctamente del pedido'))
        .catch(e => AlertError('Eliminación', 'Hubo un error al intentar borrar el item del pedido', e.response.data.message));
      return;
    }
    
    if (quantity == 0) {
      addItem(orderId!, itemOrder.item.id, value)
        .then(r => AlertOk('Agregar Item', 'El item se agregó correctamente al pedido' ))
        .catch(e =>  AlertError('Agregar Item', 'Hubo un error al intentar agregar el item al pedido', e.response.data.message));
      return;
    }
    updateQuantity(orderId!, itemOrder.item.id, value)
  }

  return (
    <div className="card-container">  
        <img className="product-image" src={itemOrder.item.picture} alt="Product Image"/>
        <main className="main-content">
          <h1 className="product-title">{itemOrder.item.name}</h1>
          <p className="product-description">{itemOrder.item.description}</p>
          <div className="flex-row">
            <div className="price-container">
              
              <h2 className="price">${itemOrder.item.price}</h2>
            </div>

          </div>
        </main>
        { orderId ? 
          <>
            <div className="card-attribute">
              <button onClick={handleRemoveProduct} className="btn btn-success btn-block  p-3 m-4"> - </button>
              <div className="input-group mb-3">
                <input type="text"
                  onInput={handleChange}
                  className="form-control"
                  id="quantity"
                  name="quantity" 
                  value={quantity}
                  /> 
              </div>
              <button onClick={handleAddProduct} className="btn btn-success btn-block p-3 m-4"> + </button>
              <br />
            
            </div>
            <div className="alert alert-primary">
              <strong >Total: {total} </strong> 
              <i title="Este valor representa el total de items agregados a este pedido, tanto los tuyos como los agregados por otros participantes del pedido" className="fas fa-circle-info"></i>  
            </div>
            </> 
            : 
            <div className="alert alert-warning">
              <strong>Selecioná un pedido para poder agregar items!</strong> 
            </div>
        }
    </div>
  );
}
