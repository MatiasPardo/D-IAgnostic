package org.example.steps;

import org.example.events.CreateOrderEvent;
import org.example.events.base.EventPublisher;
import org.example.exceptions.InvalidOptionException;
import org.example.messages.MessageBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CreateOrderStep extends Step {
    private int step = 1;
    private String nameOrId;
    private boolean sharedOrder;

    public String executeStep(Update update) {
        var userId = update.getMessage().getFrom().getId();

        switch (step) {
            case 1 -> {
                var message = getMessage();
                step++;
                return message;
            }
            case 2 -> {
                var value = validateAndGetOption(update);
                if (value.equals("0")) {
                    return resetStep(userId).getMessage();
                }
                this.sharedOrder = value.equals("2");
                var message = getMessage();
                step++;
                return message;
            }
            case 3 -> {
                this.nameOrId = update.getMessage().getText();
                this.resetStep(userId);
                var event = new CreateOrderEvent(nameOrId, sharedOrder, userId);
                EventPublisher.publish(event);
                var message = getMessage();
                step++;
                return message;
            }
        }
        if (step > 3) {
            this.resetStep(userId);
            throw new InvalidOptionException("Opción no válida");
        }
        return null;
    }

    @Override
    public String getMessage() {
        switch (step) {
            case 1 -> {
                var message = MessageBuilder.builder();
                return message
                        .withLine("¿Querés crear un pedido nuevo o agregarte a uno existente?")
                        .withLine("0. Volver al menú principal")
                        .withLine("1. Pedido nuevo")
                        .withLine("2. Pedido existente")
                        .build();
            }
            case 2 -> {
                return  sharedOrder ? "Ingresá el id del pedido" : "Ingresá el nombre para el pedido";
            }
            case 3 -> {
                return "Estamos procesado tu solicitud...";
            }
        }
        throw new RuntimeException("Opción no válida");
    }

    public String validateAndGetOption(Update update) {
        var option = update.getMessage().getText().trim();
        if (!option.equals("0") && !option.equals("1") && !option.equals("2")) {
            throw new InvalidOptionException("Opción no válida");
        }
        return option;
    }
}
