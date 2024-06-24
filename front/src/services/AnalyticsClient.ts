import { Analytics } from "../interfaces/Analytics";
import { instance } from "./BaseClient";

const endpoint = 'analytics'

export const findAnalytics = async (): Promise<Analytics> => {
  try {
    var response = instance.get(`${endpoint}`)
    return (await response).data;
  } catch (error) {
    throw error;
  }
}
