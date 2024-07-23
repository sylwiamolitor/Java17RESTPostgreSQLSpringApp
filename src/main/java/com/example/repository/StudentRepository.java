package com.example.repository;

import com.example.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
@Tag(name = "JPA repository")
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Operation(summary = "Query for finding students by his/her email address")
    Optional<Student> findByEmail(String email);

}
