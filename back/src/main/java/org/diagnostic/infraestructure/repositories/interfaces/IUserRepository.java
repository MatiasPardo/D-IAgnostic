package org.diagnostic.infraestructure.repositories.interfaces;

import java.util.Optional;

import org.diagnostic.domain.entities.User;

public interface IUserRepository {
    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);
    void save(User user);
	Long countUserUnique();
	void update(User user);
}
