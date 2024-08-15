import React, { useState } from "react";

const Pagination: React.FC<{ totalItems: number; itemsPerPage: number; onPageChange: (page: number) => void }> = ({ totalItems, itemsPerPage, onPageChange }) => {
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const [currentPage, setCurrentPage] = useState(1);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        onPageChange(page);
    };

    return (
        <div className="pagination">
            {Array.from({ length: totalPages }, (_, index) => (
                <button
                    key={index}
                    onClick={() => handlePageChange(index + 1)}
                    className={`page-item ${currentPage === index + 1 ? "active" : ""}`}
                >
                    {index + 1}
                </button>
            ))}
        </div>
    );
};

export default Pagination;
