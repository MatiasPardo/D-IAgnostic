package org.tptacs.application.events;

import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {
    public UserCreatedEvent(Object source) {
        super(source);
    }
}
