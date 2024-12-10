package org.example.javaproj.controller.RestAPIs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.javaproj.dto.LoginRequest;
import org.example.javaproj.model.User;
import org.example.javaproj.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger();
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded", produces = "application/text")
    public ResponseEntity<String> login(LoginRequest loginRequest) throws IOException {
        String username = loginRequest.getUsername();
        LOGGER.info("Received login call for user `{}`", username);
        User user = userService.getUserByUsername(username);

        if (user != null) {
            return ResponseEntity.ok("Login successful for user: " + username);
        } else {
            LOGGER.warn("Unable to find user `{}`", username);
            User newUser = new User(username);
            newUser = userService.createUser(newUser);

            return ResponseEntity.status(201).body("New user created with id: " + newUser.getId());
        }
    }
}
