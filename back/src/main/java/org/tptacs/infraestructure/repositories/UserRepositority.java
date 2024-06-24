package org.tptacs.infraestructure.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.tptacs.domain.entities.User;
import org.tptacs.infraestructure.repositories.interfaces.IUserRepository;

@Repository
public class UserRepositority extends FileRepository<User> implements IUserRepository {
    public UserRepositority() {
        super(UserRepositority.class.getSimpleName(), User.class);
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        return values()
                .stream().filter(v -> v.getUsername().equals(userName))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return values()
                .stream().filter(v -> v.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(User user) {
        super.put(user.getId(), user);
    }

	@Override
	public Long countUserUnique() {
		return (long) values().size();
	}

	public void update(User user) {
		super.replace(user.getId(), user);
	}

}
