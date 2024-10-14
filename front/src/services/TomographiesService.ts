import { instance } from "../services/BaseClient";
import { Tomography } from "../interfaces/Tomography";
import { Filters } from "../interfaces/Filters";
import { fetchTotalTomographiesFromLocalStorage } from '../views/Tomographies';
import { AxiosResponse } from "axios";
import { TomographyRequest } from "../interfaces/TomographyRequest";

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
            fetchTotalTomographiesFromLocalStorage();
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
    if (tomographyRequest.lastName) {
        formData.append("lastName", tomographyRequest.lastName);
    }
    if (tomographyRequest.name) {
        formData.append("name", tomographyRequest.name);
    }
    if (tomographyRequest.title) {
        formData.append("title", tomographyRequest.title);
    }
    if (tomographyRequest.tomography) {
        formData.append("tomography", tomographyRequest.tomography);
    }
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

