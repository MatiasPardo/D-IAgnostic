import React, { useContext, useEffect, useState } from "react";
import { TomographyCard } from "../components/TomographyCard";
import { TomographiesContext } from "../context/TomographiesContext";
import { Tomography } from "../interfaces/Tomography";
import Filter from "../components/filters/Filter"; // Asegúrate de importar el componente de filtro

export const Tomographies = () => {
    const { tomographies, getTomographies } = useContext(TomographiesContext);
    const [filteredTomographies, setFilteredTomographies] = useState<Tomography[]>([]);

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
        getTomographies();
    }, [getTomographies]);

    useEffect(() => {
        setFilteredTomographies(
            tomographies.filter( (tomo: Tomography) => 
                (filters.title === "" || tomo.title.toLowerCase().includes(filters.title.toLowerCase())) &&
                (filters.category === "" || tomo.category.toLowerCase().includes(filters.category.toLowerCase())) &&
                (filters.statusReport === "" || tomo.statusReport.toLowerCase().includes(filters.statusReport.toLowerCase()))
            )
        );
    }, [filters, tomographies]);

    const handleFilterChange = (newFilters: Filters) => {
        setFilters(newFilters);
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
        </div>
    );
};