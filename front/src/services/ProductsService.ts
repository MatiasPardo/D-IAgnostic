import { instance } from "./BaseClient";

export const findAll = async (orderId: string | null): Promise<any> => {
  if (orderId == '') orderId = null;
  const params = {
    order_id: orderId
  }

  try {
    const response = await instance.get("items", {params})
    return response.data;
  } catch (error) {
    throw error;
  }
}
