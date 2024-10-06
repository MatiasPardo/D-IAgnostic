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

    private static final int PAGE_SIZE = 15;

    @Override
    @SneakyThrows
    public void onEvent(ViewOrdersEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var user = UserRepository.get(event.getUserId());

        var client = new HttpClient();

        try {
            var response = client.getTomographies(user.getJwt());
            System.out.println("Tenemos response de tomografias :" +response);
            //sendTomographiesPage(response.getTomographies(), event.getUserId(), 0, bot);
            bot.sendText(event.getUserId(), tomographyToMessage(response, event.getUserId()));
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

    /*private void sendTomographiesPage(List<Tomography> tomographies, Long userId, int page, Bot bot) {
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, tomographies.size());

        // Si no hay más tomografías, notificar al usuario
        if (start >= tomographies.size()) {
            bot.sendText(userId, "No hay más tomografías.");
            return;
        }

        // Construir el mensaje para la página actual
        var messageBuilder = MessageBuilder.builder();
        tomographies.subList(start, end).forEach(r -> {
            messageBuilder
                    .withLine("Titulo de la tomografía: " + r.getTitle())
                    .withLine("Estado del informe: " + r.getStatusReport().getDesc())
                    .withLine("Fecha de solicitud: " + r.getCreateDate())
                    .withLine("Código del informe: " + r.getCodeReport())
                    .withLine(r.getStatusReport().equals(Tomography.StatusReport.INFORME_GENERADO) ? "Informe: " + r.getReport() : "")
                    .withLine("------------------");
        });

        bot.sendText(userId, messageBuilder.build());

        // Preguntar al usuario si desea ver más o cancelar
        bot.sendText(userId, "¿Deseas ver más tomografías? Responde con 'más' para continuar o 'cancelar' para detener.");

        // Esperar respuesta del usuario para ver más o cancelar
        // Aquí deberías tener lógica para escuchar la respuesta del usuario (esto depende de cómo manejes los eventos)
        // Supongamos que tienes un listener de eventos de mensaje:
        bot.waitForResponse(userId, response -> {
            if ("más".equalsIgnoreCase(response)) {
                sendTomographiesPage(tomographies, userId, page + 1, bot); // Mostrar la siguiente página
            } else {
                bot.sendText(userId, "Operación cancelada.");
            }
        });
    }*/

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

    private String tomographyToMessage(TomograpiesResponse response, Long userId) {
        if (response.getTomographies().isEmpty()) return "No tienes informes solicitados";
        System.out.println("Hay response de tomografias: " + response);
        var messageBuilder = MessageBuilder.builder();
        //UserRepository.get(this.userId)
        response.getTomographies().forEach(r -> {
            messageBuilder
                    .withLine("Titulo de la tomografia: " +  r.getTitle())
                    .withLine("Estado del informe: " + r.getStatusReport().getDesc())
                    .withLine("Fecha de solicitud: " + r.getCreateDate())
                    .withLine("Codigo del informe:" + r.getCodeReport())
                    .withLine(r.getStatusReport().equals(Tomography.StatusReport.INFORME_GENERADO) ? "Informe: "+r.getReport():"")
                    .withLine("------------------");
        });
        return messageBuilder.build();
    }
}
