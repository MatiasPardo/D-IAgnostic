package org.tptacs.application.useCases.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tptacs.application.events.UserCreatedEvent;
import org.tptacs.domain.entities.User;
import org.tptacs.domain.exceptions.RegistrationException;
import org.tptacs.infraestructure.repositories.interfaces.IUserRepository;
import org.tptacs.presentation.requestModels.CreateUserRequest;
import org.tptacs.presentation.responseModels.ResponseCreateUser;

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
}
