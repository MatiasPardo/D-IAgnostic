package org.example.events.base;

import java.util.EventListener;
import java.util.ServiceLoader;

public abstract class EventHandler<E extends Event> implements EventListener {
        public static EventHandler getHandler(Class type) {
        var handlers = ServiceLoader.load(EventHandler.class);
        var handler = handlers.stream().filter(h -> h.get().getEventType().equals(type)).findFirst();
        if (handler.isEmpty()) throw new RuntimeException(String.format("handler not found for event '%s'", type.toString()));
        return handler.get().get();
    }

    public abstract void onEvent(E event);
    public abstract Class getEventType();
}
