package com.revplay.controller;

import com.revplay.dto.LoginRequest;
import com.revplay.dto.RegisterRequest;
import com.revplay.entity.User;
import com.revplay.security.JwtService;
import com.revplay.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        User saved = authService.register(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse(
                        saved.getId(),
                        saved.getUsername(),
                        saved.getEmail(),
                        saved.getDisplayName()
                ));
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = authService.login(
                request.getUsernameOrEmail(),
                request.getPassword()
        );

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                token
        ));
    }

    // ================= RESPONSE RECORDS =================
    public record RegisterResponse(
            Long id,
            String username,
            String email,
            String displayName
    ) {}

    public record LoginResponse(
            Long id,
            String username,
            String email,
            String role,
            String token
    ) {}
}
