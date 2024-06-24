package org.example.events;

import lombok.SneakyThrows;
import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.client.HttpClient;
import org.example.client.requests.LoginRequest;
import org.example.client.requests.RegisterRequest;
import org.example.entities.User;
import org.example.events.base.EventHandler;
import org.example.exceptions.RestException;
import org.example.repositories.UserRepository;
import org.example.steps.InitStep;

public class RegisterUserEventHandler extends EventHandler<RegisterUserEvent> {
    @Override
    @SneakyThrows
    public void onEvent(RegisterUserEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var client = new HttpClient();
        var registerRequest = new RegisterRequest(event.getUser(), event.getMail(), event.getPassword());
        var user = UserRepository.get(event.getTelegramUser());

        try {
            var response = client.register(registerRequest);
            user.setUserId(response.getUserId());
            UserRepository.saveOrUpdate(user);
            bot.sendText(event.getTelegramUser(), "Registro correcto - userId: " + response.getUserId());
        } catch (RestException e) {
            bot.sendText(event.getTelegramUser(), "Error al registarse: " + e.getMessage());
        } catch (Exception e) {
            bot.sendText(event.getTelegramUser(), "Ocurrió un error inesperado al intentar registrarse");
        }

        String message = user.getLastStep().logoutUser(event.getTelegramUser()).getMessage();
        bot.sendText(event.getTelegramUser(), message);
    }

    @Override
    public Class getEventType() {
        return RegisterUserEvent.class;
    }
}
