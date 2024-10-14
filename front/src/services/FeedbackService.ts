import { instance } from "./BaseClient";
import { FeedbackRequest } from "../interfaces/FeedbackRequest";
import { AxiosResponse } from "axios";

type SectionErrorKey = "Composición" | "Tamaño" | "Reporte" | "Otro";

const sectionErrorEnum = {
    "Composición": "COMPOSITION",
    "Tamaño": "SIZE",
    "Reporte": "REPORT",
    "Otro": "OTRO"
};

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

