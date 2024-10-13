import { instance } from "./BaseClient";
import { FeedbackRequest } from "../interfaces/FeedbackRequest";
import { AxiosResponse } from "axios";


export const requestFeedback = async (feedbackRequest: FeedbackRequest): Promise<AxiosResponse<any>> => {
    const formData = new FormData();
    formData.append("feedback", feedbackRequest.feedback);
    formData.append("codeReport", feedbackRequest.codeReport);
    formData.append("isRight", feedbackRequest.isRight ? "true" : "false");
    formData.append("sectionError", feedbackRequest.sectionError);
    return instance.post("feedback", formData, {
        headers: {
            'Content-Type': 'multipart/form-data;'
        }
    });
    
};
