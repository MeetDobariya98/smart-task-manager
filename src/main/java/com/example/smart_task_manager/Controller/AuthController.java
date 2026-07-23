package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.LoginRequest;
import com.example.smart_task_manager.Dto.LoginResponse;
import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Security.JwtService;
import com.example.smart_task_manager.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserService userService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with ROLE_USER."
    )


    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid UserRequest request) {

        log.info("Registration request received for email: {}", request.email());

        userService.createUser(request);

        log.info("User registered successfully: {}", request.email());

        return ResponseEntity.ok(
                "User Registered Successfully"
        );
    }


    @Operation(
            summary = "Login User",
            description = "Authenticates the user and returns a JWT token."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        log.info("Login request received for email: {}", request.email());

        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        log.info("User logged in successfully: {}", request.email());

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }
}