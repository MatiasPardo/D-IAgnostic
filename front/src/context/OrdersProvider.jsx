import { UseOrders } from '../hooks/UseOrders'
import { UseProducts } from '../hooks/UseProducts'
import { OrdersContext } from './OrdersContext'

export const OrdersProvider = ({children}) => {

    const {products, getProducts} = UseProducts()
    const {orders, getOrders, handleCreateOrder} = UseOrders()

    return (
        <OrdersContext.Provider value={
            {
                products,
                orders,
                getProducts,
                getOrders,
                handleCreateOrder
            }
        }>
            {children}
        </OrdersContext.Provider>
    )
}
