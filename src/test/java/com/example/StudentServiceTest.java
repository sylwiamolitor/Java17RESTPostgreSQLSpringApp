package com.example;

import com.example.model.Student;
import com.example.model.StudentDTO;
import com.example.repo.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepo studentRepo;
    private final StudentDTOMapper studentDTOMapper = new StudentDTOMapper();
    private final Student basicStudent = new Student(
            "Ewa",
            "Test",
            "testEwa@o2.pl",
            LocalDate.EPOCH);

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepo, studentDTOMapper);
    }

    @Test
    void getAllStudentsTest() {
        studentService.getStudents();

        verify(studentRepo).findAll();
    }

    @Test
    void canGetStudentTest() {
        String email = "testEwa@o2.pl";
        when(studentRepo.findStudentByEmail(email)).thenReturn(Optional.of(basicStudent));

        StudentDTO expected = studentDTOMapper.apply(basicStudent);

        StudentDTO actual = studentService.getStudentByEmail(email);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addStudentTest() {
        String email = "thomas@gmail.com";
        Student newStudent = new Student(
                "Thomas",
                "Test",
                email,
                LocalDate.EPOCH);

        when(studentRepo.findStudentByEmail(email)).thenReturn(Optional.empty());

        studentService.addNewStudent(newStudent);

        verify(studentRepo).save(newStudent);
    }

    @Test
    void deleteStudentTest() {
        long id = 2;
        boolean exists = true;

        when(studentRepo.existsById(id)).thenReturn(exists);

        studentService.deleteStudent(id);

        verify(studentRepo).deleteById(id);
    }

    @Test
    void updateStudentTest() {
        Long id = 2L;
        String firstName = "Eva";
        String lastName = "Testing";
        String dateOfBirth = "1999-03-01";
        String email = "testN2@o2.pl";
        when(studentRepo.findById(id)).thenReturn(Optional.of(basicStudent));

        studentService.updateStudent(id, firstName, lastName, dateOfBirth, email);

        verify(studentRepo).findById(id);
        verify(studentRepo).findStudentByEmail(email);
        verify(studentRepo).save(basicStudent);
    }
}
