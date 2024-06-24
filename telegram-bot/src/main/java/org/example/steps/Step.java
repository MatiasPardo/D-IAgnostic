package org.example.steps;

import org.example.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Step {
    public abstract String getMessage();
    public abstract String executeStep(Update update);

    public Step resetStep(Long userId) {
        var user = UserRepository.get(userId);
        user.addStep(new InitStep());
        UserRepository.saveOrUpdate(user);
        return user.getLastStep();
    }

    public Step logoutUser(Long userId) {
        var user = UserRepository.get(userId);
        user.addStep(new LoginStep());
        UserRepository.saveOrUpdate(user);
        return user.getLastStep();
    }
}
