package com.example.repository;

import com.example.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Operation(summary = "Query for finding users by his/her email address")
    Optional<User> findByEmail(String email);

}
