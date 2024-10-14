package org.example.events;

import lombok.SneakyThrows;
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
            log.error(e.getStackTrace().toString());
        }

        String message = user.getLastStep().resetStep(event.getUserId()).getMessage();
        bot.sendText(event.getUserId(), message);
    }


    @Override
    public Class getEventType() {
        return ViewOrdersEvent.class;
    }


    private String tomographyToMessage(TomograpiesResponse response, Long userId) {
        if (response.getTomographies().isEmpty()) return "No tienes informes solicitados";
        System.out.println("Hay response de tomografias: " + response);
        var messageBuilder = MessageBuilder.builder();
        //UserRepository.get(this.userId)
        response.getTomographies().forEach(r -> {
            messageBuilder
                    .withLine("Titulo de la tomografia: " +  r.getTitle())
                    .withLine("Estado del informe: " + r.getStatusReport().getDesc())
                    .withLine("Fecha de solicitud: " + r.getCreatedDate())
                    .withLine("Codigo de Informe: " + r.getCodeReport())
                    .withLine("------------------");
        });
        return messageBuilder.build();
    }
}
