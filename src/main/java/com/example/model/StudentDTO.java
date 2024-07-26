package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
    String firstName;
    String lastName;
    String email;
    String country;
    LocalDate dateOfBirth;

}
