package org.diagnostic.domain.entities.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.diagnostic.domain.enums.Section;
import org.diagnostic.presentation.dto.feedback.Feedback;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "feedbacks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEntity {

    private String feedbackComment;

    private Boolean isRight;

    private Section section;

    private String userId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String codeReport;


    public Feedback toDomain(){
        return new Feedback(
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
