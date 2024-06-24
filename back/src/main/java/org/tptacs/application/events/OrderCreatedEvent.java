package org.tptacs.application.events;

import org.springframework.context.ApplicationEvent;

public class OrderCreatedEvent extends ApplicationEvent {
    public OrderCreatedEvent(Object source) {
        super(source);
    }
}
