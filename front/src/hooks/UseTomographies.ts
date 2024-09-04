import { useReducer, useState } from "react";
import { findTomographies } from "../services/TomographiesService";
import { TomographiesReducer } from "../reducers/TomographiesReducer";
import { Tomography } from "../interfaces/Tomography";

const initialTomographies: Tomography[] = [];

export const UseTomographies = () => {
  const [tomographies, dispatch] = useReducer(TomographiesReducer, initialTomographies);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 1;

  const getTomographies = async (page: number = 1) => {
    try {
      const response = await findTomographies(page, pageSize);
      dispatch({
        type: 'LOAD_TOMOGRAPHIES',
        payload: response
      });
      setCurrentPage(page); 
    } catch (error) {
      console.error("Error al obtener tomografías:", error);
    }
  };

  return {
    tomographies,
    getTomographies,
    currentPage,
    setCurrentPage
  };
};
