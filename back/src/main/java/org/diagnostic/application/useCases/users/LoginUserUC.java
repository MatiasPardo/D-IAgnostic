package org.diagnostic.application.useCases.users;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.diagnostic.application.security.JwtUtils;
import org.diagnostic.domain.entities.User;
import org.diagnostic.domain.enums.Rol;
import org.diagnostic.infraestructure.repositories.interfaces.IUserRepository;
import org.diagnostic.presentation.requestModels.LoginRequest;
import org.diagnostic.presentation.responseModels.LoginResponse;
import java.util.Objects;

@Service
public class LoginUserUC {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        Rol rol = Rol.valueOf(Objects.requireNonNull(userDetails.getAuthorities().stream().findFirst().orElse(null)).getAuthority());

        return new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                rol);
    }
}
