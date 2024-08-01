package org.tptacs.presentation.controllers.diagnostics;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tptacs.application.security.AuthExceptionHandler;
import org.tptacs.application.useCases.CreateTomographyUC;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.presentation.controllers.BaseController;
import org.tptacs.presentation.requestModels.TomographyRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/tomographies")
public class TomographyController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TomographyController.class);

    @Autowired
    private CreateTomographyUC tomographyService;

    @PostMapping(produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveTomography(@RequestParam("tomography") MultipartFile tomographyByte,
                                                 @RequestParam("title") String title) throws IOException {
        logger.error("Reciiendo tomografia...");
        Tomography tomography = new Tomography();

        if (tomographyByte.getBytes().length == 0 || title == null || title.isEmpty()) {
            return new ResponseEntity<>("El archivo y el titulo son requeridos", HttpStatus.BAD_REQUEST);
        }
        tomography.setTomography(tomographyByte.getBytes());
        tomography.setTitle("Una tomografia");
        tomography.setUserId(this.getUserFromJwt().getId());
        logger.error("Usuario {}",this.getUserFromJwt().getUsername());
        String codeReport = tomographyService.saveTomography(tomography);
        logger.error("Fin proceso de recepcion de tomografia...");
        return new ResponseEntity<>(codeReport, HttpStatus.CREATED);
    }

    @GetMapping(path = "/report/{codeReport}", produces = "application/json")
    public ResponseEntity<Tomography> getTomographyStatus(
            @PathVariable String codeReport,
            @RequestHeader("Authorization") String jwtToken) {

        // Extract userId from JWT token
        String userId = this.getUserFromJwt().getId();

        Tomography tomography = tomographyService.getTomographyStatus(codeReport, userId);
        if (tomography != null) {
            return new ResponseEntity<>(tomography, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
