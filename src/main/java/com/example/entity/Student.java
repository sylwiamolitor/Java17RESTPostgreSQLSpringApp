package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "student", uniqueConstraints = {
        @UniqueConstraint(name = "student_email_unique", columnNames = "email")
})
@Data
public class Student {
    @Id
    @SequenceGenerator(name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "student_sequence")

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
    private String lastName;

    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;

    @Column(name = "date_of_birth", nullable = false, columnDefinition = "DATE")
    private LocalDate dateOfBirth;

    @Column(name = "country", nullable = true, columnDefinition = "TEXT")
    private String country;

    @Transient
    private Integer age;

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String country, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                '}';
    }
}
