import { instance } from "./BaseClient";
import { FeedbackRequest } from "../interfaces/FeedbackRequest";
import { AxiosResponse } from "axios";


export const requestFeedback = async (feedbackRequest: FeedbackRequest): Promise<AxiosResponse<{ codeReport: string }, any>> => {
    const formData = new FormData();
    formData.append("codeReport", feedbackRequest.codeReport);
    formData.append("isRight", feedbackRequest.isRight.toString());
    formData.append("feedback", feedbackRequest.feedback);
    formData.append("sectionError", feedbackRequest.sectionError); 
    console.log("codeReport in formData:", formData.get("codeReport"));
    return instance.post("feedback", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};