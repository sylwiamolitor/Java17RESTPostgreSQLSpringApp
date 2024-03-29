package com.example;

import com.example.model.Student;
import com.example.model.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentDTOMapper implements Function<Student, StudentDTO> {
    @Override
    public StudentDTO apply(Student student) {
        return new StudentDTO(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getCountry(),
                student.getAge());
    }
}
