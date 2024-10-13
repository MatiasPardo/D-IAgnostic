package org.example.steps;

import org.example.events.SearchReportEvent;
import org.example.events.base.EventPublisher;
import org.example.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SearchReportStep extends Step{
    @Override
    public String getMessage() {
        return "Opcion en progreso";
    }

    @Override
    public String executeStep(Update update) {
        var event = new SearchReportEvent(update.getMessage().getFrom().getId());
        var user = UserRepository.get(event.getUserId());
        user.setCodeReport(update.getMessage().getText());
        EventPublisher.publish(event);
        return "Buscando informe..";
    }
}
