package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.LoginRequest;
import com.example.smart_task_manager.Dto.LoginResponse;
import com.example.smart_task_manager.Security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {
        

        String token =
                jwtService.generateToken(request.email());

        return ResponseEntity.ok(

                new LoginResponse(token)

        );
    }
}