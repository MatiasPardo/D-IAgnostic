package org.example.steps;

import org.example.events.RegisterUserEvent;
import org.example.events.base.EventPublisher;
import org.example.exceptions.InvalidOptionException;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegisterStep extends Step {
    private String user;
    private String password;
    private String mail;
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

        switch (step) {
            case 1 -> {
                var message = getMessage();
                step++;
                return message;
            }
            case 2 -> {
                this.user = option;
                var message = getMessage();
                step++;
                return message;
            }
            case 3 -> {
                this.mail = option;
                var message = getMessage();
                step++;
                return message;
            }
            case 4 -> {
                this.password = option;
                resetStep(userId);
                var event = new RegisterUserEvent(userId, user, mail, password);
                EventPublisher.publish(event);
                var message = getMessage();
                step++;
                return message;
            }
        }

        if (step > 4) {
            this.resetStep(userId);
            throw new InvalidOptionException("Opción no válida");
        }
        return null;
    }

    @Override
    public String getMessage() {
        switch (step) {
            case 1 -> {
                return "Ingresá un usuario, o escribí 'salir' para volver al menú principal";
            }
            case 2 -> {
                return "Ingresá un email, o escribí 'salir' para volver al menú principal";
            }
            case 3 -> {
                return "Ingresá un password, o escribí 'salir' para volver al menú principal";
            }
            case 4 -> {
                return "Creando usuario...";
            }
        }
        throw new RuntimeException("Opción no válida");
    }

    public String validateAndGetOption(Update update) {
        var option = update.getMessage().getText().trim();
        if (!option.equals("0") && !option.equals("1") && !option.equals("2")) {
            step--;
            throw new InvalidOptionException("Opción no válida");
        }
        return option;
    }
}
