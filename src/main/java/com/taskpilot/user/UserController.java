package com.taskpilot.user;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.taskpilot.dto.LoginRequestDTO;
import com.taskpilot.dto.LoginResponseDTO;
import com.taskpilot.dto.UserRequestDTO;
import com.taskpilot.dto.UserResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser() {

        User user = userService.getCurrentUser();

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}