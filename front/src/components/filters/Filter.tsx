import React, { useState, useEffect } from "react";
import { Filters } from "../../interfaces/Filters";

interface FilterProps {
    onFilterChange: (filters: Filters) => void;
}

const Filter: React.FC<FilterProps> = ({ onFilterChange }) => {
    const [selectedProperty, setSelectedProperty] = useState<string>("Titulo");
    const [filterValue, setFilterValue] = useState<string>("");

    useEffect(() => {
        const savedFilters = localStorage.getItem('tomographyFilters');
        if (savedFilters) {
            const parsedFilters = JSON.parse(savedFilters);
            setSelectedProperty(parsedFilters.title ? "Titulo" : (parsedFilters.document ? "Documento" : "Historia Clinica"));
            setFilterValue(parsedFilters.title || parsedFilters.document || parsedFilters.clinicHistory || "");
        }
    }, []);

    const handlePropertyChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedProperty(e.target.value);
        // Clear the filter value when the property changes
        setFilterValue("");
    };

    const handleFilterValueChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setFilterValue(value);
    };

    const handleFilterSubmit = () => {
        const updatedFilters: Filters = {
            title: selectedProperty === "Titulo" ? filterValue : "",
            document: selectedProperty === "Documento" ? filterValue : "",
            clinicHistory: selectedProperty === "Historia Clinica" ? filterValue : "",
        };

        localStorage.setItem('tomographyFilters', JSON.stringify(updatedFilters));
        onFilterChange(updatedFilters);
    };

    const handleClearFilters = () => {
        localStorage.removeItem('tomographyFilters');
        setSelectedProperty("Titulo");
        setFilterValue("");
        onFilterChange({ title: "", document: "", clinicHistory: "" });
    };

    return (
        <div className="accordion" id="accordionExample">
            <div className="accordion-item">
                <h2 className="accordion-header" id="headingOne">
                    <button className="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne" style={{ backgroundColor: 'var(--primary-color-2)' }}>
                        Buscador
                    </button>
                </h2>
                <div id="collapseOne" className="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
                    <div className="accordion-body">
                        <div className="mb-3">
                            <div className="row g-2">
                                <div className="col-12 col-md-4">
                                    <select
                                        value={selectedProperty}
                                        onChange={handlePropertyChange}
                                        className="form-select"
                                    >
                                        <option value="Titulo">Titulo</option>
                                        <option value="Documento">Documento</option>
                                        <option value="Historia Clinica">Historia Clinica</option>
                                    </select>
                                </div>

                                <div className="col-12 col-md-6">
                                    <input
                                        type="text"
                                        placeholder={`Filtrado por ${selectedProperty}`}
                                        value={filterValue}
                                        onChange={handleFilterValueChange}
                                        className="form-control"
                                    />
                                </div>

                                <div className="col-12 col-md-2">
                                    <button onClick={handleFilterSubmit} className="btn btn-primary w-100">Buscar</button>
                                </div>
                            </div>
                        </div>
                        <button onClick={handleClearFilters} className="btn btn-secondary mt-2">Limpiar Filtros</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Filter;
