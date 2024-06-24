package org.tptacs.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tptacs.application.useCases.GetAnalyticsUC;
import org.tptacs.presentation.responseModels.AnalyticsResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Analytics")
@RequestMapping(value = "/api/analytics", produces = "application/json")
public class AnalyticsController {
	
	private final GetAnalyticsUC getAnalyticsUC;
	
	
    public AnalyticsController(GetAnalyticsUC getAnalyticsUC) {
		this.getAnalyticsUC = getAnalyticsUC;
	}

    @GetMapping()
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        return ResponseEntity.ok(new AnalyticsResponse(getAnalyticsUC.countUsersUnique(), getAnalyticsUC.countOrders()));
    }

}
