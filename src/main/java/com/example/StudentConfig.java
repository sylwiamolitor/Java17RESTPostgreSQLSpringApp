package com.example;

import com.example.model.Student;
import com.example.repo.StudentRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepo studentRepo) {
        return args -> {
            Student newStudent = new Student(
                    "Sylwia",
                    "Test",
                    "testEmail@o2.pl",
                    "Liberia",
                    LocalDate.EPOCH);
            studentRepo.save(newStudent);
        };
    }

}
