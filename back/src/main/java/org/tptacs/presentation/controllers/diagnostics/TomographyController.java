package org.tptacs.presentation.controllers.diagnostics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tptacs.application.useCases.CreateTomographyUC;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.presentation.controllers.BaseController;
import org.tptacs.presentation.requestModels.TomographyRequest;

@RestController
@RequestMapping("/api/tomographies")
public class TomographyController extends BaseController {

    @Autowired
    private CreateTomographyUC tomographyService;

    @PostMapping("/")
    public ResponseEntity<String> saveTomography(@RequestBody TomographyRequest request) {
        Tomography tomography = new Tomography();
        tomography.setTomography(request.getTomography());
        tomography.setTitle(request.getTitle());
        tomography.setUserId(this.getUserFromJwt().getId());
        String codeReport = tomographyService.saveTomography(tomography);
        return new ResponseEntity<>(codeReport, HttpStatus.CREATED);
    }

    @GetMapping("/report/{codeReport}")
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
