package org.diagnostic.presentation.controllers.diagnostics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.diagnostic.application.useCases.CreateFeedbackUC;
import org.diagnostic.application.useCases.GetFeedbackUC;
import org.diagnostic.domain.enums.Section;
import org.diagnostic.presentation.controllers.BaseController;
import org.diagnostic.presentation.dto.feedback.Feedback;
import org.diagnostic.presentation.responseModels.FeedbackResponse;
import org.diagnostic.presentation.responseModels.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Feedback", description = "Endpoints for managing feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private CreateFeedbackUC createFeedbackUC;

    @Autowired
    private GetFeedbackUC getFeedbackUC;

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
            @RequestParam(value = "sectionError") List<Section> section) throws IOException {

        createFeedbackUC.saveFeedback(new Feedback(feedback,isRight,section, this.getUserFromJwt().getId(), codeReport));

        return ResponseEntity.ok(new Response("200", "Creacion exitosa", LocalDateTime.now()));
    }

    @Operation(summary = "Get a feedback", description = "Get a feedback for codeReport")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback get successfully ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @GetMapping(path = "/{codeReport}", produces = "application/json")
    public ResponseEntity<FeedbackResponse> getFeedback(
            @PathVariable String codeReport) {
        return ResponseEntity.ok(new FeedbackResponse(getFeedbackUC.get(codeReport)));
    }

}
