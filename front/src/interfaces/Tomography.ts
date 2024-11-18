import { Url } from "url";

export interface Tomography {
    clinicalHistory: any;
    document: any;
    codeReport: string;
    title: string;
    category: string;
    images: TomographyImage[];
    statusReport: string;
    patient: Patient;
    userId: string;
    createdDate: string;
}

export interface TomographyImage {
    url: string;
    tomographyCategory: string;
}

export interface Patient {
    name: string;
    lastName: string;
    birthdate: string;
    document: string;
    email: string;
    clinicHistory: string;
    hospital : string;
    typeDocument : string;
    detail : string;
}

