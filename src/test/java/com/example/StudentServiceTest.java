package com.example;

import com.example.entity.Student;
import com.example.model.ApiDTO;
import com.example.model.NameDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.model.StudentDTO;
import com.example.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;
    private final StudentDTOMapper studentDTOMapper = new StudentDTOMapper();
    private final Student basicStudent = new Student(
            "Ewa",
            "Test",
            "testEwa@o2.pl",
            "Czechia",
            LocalDate.EPOCH);

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, studentDTOMapper);
    }

    @Test
    void getAllStudentsTest() {
        studentService.getStudents();

        verify(studentRepository).findAll();
    }

    @Test
    void canGetStudentTest() {
        String email = "testEwa@o2.pl";
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(basicStudent));

        StudentDTO expected = studentDTOMapper.apply(basicStudent);

        StudentDTO actual = studentService.getStudentByEmail(email);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void addStudentTest() {
        String email = "thomas@gmail.com";
        Student newStudent = new Student(
                "Thomas",
                "Test",
                email,
                "Poland",
                LocalDate.EPOCH);

        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        studentService.addNewStudent(newStudent);

        verify(studentRepository).save(newStudent);
    }

    @Test
    void deleteStudentTest() {
        long id = 2;
        boolean exists = true;

        when(studentRepository.existsById(id)).thenReturn(exists);

        studentService.deleteStudent(id);

        verify(studentRepository).deleteById(id);
    }

    @Test
    void updateStudentTest() {
        Long id = 2L;
        String firstName = "Eva";
        String lastName = "Testing";
        String dateOfBirth = "1999-03-01";
        String email = "testN2@o2.pl";
        String country = "Germany";
        when(studentRepository.findById(id)).thenReturn(Optional.of(basicStudent));

        studentService.updateStudent(id, firstName, lastName, dateOfBirth, email, country);

        verify(studentRepository).findById(id);
        verify(studentRepository).findByEmail(email);
        verify(studentRepository).save(basicStudent);
    }

    @Test
    void getCountryByStudentIdTest() {
        Long studentId = 3L;
        String expected = "Czechia";
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(basicStudent));

        String actual = studentService.getCountryByStudentId(studentId);

        assertThat(expected).isEqualTo(actual);
        verify(studentRepository).findById(studentId);
    }

    @Test
    void mapApiToRegionTest() {
        NameDTO italyDTO = new NameDTO("Italy", "Long name of Italy");
        NameDTO swedenDTO = new NameDTO("Sweden", "Long name of Sweden");
        ApiDTO[] apiObj = new ApiDTO[2];
        apiObj[0] = new ApiDTO(italyDTO, "italySubregion", "italyRegion");
        apiObj[1] = new ApiDTO(swedenDTO, "swedenSubregion", "swedenRegion");
        String country = "Italy";

        Collection<RegionAndSubregionDTO> expected = new ArrayList<>();
        expected.add(new RegionAndSubregionDTO("italyRegion", "italySubregion"));

        Collection<RegionAndSubregionDTO> actual = studentService.mapApiToRegion(apiObj, country);

        assertThat(expected).isEqualTo(actual);
    }

}
