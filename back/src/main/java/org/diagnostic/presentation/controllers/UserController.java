package org.diagnostic.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.diagnostic.application.useCases.users.CreateUserUC;
import org.diagnostic.application.useCases.users.LoginUserUC;
import org.diagnostic.application.useCases.users.RecoveryTokenService;
import org.diagnostic.application.useCases.users.SenderEmailUC;
import org.diagnostic.presentation.requestModels.ChangePassRequest;
import org.diagnostic.presentation.requestModels.CreateUserRequest;
import org.diagnostic.presentation.requestModels.EmailRequest;
import org.diagnostic.presentation.requestModels.LoginRequest;
import org.diagnostic.presentation.responseModels.LoginResponse;
import org.diagnostic.presentation.responseModels.RecoveryResponse;
import org.diagnostic.presentation.responseModels.Response;
import org.diagnostic.presentation.responseModels.ResponseCreateUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Users")
@RequestMapping(value = "/api/users", produces = "application/json", consumes = "application/json")
@Slf4j
public class UserController extends BaseController{
    private final CreateUserUC createUserUC;
    private final LoginUserUC loginUserUC;
    private final SenderEmailUC senderEmailUC;
    private final RecoveryTokenService recoveryTokenService;

    public UserController(CreateUserUC createUserUC, LoginUserUC loginUserUC, SenderEmailUC senderEmailUC, RecoveryTokenService recoveryTokenService) {
        this.createUserUC = createUserUC;
        this.loginUserUC = loginUserUC;
        this.senderEmailUC = senderEmailUC;
        this.recoveryTokenService = recoveryTokenService;
    }

    @PostMapping()
    public ResponseEntity<ResponseCreateUser> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(this.createUserUC.createUser(createUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(this.loginUserUC.login(loginRequest));
    }

    @PostMapping("/recovery")
    public ResponseEntity<Response> recovery(@RequestBody EmailRequest loginRequest) {
        try{
            loginUserUC.loadUserByEmail(loginRequest.getEmail());
            String code = recoveryTokenService.generateOneTimeCode();
            String token = recoveryTokenService.generateToken(loginRequest.getEmail(), code);
            log.info("Se envia correo con codigo: {} y token: {}",code, token);
            senderEmailUC.sendEmail(loginRequest.getEmail(), "Recuperacion de contraseña", "Por favor no responder, su codigo de un solo uso es: " + code);
            return ResponseEntity.ok(new RecoveryResponse("200","Codigo enviado",LocalDateTime.now(), token));
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new Response("404","Correo no encontrado", LocalDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (MessagingException e) {
            return new ResponseEntity<>(new Response("400",e.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePass")
    public ResponseEntity<Response> changePass(@RequestBody ChangePassRequest change) {
        try{
            boolean isValid = recoveryTokenService.validateToken(change.getToken(), change.getEmail(), change.getCode());
            if(isValid){
                createUserUC.ressetPassward(change.getNewPassword(), this.getUserFromJwt().getUsername());
                return ResponseEntity.ok(new Response("200","Codigo enviado",LocalDateTime.now()));
            }else return new ResponseEntity<>(new Response("400","Datos no validos",LocalDateTime.now()), HttpStatus.BAD_REQUEST);

        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new Response("404","Correo no encontrado", LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }



}
