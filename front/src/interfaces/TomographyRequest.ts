export interface TomographyRequest {
    title: string;
    tomography: Blob;
    codeReport?: string;
    name?: string;
    lastName?: string;
    lastImage?: boolean;
    birthDate?: string;
    clinicalHistory?: string;
    dni?: string;
}
