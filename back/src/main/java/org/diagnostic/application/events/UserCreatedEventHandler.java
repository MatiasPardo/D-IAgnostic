package org.diagnostic.application.events;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.diagnostic.infraestructure.repositories.interfaces.IAnalyticsRepository;

@Service
public class UserCreatedEventHandler implements ApplicationListener<UserCreatedEvent> {
    IAnalyticsRepository analyticsRepository;

    public UserCreatedEventHandler(IAnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    @Async
    public void onApplicationEvent(UserCreatedEvent event) {
        analyticsRepository.addUser();
    }
}
