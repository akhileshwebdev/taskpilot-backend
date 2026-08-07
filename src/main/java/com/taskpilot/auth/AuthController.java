package com.taskpilot.auth;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import com.taskpilot.dto.LoginRequestDTO;
import com.taskpilot.dto.LoginResponseDTO;
import com.taskpilot.dto.RegisterRequestDTO;
import com.taskpilot.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request) {

        return userService.login(request);
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @Valid @RequestBody RegisterRequestDTO request) {

        userService.register(request);
    }
}