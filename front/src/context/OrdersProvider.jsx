import { UseOrders } from '../hooks/UseOrders'
import { UseProducts } from '../hooks/UseProducts'
import { OrdersContext } from './OrdersContext'

export const OrdersProvider = ({children}) => {

    const {products, getProducts} = UseProducts()
    const {orders, getOrders, handleSaveTomography} = UseOrders()

    return (
        <OrdersContext.Provider value={
            {
                handleSaveTomography
            }
        }>
            {children}
        </OrdersContext.Provider>
    )
}
