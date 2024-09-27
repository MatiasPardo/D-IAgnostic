import { AlertError, AlertOk } from "../components/SweetAlert"
import { requestFeedback } from "../services/FeedbackService"
import { FeedbackRequest } from "../interfaces/FeedbackRequest";

export const UseFeedback = () => {
    
    const handleSendFeedback = async (feedbackRequest: FeedbackRequest) => {
      try {
          const response = await requestFeedback(feedbackRequest);
          if (response.data) {
              AlertOk('Feedback', 'El Feedback se ha enviado correctamente.');
              return response;
          } else {
              AlertError('Feedback', 'No se recibió el código de informe.', 'error');
              throw new Error('Missing codeReport');
          }
      } catch (error) {
          AlertError('Feedback', 'Ocurrió un error al enviar el Feedback.', 'error');
          throw error; 
      }
  };
  return {
    handleSendFeedback
  }
}
