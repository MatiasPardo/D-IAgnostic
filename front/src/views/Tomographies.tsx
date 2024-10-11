import React, { useContext, useEffect, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter";
import { findTomographies } from '../services/TomographiesService';

export const Tomographies = () => {
    const { tomographies, getTomographies } = useContext(TomographiesContext);
    const [filteredTomographies, setFilteredTomographies] = useState<Tomography[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalTomographies, setTotalTomographies] = useState(0);
    const pageSize = 3;

    type Filters = {
        title?: string;
        clinicHistory?: string;
        document?: string;
    };

    const [filters, setFilters] = useState<Filters>({
        title: "",
        clinicHistory: "",
        document: ""
    });

    useEffect(() => {
        const savedTotalTomographies = localStorage.getItem('totalTomographies');
        if (savedTotalTomographies) {
            setTotalTomographies(JSON.parse(savedTotalTomographies));
        }
    }, []);

    useEffect(() => {
        const savedFilters = localStorage.getItem('tomographyFilters');
        if (savedFilters) {
            const parsedFilters = JSON.parse(savedFilters);
            setFilters(parsedFilters);
            getTomographies(currentPage, parsedFilters);
        } else {
            getTomographies(currentPage);
        }
    }, [currentPage]);

    useEffect(() => {
        const filtered = tomographies.filter((tomo: Tomography) => {
            const title = tomo.title?.toLowerCase() || '';
            const filterTitle = filters.title?.toLowerCase() || '';

            return (
                (filterTitle === "" || title.includes(filterTitle))
            );
        });

        setFilteredTomographies(filtered);
    }, [filters, tomographies]);

    const handleFilterChange = (newFilters: Filters) => {
        setFilters(newFilters);
        setCurrentPage(1);
        localStorage.setItem('tomographyFilters', JSON.stringify(newFilters)); // Save filters to local storage
        getTomographies(1, newFilters);
    };

    const handleNextPage = () => {
        setCurrentPage(currentPage + 1);
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const isLastPage = filteredTomographies.length < pageSize;

    const resetToInitialState = () => {
        setTotalTomographies(9); 
        setCurrentPage(1);
        setFilters({ title: "", clinicHistory: "", document: "" });
        localStorage.removeItem('tomographyFilters');
        getTomographies(1); 
    };

    return (
        <div className="p-5">
            <h1 className="mt-3 mb-3">Tomografías</h1>
            <p>Aquí usted puede visualizar todos los informes que ha solicitado, y buscar por título, nombre del paciente, historia clínica, etc.</p>

            <Filter onFilterChange={handleFilterChange} />

            <hr />

            <div className="d-flex justify-content-end mb-3">
                <button className="btn btn-secondary" onClick={resetToInitialState}>
                    Resetear a Estado Inicial
                </button>
            </div>

            <div className="row rows-cols-1 row-cols-md-6 d-flex flex-row justify-content-around" style={{ marginRight: '15%' }}>
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
                        disabled={isLastPage || currentPage * pageSize >= totalTomographies}
                    >
                        Siguiente
                    </button>
                </div>
            </div>
        </div>
    );
};
