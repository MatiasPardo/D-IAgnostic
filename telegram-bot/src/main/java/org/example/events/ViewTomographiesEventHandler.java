package org.example.events;

import lombok.SneakyThrows;
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

public class ViewTomographiesEventHandler extends EventHandler<ViewOrdersEvent> {
    @Override
    @SneakyThrows
    public void onEvent(ViewOrdersEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var user = UserRepository.get(event.getUserId());

        var client = new HttpClient();

        try {
            var response = client.getTomographies(user.getJwt());
            System.out.println("Tenemos response de tomografias :" +response);
            bot.sendText(event.getUserId(), tomographyToMessage(response));
        } catch (RestException e) {
            bot.sendText(event.getUserId(), "Error al intentar obtener los informes " + e.getMessage());
        } catch(AuthException e) {
            bot.sendText(event.getUserId(), e.getMessage());
            String message = user.getLastStep().logoutUser(event.getUserId()).getMessage();
            bot.sendText(event.getUserId(), message);
            return;
        }
        catch (Exception e) {
            bot.sendText(event.getUserId(), "Ocurrió un error inesperado al intentar obtener los informes");
        }

        String message = user.getLastStep().resetStep(event.getUserId()).getMessage();
        bot.sendText(event.getUserId(), message);
    }

    @Override
    public Class getEventType() {
        return ViewOrdersEvent.class;
    }

/*    private String ordersToMessageDeprecated(TomograpiesResponse response) {
        if (response.getTomographies().isEmpty()) return "No tienes informes solicitados";

        var messageBuilder = MessageBuilder.builder();
        response.getTomographies().forEach(r -> {
            messageBuilder
                    .withLine("Titulo de la tomografia: " +  r.())
                    .withLine("Id: " + r.getOrderId())
                    .withLine("Estado: " + r.getStatus())
                    .withLine("------------------");
        });
        return messageBuilder.build();
    }*/

    private String tomographyToMessage(TomograpiesResponse response) {
        if (response.getTomographies().isEmpty()) return "No tienes informes solicitados";
        System.out.println("Hay response de tomografias: " + response);
        var messageBuilder = MessageBuilder.builder();
        response.getTomographies().forEach(r -> {
            messageBuilder
                    .withLine("Titulo de la tomografia: " +  r.getTitle())
                    .withLine("Estado la categoria dada por la IA: " + r.getCategory().getDesc())
                    .withLine("Estado del reporte: " + r.getStatusReport().getDesc())
                    .withLine("Fecha de solicitud: " + r.getCreateDate())
                    .withLine("Codigo del reporte (seguimiento):" + r.getCodeReport())
                    .withLine(r.getStatusReport().equals(Tomography.StatusReport.INFORME_GENERADO) ? "Reporte: "+r.getReport():"")
                    .withLine("------------------");
        });
        return messageBuilder.build();
    }
}
