package com.revplay.repository;

import com.revplay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository  extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    // useful for login with "email OR username"
    Optional<User> findByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
