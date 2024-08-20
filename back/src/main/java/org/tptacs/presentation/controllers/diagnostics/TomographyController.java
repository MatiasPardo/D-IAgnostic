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
import org.tptacs.application.useCases.DeleteTomographyUC;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.presentation.controllers.BaseController;
import org.tptacs.presentation.responseModels.ReportResponse;
import org.tptacs.presentation.responseModels.Response;
import org.tptacs.presentation.responseModels.TomographyResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/tomographies")
@Tag(name = "Tomographies", description = "Endpoints for managing tomographies")
public class TomographyController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TomographyController.class);

    @Autowired
    private CreateTomographyUC tomographyService;

    @Autowired
    private DeleteTomographyUC deleteTomographyUC;

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
    public ResponseEntity<Response> saveTomography(
            @RequestParam("tomography") MultipartFile tomographyByte,
            @RequestParam("title") String title) throws IOException {

        Tomography tomography = new Tomography();

        if (tomographyByte.getBytes().length == 0 || title == null || title.isEmpty()) {
            return new ResponseEntity<>(new Response("1001","El archivo y el título son requeridos", LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        tomography.setCreateDate(LocalDateTime.now());
        tomography.setTomography(tomographyByte.getBytes());
        tomography.setTitle(title);
        tomography.setUserId(this.getUserFromJwt().getId());

        String codeReport = tomographyService.saveTomography(tomography);
        categorizeAndGenerateReportUC.categorizeAndGenerateReport(tomography);

        return ResponseEntity.ok(new ReportResponse(codeReport, "200", "Consulta exitosa"));
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
    public ResponseEntity<Response> getTomographyStatus(
            @PathVariable String codeReport,
            @RequestHeader("Authorization") String jwtToken) {

        logger.info("Consulta de informe para tomografia con codigo: {} - START",codeReport);
        String userId = this.getUserFromJwt().getId();
        Tomography tomography = tomographyService.getTomographyStatus(codeReport);
        if(!tomography.getUserId().equals(userId)){
            return new ResponseEntity<>(new Response("1005","La tomografia no corresponde con el usuario",LocalDateTime.now()), HttpStatus.OK);
        }
        logger.info("Consulta de informe - END");

        if (tomography != null) {
            return new ResponseEntity<>(tomography, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get paginated tomographies", description = "Gets paginated tomographies for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tomographies found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TomographyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No tomographies found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class)))
    })
    @GetMapping( produces = "application/json")
    public ResponseEntity<TomographyResponse> getTomography(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(value = "page",required = false) Integer page,
            @RequestParam(value = "size",required = false) Integer size) {

        logger.info("Consulta de tomografias paginadas - START");
        String userId = this.getUserFromJwt().getId();
        List<Tomography> tomographyPage = List.of();
        if(page != null && size != null){
            tomographyPage = tomographyService.getTomography(userId, page, size).getContent();
        }
        tomographyPage = tomographyService.getTomography(userId);

        logger.info("Consulta de tomografias paginadas - END");

        if (!tomographyPage.isEmpty()) {
            return new ResponseEntity<>(new TomographyResponse(tomographyPage, Boolean.TRUE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new TomographyResponse(Boolean.FALSE, "No se encontraron tomografias para ese usuario"), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get paginated tomographies", description = "Gets paginated tomographies for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tomographies found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TomographyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No tomographies found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping(path = "/{codeReport}", produces = "application/json")
    public ResponseEntity<Response> deleteTomography(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable String codeReport) {

        logger.info("Eliminacion de tomografias - START");
        deleteTomographyUC.deleteTomography(codeReport, this.getUserFromJwt().getId());
        logger.info("Eliminacion de tomografias - END");
        return new ResponseEntity<>(new Response(), HttpStatus.OK);

    }
}
