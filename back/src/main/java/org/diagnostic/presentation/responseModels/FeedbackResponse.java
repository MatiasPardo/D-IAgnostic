package org.diagnostic.presentation.responseModels;

import lombok.Data;
import org.diagnostic.domain.enums.Section;
import org.diagnostic.presentation.dto.feedback.Feedback;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedbackResponse extends Response{

    private String feedbackComment;

    private Boolean isRight;

    private List<Section> section;

    private String userId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String codeReport;

    public FeedbackResponse(Feedback feedback) {
        this.feedbackComment = feedback.getFeedbackComment();
        this.codeReport = feedback.getCodeReport();
        this.isRight = feedback.getIsRight();
        this.createdDate = feedback.getCreatedDate();
        this.section = feedback.getSection();
        this.userId = feedback.getUserId();
        this.updatedDate = feedback.getUpdatedDate();
    }
}
