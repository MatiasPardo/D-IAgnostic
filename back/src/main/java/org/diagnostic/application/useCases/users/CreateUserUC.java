package org.diagnostic.application.useCases.users;

import org.diagnostic.application.events.UserCreatedEvent;
import org.diagnostic.domain.entities.User;
import org.diagnostic.domain.exceptions.RegistrationException;
import org.diagnostic.infraestructure.repositories.interfaces.IUserRepository;
import org.diagnostic.presentation.requestModels.CreateUserRequest;
import org.diagnostic.presentation.responseModels.ResponseCreateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUserUC {
    private final IUserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ApplicationEventPublisher publisher;

    public CreateUserUC(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseCreateUser createUser(CreateUserRequest createUserRequest) {
        var userNameOptional = this.userRepository.findByUsername(createUserRequest.getUserName());
        var emailOptional = this.userRepository.findByEmail(createUserRequest.getEmail());

        if (userNameOptional.isPresent()) throw new RegistrationException("user already exists");
        if (emailOptional.isPresent()) throw new RegistrationException("email already exists");

        User user = new User(UUID.randomUUID().toString(), createUserRequest.getUserName(),
                createUserRequest.getEmail(),
                encoder.encode(createUserRequest.getPassword()));

        this.userRepository.save(user);

        var userCreatedEvent = new UserCreatedEvent(this);
        this.publisher.publishEvent(userCreatedEvent);

        return new ResponseCreateUser(user.getId());
    }

    public void ressetPassward(String newPassword, String username) {
        User user = this.userRepository.findByUsername(username).get();
        user.newPassword(encoder.encode(newPassword));
        this.userRepository.update(user);
    }
}
