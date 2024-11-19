import { instance } from "./BaseClient";
import { FeedbackRequest } from "../interfaces/FeedbackRequest";
import { AxiosResponse } from "axios";

type SectionErrorKey = "Patologia" | "Informe Medico" | "Reporte" | "Otro";

const sectionErrorEnum = {
    "Patologia": "COMPOSITION",
    "Informe Medico": "SIZE",
    "Reporte": "REPORT",
    "Otro": "OTRO"
};
// @TODO ojo aca estan las variables viejas q recibe el back

export const requestFeedback = async (feedbackRequest: FeedbackRequest): Promise<AxiosResponse<any>> => {
    const formData = new FormData();
    formData.append("feedback", feedbackRequest.feedback);
    formData.append("codeReport", feedbackRequest.codeReport);
    formData.append("isRight", feedbackRequest.isRight ? "true" : "false");
    formData.append("isRight", feedbackRequest.isRight ? "true" : "false");

    const sectionErrors = feedbackRequest.sectionError
        .split(",") 
        .map(value => value.trim()) 
        .map(value => sectionErrorEnum[value as SectionErrorKey] || value); 

    sectionErrors.forEach(error => formData.append("sectionError", error));

    return instance.post("feedback", formData, {
        headers: {
            'Content-Type': 'multipart/form-data;'
        }
    });
};

