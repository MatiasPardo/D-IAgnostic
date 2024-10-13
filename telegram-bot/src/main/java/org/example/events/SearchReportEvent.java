package org.example.events;

import lombok.Getter;
import org.example.events.base.Event;

@Getter
public class SearchReportEvent extends Event {
    private Long userId;

    public SearchReportEvent(Long userId) {
        this.userId = userId;
    }

}