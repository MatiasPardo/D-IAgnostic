package org.example.bot;

import org.example.entities.User;
import org.example.exceptions.InvalidOptionException;
import org.example.repositories.UserRepository;
import org.example.steps.InitStep;
import org.example.steps.LoginStep;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "TACS_G1_BOT";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_KEY"); //"6836802718:AAEWae_ZBwSzX2Qc0GeSpDCbnWseI2fH7Uk";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        analyzeText(user.getId(), update);

        System.out.println(user.getFirstName() + " wrote " + msg.getText() );
    }

    private void analyzeText(Long who, Update update){
        var user = UserRepository.get(who);
        if (user == null) {
            user = new User(who);
            user.addStep(new LoginStep());
            UserRepository.saveOrUpdate(user);
        }

        try {
            sendText(who, user.getLastStep().executeStep(update));
        } catch (InvalidOptionException e) {
            sendText(who, e.getMessage());
        }

    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
