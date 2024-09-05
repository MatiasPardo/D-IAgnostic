import React, { useContext, useEffect, useReducer, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter"; 
import { findTomographies } from "../services/TomographiesService";
import { TomographiesReducer } from "../reducers/TomographiesReducer";

export const Tomographies = () => {
    const { tomographies, getTomographies } = useContext(TomographiesContext);
    const [filteredTomographies, setFilteredTomographies] = useState<Tomography[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const pageSize = 5; 

    type Filters = {
        title: string;
        category: string;
        statusReport: string;
    };
    
    const [filters, setFilters] = useState<Filters>({
        title: "",
        category: "",
        statusReport: ""
    });

    useEffect(() => {
        getTomographies(currentPage); 
    }, [getTomographies, currentPage]);

    useEffect(() => {
        setFilteredTomographies(
            tomographies.filter((tomo: Tomography) => 
                (filters.title === "" || tomo.title.toLowerCase().includes(filters.title.toLowerCase())) &&
                (filters.category === "" || tomo.category.toLowerCase().includes(filters.category.toLowerCase())) &&
                (filters.statusReport === "" || tomo.statusReport.toLowerCase().includes(filters.statusReport.toLowerCase()))
            )
        );
    }, [filters, tomographies]);

    const handleFilterChange = (newFilters: Filters) => {
        setFilters(newFilters);
    };

    const handleNextPage = () => {
        setCurrentPage(currentPage + 1);
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    return (
        <div className="p-5">
            <h1>Tomografías</h1>
            <Filter onFilterChange={handleFilterChange} />
            <hr />
            <div className="row rows-cols-1 row-cols-md-6">
                {filteredTomographies.map((tomo: Tomography) => (
                    <TomographyCard
                        key={tomo.codeReport}
                        tomography={tomo}
                    />
                ))}
            </div>
            <div className="d-flex justify-content-center mt-4">
                <div className="btn-group">
                    <button
                        className="btn btn-primary me-2"
                        onClick={handlePreviousPage}
                        disabled={currentPage === 1}
                    >
                        Anterior
                    </button>
                    <span className="d-flex align-items-center mx-2">
                        Página {currentPage}
                    </span>
                    <button
                        className="btn btn-primary ms-2"
                        onClick={handleNextPage}
                        disabled={filteredTomographies.length < pageSize}
                    >
                        Siguiente
                    </button>
                </div>
            </div>
        </div>
    );
};

export const UseTomographies = () => {
  const [tomographies, dispatch] = useReducer(TomographiesReducer, findTomographies);
  const [currentPage, setCurrentPage] = useState(1); 
  const pageSize = 4;

  const getTomographies = async (page: number) => {
    try {
      const response = await findTomographies(page - 1, pageSize); 
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
    getTomographies,
    currentPage,
    setCurrentPage
  };
};
