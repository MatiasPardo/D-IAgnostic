import { useReducer } from "react";
import { findTomographies } from "../services/TomographiesService";
import { TomographiesReducer } from "../reducers/TomographiesReducer"
import { Tomography } from "../interfaces/Tomography";

const initialTomographies: Tomography[] = [];

export const UseTomographies = () => {
  const [tomographies, dispatch] = useReducer(TomographiesReducer, initialTomographies);

  const getTomographies = async () => {
    try {
      const response = await findTomographies();
      dispatch({
        type: 'LOAD_TOMOGRAPHIES',
        payload: response
      });
    } catch (error) {
      console.error("Error al obtener tomografías:", error);
    }
  };

  return {
    tomographies,
    getTomographies
  };

}
