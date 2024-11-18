package com.example;

import com.example.entity.Student;
import com.example.model.ApiDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getStudents(PageRequest pageRequest) {
        return studentRepository.findAll(pageRequest);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .stream()
                .findFirst();
    }

    public Optional<String> getCountryByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .stream()
                .map(Student::getCountry)
                .filter(Objects::nonNull)
                .findFirst();
    }

    public Collection<RegionAndSubregionDTO> mapApiToRegion(ApiDTO[] apiObj, String country) {
        return Arrays.stream(apiObj)
                .filter(input -> input.name().common().equals(country))
                .map(input -> new RegionAndSubregionDTO(input.region(), input.subregion()))
                .collect(Collectors.toList());
    }

    public Student addNewStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(student.getEmail());
        if (optionalStudent.isPresent()) {
            throw new IllegalStateException("Email taken");
        }
        checkIfEmailValid(student.getEmail());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists)
            throw new IllegalArgumentException("Student with id" + id + " does not exist");
        studentRepository.deleteById(id);

    }

    @Transactional
    public Student updateStudent(Long id, Student newStudent) {
        if (studentRepository.findById(id).isPresent()) {
            checkIfEmailValid(newStudent.getEmail());
            Optional<Student> studentOptional = studentRepository.findByEmail(newStudent.getEmail());
            if (studentOptional.isPresent())
                throw new IllegalStateException("Email taken");
            newStudent.setId(id);
            return studentRepository.save(newStudent);
        } else throw new IllegalArgumentException("Student with id " + id + " does not exist");
    }

    private void checkIfEmailValid(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email not valid");
        }
    }
}
