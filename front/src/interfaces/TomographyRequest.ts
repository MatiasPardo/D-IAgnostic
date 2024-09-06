export interface TomographyRequest {
    title: string;
    tomography: Blob;
    patientName?: string;
    lastImage?: boolean;
}
