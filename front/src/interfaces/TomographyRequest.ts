export interface TomographyRequest {
    title: string;
    tomography: Blob;
    codeReport?: string;
    patientName?: string;
    lastImage?: boolean;
    birthDate?: string;
    clinicalHistory?: string;
    dni?: string;
}
