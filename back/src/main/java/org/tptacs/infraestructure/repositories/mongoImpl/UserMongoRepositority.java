package org.tptacs.infraestructure.repositories.mongoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.tptacs.domain.entities.User;
import org.tptacs.infraestructure.repositories.interfaces.IUserRepository;
import org.tptacs.infraestructure.repositories.mongoImpl.interfaces.IUserMongoRepository;

import java.util.Optional;

@Repository
@Primary
public class UserMongoRepositority implements IUserRepository {
    @Autowired
    private IUserMongoRepository IUserMongoRepository;

    // Mejorar este método para no traerse todos los objetos y filtrar a memoria
    @Override
    public Optional<User> findByUsername(String userName) {
        return IUserMongoRepository.findAll()
                .stream().filter(v -> v.getUsername().equals(userName))
                .findFirst();
    }

    // Mejorar este método para no traerse todos los objetos y filtrar a memoria
    @Override
    public Optional<User> findByEmail(String email) {
        return IUserMongoRepository.findAll()
                .stream().filter(v -> v.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(User user) {
        IUserMongoRepository.save(user);
    }

	@Override
	public Long countUserUnique() {
		return IUserMongoRepository.count();
	}

	public void update(User user) {
		IUserMongoRepository.save(user);
	}
}
