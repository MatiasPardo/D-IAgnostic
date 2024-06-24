package org.example.repositories;

import org.example.entities.User;

import java.util.HashMap;

public class UserRepository {
    private static HashMap<Long, User> repository = new HashMap<>();

    public static User get(Long userId) {
        return repository.get(userId);
    }

    public static void saveOrUpdate(User user) {
        repository.put(user.getTelegramUserId(), user);
    }
}
