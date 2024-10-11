import { instance } from "./BaseClient";
import { Tomography } from "../interfaces/Tomography";
import { TomographyRequest } from "../interfaces/TomographyRequest";
import { AxiosResponse } from "axios";
import axios from 'axios';
import { Filters } from "../interfaces/Filters";

interface TomographyResponse {
    tomographies: Tomography[];
    successful: boolean;
    error?: string;
    pagination: Pagination;
}
interface Pagination {
    totalPage: number;
    totalSize: number;
}

export const findTomographies = async (page: number, size: number, filter: Filters = {}): Promise<Tomography[]> => {
    try {
        const cachedFilters = localStorage.getItem('tomographyFilters');
        const finalFilters = cachedFilters ? { ...JSON.parse(cachedFilters), ...filter } : filter;

        const queryParams = new URLSearchParams({
            page: page.toString(),
            size: size.toString(),
            ...(finalFilters.title ? { title: finalFilters.title } : {}),
            ...(finalFilters.clinicHistory ? { clinicHistory: finalFilters.clinicHistory } : {}),
            ...(finalFilters.document ? { document: finalFilters.document } : {}),
        }).toString();

        const response = await instance.get<TomographyResponse>(`tomographies?${queryParams}`);

        if (response.data.successful) {
            localStorage.setItem('totalTomographies', JSON.stringify(response.data.pagination.totalSize));
            return response.data.tomographies;
        } else {
            throw new Error(response.data.error || "No tomographies available");
        }
    } catch (error) {
        console.error("Error fetching tomographies:", error);
        throw error;
    }
};


export const requestReport = async (tomographyRequest: TomographyRequest): Promise<AxiosResponse<{ codeReport: string }, any>> => {
    const formData = new FormData();
    formData.append("tomography", tomographyRequest.tomography);
    formData.append("title", tomographyRequest.title);
    if (tomographyRequest.codeReport) {
        formData.append("codeReport", tomographyRequest.codeReport);
    }
    if (tomographyRequest.clinicalHistory) {
        formData.append("clinicHistory", tomographyRequest.clinicalHistory);
    }  
    if (tomographyRequest.dni) {
        formData.append("document", tomographyRequest.dni);
    }
    if (tomographyRequest.birthDate) {
        const birthDateISO = `${tomographyRequest.birthDate}T00:00:00Z`;
        formData.append("birthdate", birthDateISO);
    }
    
    formData.append("lastImage", tomographyRequest.lastImage ? "true" : "false");
    return instance.post("tomographies", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};
