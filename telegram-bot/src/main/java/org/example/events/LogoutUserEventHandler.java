package org.example.events;

import lombok.SneakyThrows;
import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.events.base.EventHandler;
import org.example.repositories.UserRepository;

public class LogoutUserEventHandler extends EventHandler<LogoutUserEvent> {
    @Override
    @SneakyThrows
    public void onEvent(LogoutUserEvent event) {
        Thread.sleep(500);
        Bot bot = SingletonBot.singletonBot.get("bot");
        Thread.sleep(500);
        bot.sendText(event.getUserId(), "Logout exitoso");
        var user = UserRepository.get(event.getUserId());
        var message = user.getLastStep().logoutUser(event.getUserId()).getMessage();
        bot.sendText(event.getUserId(), message);
    }

    @Override
    public Class getEventType() {
        return LogoutUserEvent.class;
    }
}
