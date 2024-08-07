import { instance } from "./BaseClient";
import { Tomography } from "../interfaces/Tomography";

interface TomographyResponse {
    tomographies: Tomography[];
    successful: boolean;
    error?: string; 
}

export const findTomographies = async (): Promise<Tomography[]> => {
  try {
    const response = await instance.get<TomographyResponse>("tomographies/");
    
    if (response.data.successful) {
        return response.data.tomographies;
    } else {
        throw new Error(response.data.error || "Aún no se cargaron tomografías");
    }
  } catch (error) {
    console.error("Error al obtener tomografías:", error);
    throw error;
  }
};
