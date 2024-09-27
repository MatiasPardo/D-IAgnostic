import { instance } from "./BaseClient";
import { FeedbackRequest } from "../interfaces/FeedbackRequest";
import { AxiosResponse } from "axios";


export const requestFeedback = async (feedbackRequest: FeedbackRequest): Promise<AxiosResponse<any>> => {
    const formData = new FormData();
    formData.append("feedback", "pruebaaaa");
    formData.append("codeReport", "4e8aab13");
    formData.append("isRight", "false");
    formData.append("sectionError", "REPORT");

    return instance.post("feedback", formData, {
        headers: {
            'Content-Type': 'multipart/form-data;'
        }
    });
    
};
