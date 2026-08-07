package com.taskpilot.user;

import org.springframework.security.core.Authentication;
import com.taskpilot.dto.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskpilot.jwt.JwtService;
import com.taskpilot.dto.LoginRequestDTO;
import com.taskpilot.dto.LoginResponseDTO;
import com.taskpilot.dto.UserRequestDTO;
import com.taskpilot.dto.UserResponseDTO;
import com.taskpilot.dto.RegisterRequestDTO;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    

    public UserService(UserRepository userRepository,
            JwtService jwtService,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {

		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		}

    public UserResponseDTO createUser(UserRequestDTO request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }
    public void register(RegisterRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        UserRequestDTO userRequest = new UserRequestDTO();

        userRequest.setName(request.getName());
        userRequest.setEmail(request.getEmail());
        userRequest.setPassword(request.getPassword());

        createUser(userRequest);
    }

    private UserResponseDTO mapToResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
    public LoginResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDTO(token);
    }
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}