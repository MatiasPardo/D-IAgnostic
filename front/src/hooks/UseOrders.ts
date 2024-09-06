import { useReducer } from "react"
import { OrdersReducer } from "../reducers/OrdersReducer"
import { createOrder, findOrders, updateOrderShared } from "../services/OrdersService"
import { Order } from "../interfaces/Order"
import { OrderRequest } from "../interfaces/OrderRequest"
import { AlertError, AlertOk } from "../components/SweetAlert"
import { requestReport } from "../services/TomographiesService"
import { TomographyRequest } from "../interfaces/TomographyRequest"


const initialOrders: Order[] = []

export const UseOrders = () => {

  const [orders, dispatch] = useReducer(OrdersReducer, initialOrders)

/*   const getOrders = async() => {
    const response = await findOrders()
    const allOrders: Order[] = response.orderDtos.map((orderDto: any) => <Order>{
      id: orderDto.orderId,
      name: orderDto.name,
      items: [],
      userId: orderDto.userId,
      status: orderDto.status,
      hasItems: orderDto.hasItems
    })

    dispatch({
      type: 'LOAD_ORDERS',
      payload: allOrders
    })
  }
 */
/*   const handleCreateOrder = async(order: OrderRequest) => {
    if(!order.id) {
      createOrder(order)
      .then(response => {
        dispatch({
          type: 'UPDATE_ORDERS',
          payload: response.data.orderId
        });
        AlertOk('Pedido', 'El pedido se creó correctamente');
      })
      .catch(e => AlertError('Pedido', 'Ocurrió un error al crear el pedido', e));
    } else {
      updateOrderShared(order.id)
      .then(response => {
        dispatch({
          type: 'UPDATE_ORDERS',
          payload: response.data.orderId
        });
        AlertOk('Pedido', 'El pedido se agrego correctamente');
      })
      .catch((e) => AlertError('Pedido', 'Ocurrió un error al actualizar el pedido', e));

    }
  } */

    const handleSaveTomography = async(tomographyRequest: TomographyRequest) => {
      try {
        console.log(tomographyRequest);
        const response = await requestReport(tomographyRequest);
        AlertOk('Tomografía', 'La tomografía se subió correctamente');
      } catch (error) {
        AlertError('Tomografía', 'Ocurrió un error al subir la tomografía','error');
      }
    };
  

  return {
    handleSaveTomography
  }
}
