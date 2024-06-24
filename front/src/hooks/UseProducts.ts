import { useReducer } from "react"
import { ProductsReducer } from "../reducers/ProductsReducer"
import { findAll } from "../services/ProductsService"
import { ItemOrder } from "../interfaces/ItemOrder"

const initialProducts: ItemOrder[] = []

export const UseProducts = () => {

  const [products, dispatch] = useReducer(ProductsReducer, initialProducts)

  const getProducts = async(order_id: string) => {
    const allProducts: ItemOrder[] = await findAll(order_id)

    dispatch({
      type: 'LOAD_PRODUCTS',
      payload: allProducts
    })
  }

  return {
    products,
    getProducts,
  }
}
