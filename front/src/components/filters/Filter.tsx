import React, { useState } from "react";
import { Filters } from "../../interfaces/Filters";

interface FilterProps {
    onFilterChange: (filters: Filters) => void;
}

const Filter: React.FC<FilterProps> = ({ onFilterChange }) => {
    const [selectedProperty, setSelectedProperty] = useState<string>("Titulo");
    const [filterValue, setFilterValue] = useState<string>("");

    const handlePropertyChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedProperty(e.target.value);
    };

    const handleFilterValueChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setFilterValue(value);

        const updatedFilters: Filters = {
            title: selectedProperty === "Titulo" ? value : "",
            category: selectedProperty === "Codigo De Reporte" ? value : "",
            statusReport: selectedProperty === "Estado de informe" ? value : ""
        };

        onFilterChange(updatedFilters);
    };

    return (
        <div className="mb-3">
            <div className="row g-2">
                <div className="col-12 col-md-4">
                    <select
                        value={selectedProperty}
                        onChange={handlePropertyChange}
                        className="form-select"
                    >
                        <option value="titulo">Titulo</option>
                        <option value="Codigo De Reporte">Codigo De Reporte</option>
                        <option value="Estado de informe">Estado de Informe</option>
                    </select>
                </div>

                <div className="col-12 col-md-8">
                    <input
                        type="text"
                        placeholder={`Filtrado por ${selectedProperty}`}
                        value={filterValue}
                        onChange={handleFilterValueChange}
                        className="form-control"
                    />
                </div>
            </div>
        </div>
    );
};

export default Filter;
