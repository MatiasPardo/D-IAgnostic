package org.example;

import org.example.bot.Bot;
import org.example.bot.SingletonBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        var bot = new Bot();
        SingletonBot.singletonBot.put("bot", bot);
        botsApi.registerBot(bot);
    }
}