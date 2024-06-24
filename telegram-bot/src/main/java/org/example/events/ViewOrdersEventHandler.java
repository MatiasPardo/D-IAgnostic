package org.example.events;

import lombok.SneakyThrows;
import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.client.HttpClient;
import org.example.client.requests.CreateOrderRequest;
import org.example.client.responses.OrdersResponse;
import org.example.events.base.EventHandler;
import org.example.exceptions.AuthException;
import org.example.exceptions.RestException;
import org.example.messages.MessageBuilder;
import org.example.repositories.UserRepository;
import org.example.steps.InitStep;

public class ViewOrdersEventHandler extends EventHandler<ViewOrdersEvent> {
    @Override
    @SneakyThrows
    public void onEvent(ViewOrdersEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var user = UserRepository.get(event.getUserId());

        var client = new HttpClient();

        try {
            var response = client.getOrders(user.getJwt());
            bot.sendText(event.getUserId(), ordersToMessage(response));
        } catch (RestException e) {
            bot.sendText(event.getUserId(), "Error al intentar obtener los pedidos " + e.getMessage());
        } catch(AuthException e) {
            bot.sendText(event.getUserId(), e.getMessage());
            String message = user.getLastStep().logoutUser(event.getUserId()).getMessage();
            bot.sendText(event.getUserId(), message);
            return;
        }
        catch (Exception e) {
            bot.sendText(event.getUserId(), "Ocurrió un error inesperado al intentar obtener los pedidos");
        }

        String message = user.getLastStep().resetStep(event.getUserId()).getMessage();
        bot.sendText(event.getUserId(), message);
    }

    @Override
    public Class getEventType() {
        return ViewOrdersEvent.class;
    }

    private String ordersToMessage(OrdersResponse response) {
        if (response.getOrderDtos().isEmpty()) return "No tienes pedidos creados";

        var messageBuilder = MessageBuilder.builder();
        response.getOrderDtos().forEach(r -> {
            messageBuilder
                    .withLine("Nombre: " +  r.getName())
                    .withLine("Id: " + r.getOrderId())
                    .withLine("Estado: " + r.getStatus())
                    .withLine("------------------");
        });
        return messageBuilder.build();
    }
}
