package com.example;

import com.example.model.Student;
import com.example.model.StudentDTO;
import com.example.repo.StudentRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final StudentDTOMapper studentDTOMapper;

    @Autowired
    public StudentService(StudentRepo studentRepo, StudentDTOMapper studentDTOMapper) {
        this.studentRepo = studentRepo;
        this.studentDTOMapper = studentDTOMapper;
    }

    public List<StudentDTO> getStudents() {
        return studentRepo.findAll()
                .stream()
                .map(studentDTOMapper)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentByEmail(String email) {
        return studentRepo.findStudentByEmail(email)
                .stream()
                .map(studentDTOMapper)
                .findFirst()
                .orElseThrow();
    }

    public void addNewStudent(Student student) {
        Optional<Student> optionalStudent = studentRepo.findStudentByEmail(student.getEmail());
        if (optionalStudent.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepo.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepo.existsById(id);
        if (!exists)
            throw new IllegalStateException("student with id" + id + " does not exist");
        studentRepo.deleteById(id);

    }

    @Transactional
    public void updateStudent(Long id, String firstName, String lastName, String dateOfBirth, String email) {
        Student student = studentRepo.findById(id).orElseThrow(() -> new IllegalStateException("student with id " + id + " does not exist"));
        boolean changed = false;
        if (firstName != null && !firstName.isEmpty() && !Objects.equals(student.getFirstName(), firstName)) {
            student.setFirstName(firstName);
            changed = true;
        }
        if (lastName != null && !lastName.isEmpty() && !Objects.equals(student.getLastName(), lastName)) {
            student.setLastName(lastName);
            changed = true;
        }
        if (dateOfBirth != null && !dateOfBirth.isEmpty() && DateValidator.isValid(dateOfBirth)) {
            LocalDate date = LocalDate.parse(dateOfBirth);
            if (!Objects.equals(student.getDateOfBirth(), date)) {
                student.setDateOfBirth(date);
                changed = true;
            }
        }

        if (email != null && !email.isEmpty() && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = studentRepo.findStudentByEmail(email);
            if (studentOptional.isPresent())
                throw new IllegalStateException("email taken");
            student.setEmail(email);
            changed = true;
        }
        if (changed)
            studentRepo.save(student);
    }
}
