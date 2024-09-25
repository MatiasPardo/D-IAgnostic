package org.diagnostic.presentation.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.diagnostic.domain.entities.feedback.FeedbackEntity;
import org.diagnostic.domain.enums.Section;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    private String feedbackComment;

    private Boolean isRight;

    private Section section;

    private String userId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String codeReport;

    public Feedback(String feedback, Boolean isRight, Section section, String id, String codeReport) {
        this.feedbackComment = feedback;
        this.isRight = isRight;
        this.section = section;
        this.userId = id;
        this.codeReport = codeReport;
        this.createdDate = LocalDateTime.now();
    }

    public FeedbackEntity toEntity(){
        return new FeedbackEntity(
                this.feedbackComment,
                this.isRight,
                this.section,
                this.userId,
                this.createdDate,
                this.updatedDate,
                this.codeReport
        );
    }
}
