package com.example;

import com.example.entity.Student;
import com.example.model.ApiDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow();
    }

    public String getCountryByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .stream()
                .map(Student::getCountry)
                .findFirst()
                .orElseThrow();
    }

    public Collection<RegionAndSubregionDTO> mapApiToRegion(ApiDTO[] apiObj, String country) {
        return Arrays.stream(apiObj)
                .filter(input -> input.name().common().equals(country))
                .map(input -> new RegionAndSubregionDTO(input.region(), input.subregion()))
                .collect(Collectors.toList());
    }

    public void addNewStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(student.getEmail());
        if (optionalStudent.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        checkIfEmailValid(student.getEmail());
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists)
            throw new IllegalArgumentException("student with id" + id + " does not exist");
        studentRepository.deleteById(id);

    }

    @Transactional
    public void updateStudent(Long id, String firstName, String lastName, String dateOfBirth, String email, String country) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("student with id " + id + " does not exist"));
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
        if (country != null && !country.isEmpty() && !Objects.equals(student.getCountry(), country)) {
            student.setCountry(country);
            changed = true;
        }
        if (email != null && !email.isEmpty() && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = studentRepository.findByEmail(email);
            if (studentOptional.isPresent())
                throw new IllegalStateException("email taken");
            checkIfEmailValid(email);
            student.setEmail(email);
            changed = true;
        }
        if (changed)
            studentRepository.save(student);
    }

    private void checkIfEmailValid(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email not valid");
        }
    }
}
