package org.example.events;

import lombok.Getter;
import org.example.events.base.Event;

@Getter
public class LogoutUserEvent extends Event {
    private Long userId;

    public LogoutUserEvent(Long userId) {
        this.userId = userId;
    }
}
