package org.example.events;

import lombok.Getter;
import org.example.events.base.Event;

@Getter
public class CreateOrderEvent extends Event {
    private String nameOrId;
    private boolean shared;
    private Long userId;

    public CreateOrderEvent(String nameOrId, boolean shared, Long userId) {
        this.nameOrId = nameOrId;
        this.shared = shared;
        this.userId = userId;
    }
}
