package com.example.repo;

import com.example.model.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email=?1")
    @Operation(summary = "Query for finding users by his/her email address")
    Optional<User> findUserByEmail(String email);

}
