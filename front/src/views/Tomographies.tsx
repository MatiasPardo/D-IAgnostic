import React, { useContext, useEffect, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter";
import { instance } from "../services/BaseClient";

export const fetchTotalTomographiesFromLocalStorage = () => {
    const savedTotalTomographies = localStorage.getItem('totalTomographies');
    return savedTotalTomographies ? JSON.parse(savedTotalTomographies) : 9;
};

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

    const [selectedProperty, setSelectedProperty] = useState("Titulo");
    const [isLastPage, setIsLastPage] = useState(false);
    useEffect(() => {
        const total = fetchTotalTomographiesFromLocalStorage();
        setTotalTomographies(total);
    }, []);

    useEffect(() => {
        resetToInitialState();
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
            return filterTitle === "" || title.includes(filterTitle);
        });
        setFilteredTomographies(filtered);
    }, [filters, tomographies]);

    useEffect(() => {
        setIsLastPage(totalTomographies <= currentPage * pageSize);
    }, [totalTomographies, currentPage]);

    const handleFilterChange = (newFilters: Filters) => {
        setFilters(newFilters);
        setCurrentPage(1);
        localStorage.setItem('tomographyFilters', JSON.stringify(newFilters));
        getTomographies(1, newFilters);
        updateTotalTomographies(newFilters);
    };

    const updateTotalTomographies = (filters: Filters) => {
        const filteredTotal = tomographies.filter((tomo: Tomography) => {
            const title = tomo.title?.toLowerCase() || '';
            const filterTitle = filters.title?.toLowerCase() || '';
            return filterTitle === "" || title.includes(filterTitle);
        });
        setTotalTomographies(filteredTotal.length);
    };

    const handleNextPage = () => setCurrentPage((prevPage) => prevPage + 1);

    const handlePreviousPage = () => {
        if (currentPage > 1) setCurrentPage((prevPage) => prevPage - 1);
    };

    const resetToInitialState = async () => {
        setTotalTomographies(0);
        localStorage.removeItem('tomographyFilters');
        setFilters({ title: "", clinicHistory: "", document: "" });
        setSelectedProperty("Titulo");
        getTomographies(1);
        try {
            const response = await instance.get(`tomographies?page=0&size=3`);
            console.log("eliel",response)
            if (response.status === 200) {
                localStorage.setItem('totalTomographies', JSON.stringify(response.data.pagination.totalSize));
                setTotalTomographies(response.data.pagination.totalSize); // Update total size
            }
        } catch (error) {
            
        }

    };

    return (
        <div className="p-5">
            <h1 className="mt-3 mb-3">Tomografías</h1>
            <p>Aquí usted puede visualizar todos los informes que ha solicitado, y buscar por título, nombre del paciente, historia clínica, etc.</p>

            <Filter 
                onFilterChange={handleFilterChange} 
                onReset={resetToInitialState} 
                selectedProperty={selectedProperty}
                setSelectedProperty={setSelectedProperty} 
            />

            <hr />

            <div className="row rows-cols-1 row-cols-md-6 d-flex flex-row justify-content-around" style={{ marginRight: '15%' }}>
                {filteredTomographies.map((tomo: Tomography) => (
                    <TomographyCard key={tomo.codeReport} tomography={tomo} />
                ))}
            </div>

            <div className="d-flex justify-content-center mt-4">
                <div className="btn-group">
                    <button className="btn btn-primary me-2" onClick={handlePreviousPage} disabled={currentPage === 1}>
                        Anterior
                    </button>
                    <span className="d-flex align-items-center mx-2">Página {currentPage}</span>
                    <button
                        className="btn btn-primary ms-2"
                        onClick={handleNextPage}
                        disabled={isLastPage}
                    >
                        Siguiente
                    </button>
                </div>
            </div>
        </div>
    );
};
