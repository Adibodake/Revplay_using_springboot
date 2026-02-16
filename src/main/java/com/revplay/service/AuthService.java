package com.revplay.service;

import com.revplay.dto.RegisterRequest;
import com.revplay.entity.User;
import com.revplay.entity.enums.Role;
import com.revplay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .displayName(req.getDisplayName() == null ? req.getUsername() : req.getDisplayName())
                .role(Role.LISTENER)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    public User login(String usernameOrEmail, String password) {

        User user = userRepository
                .findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }
}
