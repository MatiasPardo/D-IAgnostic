package org.example.events;

import lombok.Getter;
import org.example.events.base.Event;

@Getter
public class ViewOrdersEvent extends Event {
    private Long userId;

    public ViewOrdersEvent(Long userId) {
        this.userId = userId;
    }
}
