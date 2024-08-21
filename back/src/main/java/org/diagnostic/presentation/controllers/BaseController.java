package org.diagnostic.presentation.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.diagnostic.domain.entities.User;

public class BaseController {
    public User getUserFromJwt() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
