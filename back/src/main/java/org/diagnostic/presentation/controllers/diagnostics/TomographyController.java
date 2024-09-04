package org.diagnostic.presentation.controllers.diagnostics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.diagnostic.application.useCases.CategorizeAndGenerateReportUC;
import org.diagnostic.application.useCases.CreateTomographyUC;
import org.diagnostic.application.useCases.DeleteTomographyUC;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.domain.exceptions.NotFoundTomographyException;
import org.diagnostic.presentation.controllers.BaseController;
import org.diagnostic.presentation.responseModels.ReportResponse;
import org.diagnostic.presentation.responseModels.Response;
import org.diagnostic.presentation.responseModels.TomographyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


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
            @RequestParam(value = "tomography") MultipartFile tomographyByte,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "codeReport",required = false) String codeReport,
            @RequestParam(value = "lastImage",required = false) Boolean lastImage) throws IOException {
        Tomography tomography = new Tomography();

        if (tomographyByte.getBytes().length == 0 || title == null || title.isEmpty()) {
            return new ResponseEntity<>(new Response("1001","El archivo y el título son requeridos", LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        tomography.setCreateDate(LocalDateTime.now());
        tomography.setTitle(title);
        tomography.setUserId(this.getUserFromJwt().getId());

        if(codeReport != null){
            tomography = tomographyService.getTomographyStatus(codeReport);
            tomography.setTomography(tomographyByte.getBytes());
            tomographyService.saveUrl(tomography,tomographyService.uploadFile(UUID.randomUUID().toString() + tomography.getTomographyDetail().size(),tomography.getTomography()), codeReport);
        }else{
            tomography = tomographyService.saveAndGenerateUrlTomography(tomography, tomographyByte.getBytes());
            codeReport = tomography.getCodeReport();
        }

        if(lastImage != null && lastImage.equals(Boolean.TRUE)){
            categorizeAndGenerateReportUC.categorizeAndGenerateReport(tomography);
        }


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
            return new ResponseEntity<>(tomography.dto(), HttpStatus.OK);
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
        long pageTotal = 0;
        long sizeTotal = 0;
        if(page != null && size != null){
            Page<Tomography> tomography = tomographyService.getTomography(userId, page, size);
            pageTotal = tomography.getPageable().getPageSize();
            sizeTotal = tomography.getTotalElements();
            tomographyPage = tomography.getContent();
        }else {
            tomographyPage = tomographyService.getTomography(userId);
        }

        logger.info("Consulta de tomografias paginadas - END");

        if (!tomographyPage.isEmpty()) {
            return new ResponseEntity<>(new TomographyResponse(tomographyPage, Boolean.TRUE, pageTotal, sizeTotal), HttpStatus.OK);
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
        String userId = this.getUserFromJwt().getId();
        logger.info("Borrando tomografia con codigo de reporte: {} del usuario: {}", codeReport, userId);
        try{
            deleteTomographyUC.deleteTomography(codeReport, userId);
        }catch(NotFoundTomographyException e){
            return new ResponseEntity<>(new Response("404","Error al eliminar la imagen solicitada",LocalDateTime.now()), HttpStatus.BAD_REQUEST);

        }
        logger.info("Eliminacion de tomografias - END");
        return new ResponseEntity<>(new Response(), HttpStatus.OK);

    }
}
