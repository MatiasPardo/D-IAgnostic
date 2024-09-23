import React, { useContext, useEffect, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter";
import { instance } from '../services/BaseClient';

export const Tomographies = () => {
    const { tomographies, getTomographies } = useContext(TomographiesContext);
    const [filteredTomographies, setFilteredTomographies] = useState<Tomography[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalTomographies, setTotalTomographies] = useState(0); // Total count of items
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
        const fetchReport = async () => {
            try {
                const response = await instance.get(`tomographies`);
                setTotalTomographies(response.data.tomographies.length); 
            } catch (error) {
                console.error('Error loading reports:', error);
            }
        };
        fetchReport();
    }, []);

    useEffect(() => {
        const fetchTomographies = async () => {
            await getTomographies(currentPage);
        };
    
        fetchTomographies();
    }, [currentPage]);

    useEffect(() => {
        const filtered = tomographies.filter((tomo: Tomography) => {
            const title = tomo.title?.toLowerCase() || '';
            const category = tomo.codeReport?.toLowerCase() || '';
            const statusReport = tomo.statusReport?.toLowerCase() || '';

            const filterTitle = filters.title.toLowerCase();
            const filterCategory = filters.category.toLowerCase();
            const filterStatusReport = filters.statusReport.toLowerCase();

            return (
                (filterTitle === "" || title.includes(filterTitle)) &&
                (filterCategory === "" || category.includes(filterCategory)) &&
                (filterStatusReport === "" || statusReport.includes(filterStatusReport))
            );
        });

        setFilteredTomographies(filtered);
    }, [filters, tomographies]);

    const handleFilterChange = (newFilters: Filters) => {
        setFilters(newFilters);
        setCurrentPage(1);  
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

    return (
        <div className="p-5">
            <h1 className="mt-3 mb-3">Tomografías</h1>
            <Filter onFilterChange={handleFilterChange} />
            <hr />
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
