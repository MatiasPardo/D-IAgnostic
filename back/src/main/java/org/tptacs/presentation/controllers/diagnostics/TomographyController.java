package org.tptacs.presentation.controllers.diagnostics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tptacs.application.useCases.CategorizeAndGenerateReportUC;
import org.tptacs.application.useCases.CreateTomographyUC;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.presentation.controllers.BaseController;
import org.tptacs.presentation.responseModels.TomographyResponse;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tomographies")
@Tag(name = "Tomographies", description = "Endpoints for managing tomographies")
public class TomographyController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TomographyController.class);

    @Autowired
    private CreateTomographyUC tomographyService;

    @Autowired
    private CategorizeAndGenerateReportUC categorizeAndGenerateReportUC;

    @Operation(summary = "Upload a new tomography", description = "Uploads a new tomography file and title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tomography successfully uploaded",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @PostMapping(produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveTomography(
            @RequestParam("tomography") MultipartFile tomographyByte,
            @RequestParam("title") String title) throws IOException {
        logger.info("Reciiendo tomografia...");
        Tomography tomography = new Tomography();

        if (tomographyByte.getBytes().length == 0 || title == null || title.isEmpty()) {
            return new ResponseEntity<>("El archivo y el titulo son requeridos", HttpStatus.BAD_REQUEST);
        }
        tomography.setTomography(tomographyByte.getBytes());
        tomography.setTitle(title);
        tomography.setUserId(this.getUserFromJwt().getId());
        logger.info("Usuario {}",this.getUserFromJwt().getUsername());
        String codeReport = tomographyService.saveTomography(tomography);
        categorizeAndGenerateReportUC.categorizeAndGenerateReport(tomography);
        logger.info("Fin proceso de recepcion de tomografia...");
        return new ResponseEntity<>(codeReport, HttpStatus.CREATED);
    }

    @Operation(summary = "Get tomography status", description = "Gets the status of a tomography by its code report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tomography found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tomography.class))),
            @ApiResponse(responseCode = "404", description = "Tomography not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)))
    })
    @GetMapping(path = "/report/{codeReport}", produces = "application/json")
    public ResponseEntity<Tomography> getTomographyStatus(
            @PathVariable String codeReport,
            @RequestHeader("Authorization") String jwtToken) {

        logger.info("Consulta de informe para tomografi con codigo: {} - START",codeReport);
        String userId = this.getUserFromJwt().getId();
        Tomography tomography = tomographyService.getTomographyStatus(codeReport, userId);
        logger.info("Consulta de informe - END");

        if (tomography != null) {
            return new ResponseEntity<>(tomography, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get tomographies", description = "Gets the status of a tomography by its code report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tomographies found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tomography.class))),
            @ApiResponse(responseCode = "404", description = "Tomography not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)))
    })
    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<TomographyResponse> getTomography(
            @RequestHeader("Authorization") String jwtToken) {
        logger.info("Consulta de tomografias - START");
        String userId = this.getUserFromJwt().getId();
        List<Tomography> tomography = tomographyService.getTomography(userId);
        logger.info("Consulta de tomografias - END");
        if (tomography != null) {
            return new ResponseEntity<>(new TomographyResponse(tomography,Boolean.TRUE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new TomographyResponse(Boolean.FALSE, "No se encontraron tomografias para ese usuario"), HttpStatus.NOT_FOUND);
        }
    }
}
