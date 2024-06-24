package org.example.entities;

import lombok.Getter;
import lombok.Setter;
import org.example.steps.Step;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private Long telegramUserId;
    @Setter
    private String userId;
    @Setter
    private String jwt;
    private List<Step> steps;

    public User(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
        this.steps = new ArrayList<>();
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public Step getLastStep() {
        var lastIndex = steps.size() - 1;
        if (lastIndex < 0) return null;

        return this.steps.get(lastIndex);
    }


}
