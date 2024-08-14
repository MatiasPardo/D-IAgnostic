import { instance } from "./BaseClient";
import { Tomography } from "../interfaces/Tomography";
import { TomographyRequest } from "../interfaces/TomographyRequest";
import { AxiosResponse } from "axios";

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


export const requestReport = async(tomographyRequest: TomographyRequest): Promise<AxiosResponse<{codeReport: string}, any>> => {
  console.log('hola mundo');
  return instance.post("tomographies", tomographyRequest, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}