import { AxiosResponse } from "axios";
import { OrderRequest } from "../interfaces/OrderRequest";
import { instance } from "./BaseClient";
import { getUser } from "../helpers/UserHelper";


const endpoint = 'orders'

export const findOrders = async () => {
  try {
    const response = await instance.get("orders");
    return response.data;
  } catch (error) {
    console.log("Error findOrders")
    throw error;
  }
}

export const createOrder = async(order: OrderRequest): Promise<AxiosResponse<{orderId: string}, any>> => {
    return instance.post("orders", order);
}

export const addItem = async (orderId: string, itemId: string, quantity: number) => {
  const user = getUser();
  return instance.post(`${endpoint}/${orderId}/items`, {id: itemId, quantity: quantity, userId: user.userId})
}

export const removeItem = async (orderId: string, itemId: string) => {
  const user = getUser();
  return instance.delete(`${endpoint}/${orderId}/items/${itemId}`)
}

export const updateQuantity = async (orderId: string, itemId: string, quantity: number) => {
  const user = getUser();
  return instance.patch(`${endpoint}/${orderId}/items/${itemId}`, {quantity})
}

export const updateOrder = async (orderId: string) => {
  return instance.patch(`${endpoint}/${orderId}`, {orderId})
}

export const updateOrderShared = async (orderId: string) => {
  return instance.patch(`${endpoint}/${orderId}/users`, {orderId})
}
