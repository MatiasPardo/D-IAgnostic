import React, { useContext, useEffect, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter";

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
        //revisar recursividad
        
    useEffect(() => {
        console.log('Filters:', filters);
        console.log('Tomographies:', tomographies);

        const filtered = tomographies.filter((tomo: Tomography) => {
            const title = tomo.title?.toLowerCase() || '';
            const category = tomo.codeReport?.toLowerCase() || '';
            const statusReport = tomo.statusReport?.toLowerCase() || '';

            const filterTitle = filters.title.toLowerCase();
            const filterCategory = filters.category.toLowerCase();
            const filterStatusReport = filters.statusReport.toLowerCase();

            console.log('Filtering:', {
                title, category, statusReport,
                filterTitle, filterCategory, filterStatusReport
            });

            return (
                (filterTitle === "" || title.includes(filterTitle)) &&
                (filterCategory === "" || category.includes(filterCategory)) &&
                (filterStatusReport === "" || statusReport.includes(filterStatusReport))
            );
        });

        console.log('Filtered Tomographies:', filtered);
        setFilteredTomographies(filtered);
    }, [filters, tomographies]);

    const handleFilterChange = (newFilters: Filters) => {
        console.log('Handle Filter Change:', newFilters);
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
            <div className="row rows-cols-1 row-cols-md-6 d-flex flex-row justify-content-around">
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
