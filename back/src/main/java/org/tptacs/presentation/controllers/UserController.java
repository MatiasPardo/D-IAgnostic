package org.tptacs.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tptacs.application.useCases.users.CreateUserUC;
import org.tptacs.application.useCases.users.LoginUserUC;
import org.tptacs.presentation.requestModels.CreateUserRequest;
import org.tptacs.presentation.requestModels.LoginRequest;
import org.tptacs.presentation.responseModels.ResponseCreateUser;
import org.tptacs.presentation.responseModels.LoginResponse;

@RestController
@Tag(name = "Users")
@RequestMapping(value = "/api/users", produces = "application/json", consumes = "application/json")
public class UserController {
    private final CreateUserUC createUserUC;
    private final LoginUserUC loginUserUC;

    public UserController(CreateUserUC createUserUC, LoginUserUC loginUserUC) {
        this.createUserUC = createUserUC;
        this.loginUserUC = loginUserUC;
    }

    @PostMapping()
    public ResponseEntity<ResponseCreateUser> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(this.createUserUC.createUser(createUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(this.loginUserUC.login(loginRequest));
    }
}
