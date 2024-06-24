package org.example.events.base;

import java.util.concurrent.CompletableFuture;


public class EventPublisher {
    public static void publish(Event event) {
        var handler = EventHandler.getHandler(event.getClass());
        CompletableFuture.runAsync(() -> {
            handler.onEvent(event);
        });
    }
}
