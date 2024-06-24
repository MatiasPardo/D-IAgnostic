package org.tptacs.application.events;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tptacs.infraestructure.repositories.interfaces.IAnalyticsRepository;

@Service
public class OrderCreatedEventHandler implements ApplicationListener<OrderCreatedEvent> {
    IAnalyticsRepository analyticsRepository;

    public OrderCreatedEventHandler(IAnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }
    @Override
    @Async
    public void onApplicationEvent(OrderCreatedEvent event) {
        analyticsRepository.addOrder();
    }

}
