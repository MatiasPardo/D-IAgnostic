import { Url } from "url";

export interface Tomography {
    codeReport: string;
    title: string;
    category: string;
    images: string[];
    statusReport: string;
    createDate: string[];
    // @TODO agregar --> clinicHistory: string; 
}