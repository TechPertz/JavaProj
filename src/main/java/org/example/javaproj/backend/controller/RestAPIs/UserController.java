package org.example.javaproj.backend.controller.RestAPIs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.javaproj.backend.dto.LoginRequest;
import org.example.javaproj.backend.dto.LoginResponse;
import org.example.javaproj.backend.model.Board;
import org.example.javaproj.backend.model.User;
import org.example.javaproj.backend.service.BoardService;
import org.example.javaproj.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger();
    private final UserService userService;
    private final BoardService boardService;

    public UserController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws IOException {
        String username = loginRequest.getUsername();
        LOGGER.info("Received login call for user `{}`", username);
        User user = userService.getUserByUsername(username);

        if (user != null) {
            Board board = boardService.getOrCreateMainBoard(user);
            LoginResponse response = new LoginResponse("Login successful for user: " + username, user.getId(), board.getId(), board.getMatrixData());
            return ResponseEntity.ok(response);
        } else {
            LOGGER.warn("Unable to find user `{}`", username);
            user = new User(username);
            user = userService.createUser(user);
            Board board = boardService.getOrCreateMainBoard(user);

            LoginResponse response = new LoginResponse("New user created with id: " + user.getId(), user.getId(), board.getId(), board.getMatrixData());
            return ResponseEntity.status(201).body(response);
        }
    }
}
