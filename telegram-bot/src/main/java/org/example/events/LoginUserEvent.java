package org.example.events;

import lombok.Getter;
import org.example.events.base.Event;

@Getter
public class LoginUserEvent extends Event {
    private Long userId;
    private String userName;
    private String password;

    public LoginUserEvent(Long userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
}
