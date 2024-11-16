package org.example.events;

import lombok.extern.slf4j.Slf4j;
import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.client.HttpClient;
import org.example.client.responses.TomograpiesResponse;
import org.example.entities.Tomography;
import org.example.events.base.EventHandler;
import org.example.exceptions.AuthException;
import org.example.exceptions.RestException;
import org.example.messages.MessageBuilder;
import org.example.repositories.UserRepository;

@Slf4j
public class SearchReportEventHandler extends EventHandler<SearchReportEvent> {
    @Override
    public void onEvent(SearchReportEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var user = UserRepository.get(event.getUserId());

        var client = new HttpClient();
        try{
            var response = client.searchReport(user.getCodeReport(), user.getJwt());
            bot.sendText(event.getUserId(), tomographyToMessage(response));
        } catch (RestException e) {
            bot.sendText(event.getUserId(), "Error al intentar obtener el informe " + e.getMessage());
        } catch(AuthException e) {
            bot.sendText(event.getUserId(), e.getMessage());
            String message = user.getLastStep().logoutUser(event.getUserId()).getMessage();
            bot.sendText(event.getUserId(), message);
            return;
        }catch (Exception e) {
            bot.sendText(event.getUserId(), "Ocurrió un error inesperado al intentar obtener los informes");
            log.error(e.getStackTrace().toString());
        }

        String message = user.getLastStep().resetStep(event.getUserId()).getMessage();
        bot.sendText(event.getUserId(), message);
    }

    @Override
    public Class getEventType() {
        return SearchReportEvent.class;
    }

    private String tomographyToMessage(Tomography r) {
        if (r == null) return "No tienes informes solicitados";
        System.out.println("Hay response de informe: " + r);
        var messageBuilder = MessageBuilder.builder();
        //UserRepository.get(this.userId)

        messageBuilder
                .withLine(r.getReport() != null ? "Informe: " + r.getReport():r.getStatusReport().equals(Tomography.StatusReport.NO_CORRESPONDE_INFORME)? Tomography.StatusReport.NO_CORRESPONDE_INFORME.getDesc():" Informe aun no generado");

        return messageBuilder.build();
    }
}
