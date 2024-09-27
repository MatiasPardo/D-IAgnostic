package org.diagnostic.presentation.controllers.diagnostics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.diagnostic.application.useCases.CreateFeedbackUC;
import org.diagnostic.domain.enums.Section;
import org.diagnostic.presentation.controllers.BaseController;
import org.diagnostic.presentation.dto.feedback.Feedback;
import org.diagnostic.presentation.responseModels.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Feedback", description = "Endpoints for managing feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private CreateFeedbackUC createFeedbackUC;

    @Operation(summary = "Upload a new feedback", description = "Uploads a new feedback and positive or negative result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback successfully stored",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<Response> saveFeedback(
            @RequestParam(value = "feedback") String feedback,
            @RequestParam(value = "codeReport") String codeReport,
            @RequestParam(value = "isRight") Boolean isRight,
            @RequestParam(value = "sectionError") Section section) throws IOException {

        createFeedbackUC.saveFeedback(new Feedback(feedback,isRight,section, this.getUserFromJwt().getId(), codeReport));

        return ResponseEntity.ok(new Response("200", "Creacion exitosa", LocalDateTime.now()));
    }

}
