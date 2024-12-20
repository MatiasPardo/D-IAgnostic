package org.example.steps;

import org.example.events.LogoutUserEvent;
import org.example.events.SearchReportEvent;
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
                return "Buscando tus solicitudes...";
            }
            case "2" -> {
                var createProductStep = createProductStep(update);

                return createProductStep.executeStep(update);
            }
            case "3" -> {
                var createSearchReportStep = createSearchReportStep(update);
                return "Ingrese el codigo del informe para poder visualizarlo";
            }
            case "4" -> {
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
                .withLine("1. Ver todos mis solicitudes")
                .withLine("2. Solicitar un nuevo informe")
                .withLine("3. Ver un informe de una tomografia")
                .withLine("4. Cerrar sesión")
                .build();
    }

    private Step createProductStep(Update update) {
        var createProductStep = new CreateTomographyStep(null);
        var user = UserRepository.get(update.getMessage().getFrom().getId());
        user.addStep(createProductStep);
        UserRepository.saveOrUpdate(user);
        return createProductStep;
    }

    private Step createSearchReportStep(Update update) {
        var searchReport = new SearchReportStep();
        var user = UserRepository.get(update.getMessage().getFrom().getId());
        user.addStep(searchReport);
        UserRepository.saveOrUpdate(user);
        return searchReport;
    }
}


