import { Url } from "url";

export interface Tomography {
    codeReport: string;
    title: string;
    category: string;
    images: TomographyImage[];
    statusReport: string;
    patient: Patient;
}

export interface TomographyImage {
    url: string;
    tomographyCategory: string;
}

export interface Patient {
    name: string;
    lastName: string;
    birthDate: Date;
    document: string;
    email: string;
    clinicHistory: string;
}

