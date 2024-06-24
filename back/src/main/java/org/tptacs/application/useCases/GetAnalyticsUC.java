package org.tptacs.application.useCases;

import org.springframework.stereotype.Service;
import org.tptacs.infraestructure.repositories.interfaces.IAnalyticsRepository;

@Service
public class GetAnalyticsUC {
	
    private final IAnalyticsRepository analyticsRepository;

	public GetAnalyticsUC(IAnalyticsRepository analyticsRepository) {
		this.analyticsRepository = analyticsRepository;
	}
	
	public Long countOrders() {
		return analyticsRepository.countOrders();
	}
	
	public Long countUsersUnique() {
		return analyticsRepository.countUsers();
	}

	
}
