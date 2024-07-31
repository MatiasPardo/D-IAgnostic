package org.tptacs.presentation.controllers.diagnostics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tptacs.application.useCases.CreateTomographyUC;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.presentation.requestModels.TomographyRequest;

@RestController
@RequestMapping("/api/tomographies")
public class TomographyController {

    @Autowired
    private CreateTomographyUC tomographyService;

    @PostMapping("/")
    public ResponseEntity<String> saveTomography(@RequestBody TomographyRequest request) {
        Tomography tomography = new Tomography();
        tomography.setTomography(request.getTomography());
        tomography.setTitle(request.getTitle());
        tomography.setUserId(request.getUserId());
        String codeReport = tomographyService.saveTomography(tomography);
        return new ResponseEntity<>(codeReport, HttpStatus.CREATED);
    }

    @GetMapping("/Report/{codeReport}")
    public ResponseEntity<Tomography> getTomographyStatus(
            @PathVariable String codeReport,
            @RequestHeader("Authorization") String jwtToken) {

        // Extract userId from JWT token
        String userId = extractUserIdFromJwt(jwtToken);

        Tomography tomography = tomographyService.getTomographyStatus(codeReport, userId);
        if (tomography != null) {
            return new ResponseEntity<>(tomography, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private String extractUserIdFromJwt(String jwtToken) {
        // Implement JWT token parsing to extract userId
        return "parsedUserId";
    }
}
