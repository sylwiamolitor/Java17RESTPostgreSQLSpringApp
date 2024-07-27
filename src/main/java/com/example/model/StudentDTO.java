package com.example.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    String email;
    String country;
    @NotNull
    LocalDate dateOfBirth;

}
