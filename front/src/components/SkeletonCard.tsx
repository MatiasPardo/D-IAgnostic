import React from "react";

const SkeletonCard: React.FC = () => (
    <div className="card m-2">
        <div className="card-body">
            <div className="skeleton skeleton-text mb-2"></div>
            <div className="skeleton skeleton-text mb-2"></div>
            <div className="skeleton skeleton-image"></div>
        </div>
    </div>
);

export default SkeletonCard;
