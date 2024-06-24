package org.example.events;

import lombok.SneakyThrows;
import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.client.HttpClient;
import org.example.client.requests.LoginRequest;
import org.example.client.responses.LoginResponse;
import org.example.events.base.EventHandler;
import org.example.exceptions.RestException;
import org.example.repositories.UserRepository;
import org.example.steps.InitStep;

public class LoginUserEventHandler extends EventHandler<LoginUserEvent> {
    @Override
    @SneakyThrows
    public void onEvent(LoginUserEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var user = UserRepository.get(event.getUserId());
        var client = new HttpClient();
        var loginRequest = new LoginRequest(event.getUserName(), event.getPassword());

        String message;
        try {
            var response = client.login(loginRequest);
            user.setJwt(response.getToken());
            bot.sendText(event.getUserId(), "Login exitoso - token: " + response.getToken());
            message = user.getLastStep().resetStep(event.getUserId()).getMessage();
            bot.sendText(event.getUserId(), message);
        } catch (RestException e) {
            bot.sendText(event.getUserId(), "Error al iniciar sesión: " + e.getMessage());
            bot.sendText(event.getUserId(), user.getLastStep().logoutUser(event.getUserId()).getMessage());
        } catch (Exception e) {
            bot.sendText(event.getUserId(), "Ocurrió un error inesperado al intentar iniciar sesión");
            bot.sendText(event.getUserId(), user.getLastStep().logoutUser(event.getUserId()).getMessage());
        }
    }

    @Override
    public Class getEventType() {
        return LoginUserEvent.class;
    }
}
