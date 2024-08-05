import { instance } from "./BaseClient";
import { Tomography } from "../interfaces/Tomography";

// Define el tipo de respuesta que coincide con el formato del servidor
interface TomographyResponse {
    tomographies: Tomography[];
    successful: boolean;
    error?: string; // `error` es opcional en caso de que no haya errores
}

export const findTomographies = async (): Promise<Tomography[]> => {
  try {
    // Realiza la solicitud y recibe el objeto con el formato esperado
    const response = await instance.get<TomographyResponse>("tomographies/");
    
    // Verifica si la respuesta fue exitosa
    if (response.data.successful) {
        // Devuelve la lista de tomografías
        return response.data.tomographies;
    } else {
        // Maneja el caso en el que la respuesta no sea exitosa
        throw new Error(response.data.error || "Aún no se cargaron tomografías");
    }
  } catch (error) {
    // Maneja cualquier error que ocurra durante la solicitud
    console.error("Error al obtener tomografías:", error);
    throw error;
  }
};
