import { instance } from "./BaseClient";
import { Tomography } from "../interfaces/Tomography";
import { TomographyRequest } from "../interfaces/TomographyRequest";
import { AxiosResponse } from "axios";
import axios from 'axios';


interface TomographyResponse {
    tomographies: Tomography[];
    successful: boolean;
    error?: string;
}

export const findTomographies = async (page: number, size: number): Promise<Tomography[]> => {
    try {
        const response = await instance.get<TomographyResponse>(`tomographies?page=${page}&size=${size}`);
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

export const requestReport = async (tomographyRequest: TomographyRequest): Promise<AxiosResponse<{ codeReport: string }, any>> => {
    const formData = new FormData();
    formData.append("tomography", tomographyRequest.tomography);
    formData.append("title", tomographyRequest.title);
    formData.append("lastImage", tomographyRequest.lastImage ? "true" : "false");
    console.log(formData)
    return instance.post("tomographies", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};