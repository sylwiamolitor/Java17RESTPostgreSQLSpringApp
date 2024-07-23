package com.example.model;

import java.time.LocalDate;

public record StudentDTO(String firstName, String lastName, String email, String country, LocalDate dateOfBirth) {


}
