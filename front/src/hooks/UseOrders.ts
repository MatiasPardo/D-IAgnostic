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

    const handleSaveTomography = async (tomographyRequest: TomographyRequest) => {
      try {
          const response = await requestReport(tomographyRequest);
          
          if (response.data && response.data.codeReport) {
              AlertOk('Tomografía', 'La tomografía se subió correctamente');
              return response;
          } else {
              AlertError('Tomografía', 'No se recibió el código de informe', 'error');
              throw new Error('Missing codeReport');
          }
      } catch (error) {
          AlertError('Tomografía', 'Ocurrió un error al subir la tomografía', 'error');
          throw error; 
      }
  };
  return {
    handleSaveTomography
  }
}
