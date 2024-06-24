package org.example.events;

import lombok.Getter;
import org.example.events.base.Event;

@Getter
public class RegisterUserEvent extends Event {
    private Long telegramUser;
    private String user;
    private String mail;
    private String password;

    public RegisterUserEvent(Long telegramUser, String user, String mail, String password) {
        this.telegramUser = telegramUser;
        this.user = user;
        this.mail = mail;
        this.password = password;
    }
}
