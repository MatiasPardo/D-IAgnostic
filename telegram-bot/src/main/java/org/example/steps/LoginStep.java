package org.example.steps;

import org.example.events.CreateOrderEvent;
import org.example.events.LoginUserEvent;
import org.example.events.RegisterUserEvent;
import org.example.events.base.EventPublisher;
import org.example.exceptions.InvalidOptionException;
import org.example.messages.MessageBuilder;
import org.example.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LoginStep extends Step {
    private String user;
    private String password;
    private int step = 1;

    @Override
    public String executeStep(Update update) {
        var userId = update.getMessage().getFrom().getId();
        var option = update.getMessage().getText().trim();
        if ("salir".equals(option)) {
            return logoutUser(userId).getMessage();
        }

        if (step == 1 && !option.equals("1") && !option.equals("2")) {
            return getMessage();
        }

        if (option.equals("2")) {
            var user = UserRepository.get(userId);
            var registerStep = new RegisterStep();
            user.addStep(registerStep);
            return registerStep.executeStep(update);
        }

        switch (step) {
            case 1 -> {
                step++;
            }
            case 2 -> {
                this.user = option;
                step++;
            }
            case 3 -> {
                this.password = option;
                resetStep(userId);
                var event = new LoginUserEvent(userId, this.user, this.password);
                EventPublisher.publish(event);
                step++;
            }
        }
        var message = getMessage();

        if (step > 4) {
            this.resetStep(userId);
            throw new InvalidOptionException("Opción no válida");
        }
        return message;
    }

    @Override
    public String getMessage() {
        switch (step) {
            case 1 -> {
                var message = MessageBuilder.builder();
                return message
                        .withLine("Debes iniciar sesión o registrarte para poder operar")
                        .withLine("1. Iniciar sesión")
                        .withLine("2. Registrarme")
                        .build();
            }
            case 2 -> {
                return  "Ingresá tu usuario, o escribí 'salir' para volver al menú principal: ";
            }
            case 3 -> {
                return "Ingresá tu password, o escribí 'salir' para volver al menú principal: ";
            }
            case 4 -> {
                return "Iniciando sesión...";
            }
        }
        throw new RuntimeException("Opción no válida");
    }

    public String validateAndGetOption(Update update) {
        var option = update.getMessage().getText().trim();
        if (!option.equals("1") && !option.equals("2")) {
            step--;
            throw new InvalidOptionException("Opción no válida");
        }
        return option;
    }
}
