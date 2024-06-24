package org.example.events;

import lombok.SneakyThrows;
import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.example.client.HttpClient;
import org.example.client.requests.CreateOrderRequest;
import org.example.client.responses.CreateOrderResponse;
import org.example.events.base.EventHandler;
import org.example.exceptions.AuthException;
import org.example.exceptions.RestException;
import org.example.repositories.UserRepository;

public class CreateOrderEventHandler extends EventHandler<CreateOrderEvent> {
    @Override
    @SneakyThrows
    public void onEvent(CreateOrderEvent event) {
        Bot bot = SingletonBot.singletonBot.get("bot");
        var user = UserRepository.get(event.getUserId());

        var client = new HttpClient();
        var createOrderRequest = new CreateOrderRequest(event.getNameOrId());

        try {
            CreateOrderResponse response;
            if (event.isShared()) {
                response = client.shareOrder(event.getNameOrId(), user.getJwt());
            } else {
                response = client.createOrder(createOrderRequest, user.getJwt());
            }
            var message = event.isShared()
                    ? String.format("Fuiste agregado al pedido '%s' con id '%s'", response.getName(), response.getOrderId())
                    :  String.format("Se creó el pedido '%s' con id '%s'", event.getNameOrId(), response.getOrderId());

            bot.sendText(event.getUserId(), message);
        } catch (RestException e) {
            bot.sendText(event.getUserId(), "Error al intentar crear el pedido " + e.getMessage());
        } catch(AuthException e) {
            bot.sendText(event.getUserId(), e.getMessage());
            String message = user.getLastStep().logoutUser(event.getUserId()).getMessage();
            bot.sendText(event.getUserId(), message);
            return;
        }
        catch (Exception e) {
            bot.sendText(event.getUserId(), "Ocurrió un error inesperado al intentar crear el pedido");
        }

        String message = user.getLastStep().resetStep(event.getUserId()).getMessage();
        bot.sendText(event.getUserId(), message);
    }

    @Override
    public Class getEventType() {
        return CreateOrderEvent.class;
    }
}
