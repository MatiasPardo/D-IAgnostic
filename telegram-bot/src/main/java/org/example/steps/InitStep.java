package org.example.steps;

import org.example.events.LogoutUserEvent;
import org.example.events.ViewOrdersEvent;
import org.example.events.base.EventPublisher;
import org.example.messages.MessageBuilder;
import org.example.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InitStep extends Step {
    public String executeStep(Update update) {
        var option = update.getMessage().getText();
        switch (option) {
            case "1" -> {
                var event = new ViewOrdersEvent(update.getMessage().getFrom().getId());
                EventPublisher.publish(event);
                return "Buscando tus pedidos...";
            }
            case "2" -> {
                var createProductStep = createProductStep(update);
                return createProductStep.executeStep(update);
            }
            case "3" -> {
                var event = new LogoutUserEvent(update.getMessage().getFrom().getId());
                EventPublisher.publish(event);
                return "Cerrando sesión...";
            }
        }
        return getMessage();
    }
    @Override
    public String getMessage() {
        var messageBuilder = MessageBuilder.builder();
        return messageBuilder
                .withLine("Seleccioná una opción")
                .withLine("1. Ver todos mis pedidos")
                .withLine("2. Crear un pedido")
                .withLine("3. Cerrar sesión")
                .build();
    }

    private Step createProductStep(Update update) {
        var createProductStep = new CreateOrderStep();
        var user = UserRepository.get(update.getMessage().getFrom().getId());
        user.addStep(createProductStep);
        UserRepository.saveOrUpdate(user);
        return createProductStep;
    }
}
