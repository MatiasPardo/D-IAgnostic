import React, { useState } from "react";
import { Filters } from "../../interfaces/Filters";

interface FilterProps {
    onFilterChange: (filters: Filters) => void;
}

const Filter: React.FC<FilterProps> = ({ onFilterChange }) => {
    const [filters, setFilters] = useState<Filters>({
        title: "",
        category: "",
        statusReport: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        const updatedFilters = { ...filters, [name]: value };
        setFilters(updatedFilters);
        onFilterChange(updatedFilters);
    };

    return (
        <div className="mb-3">
            <input
                type="text"
                name="title"
                placeholder="Filtrar por titulo"
                value={filters.title}
                onChange={handleChange}
                className="form-control mb-2"
            />
            <input
                type="text"
                name="category"
                placeholder="Filtrar por estado de la tomografia"
                value={filters.category}
                onChange={handleChange}
                className="form-control mb-2"
            />
            <input
                type="text"
                name="statusReport"
                placeholder="Filtrar por estado del informe"
                value={filters.statusReport}
                onChange={handleChange}
                className="form-control mb-2"
            />
        </div>
    );
};

export default Filter;
