import React, { useContext, useEffect, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter"; // Make sure to import the filter component

export const Tomographies = () => {
    const { tomographies, getTomographies } = useContext(TomographiesContext);
    const [filteredTomographies, setFilteredTomographies] = useState<Tomography[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const pageSize = 3; // You can adjust the number of items per page

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

    // Fetch tomographies when the component mounts or when the currentPage changes
    useEffect(() => {
        getTomographies(currentPage); // Pass the current page to getTomographies
    }, [getTomographies, currentPage]);

    // Apply filters to the tomographies
    useEffect(() => {
        setFilteredTomographies(
            tomographies.filter((tomo: Tomography) => 
                (filters.title === "" || tomo.title.toLowerCase().includes(filters.title.toLowerCase())) &&
                (filters.category === "" || tomo.category.toLowerCase().includes(filters.category.toLowerCase())) &&
                (filters.statusReport === "" || tomo.statusReport.toLowerCase().includes(filters.statusReport.toLowerCase()))
            )
        );
    }, [filters, tomographies]);

    // Update filters when the filter component changes
    const handleFilterChange = (newFilters: Filters) => {
        setFilters(newFilters);
    };

    // Handlers for pagination
    const handleNextPage = () => {
        setCurrentPage(currentPage + 1);
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    return (
        <div className="p-4">
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
            <div className="pagination-controls">
                <button onClick={handlePreviousPage} disabled={currentPage === 1}>
                    Previous
                </button>
                <span>Page {currentPage}</span>
                <button onClick={handleNextPage} disabled={filteredTomographies.length < pageSize}>
                    Next
                </button>
            </div>
        </div>
    );
};
