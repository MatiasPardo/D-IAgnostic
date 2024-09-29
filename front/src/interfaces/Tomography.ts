export interface Tomography {
    codeReport: string;
    title: string;
    category: string;
    images: string[];
    statusReport: string;
    createDate: string;
    userId: string;
    patient: Patient; 
}

export interface Patient {
    clinicHistory?: string;
    document?: string;
    birthdate?: string; 
}
