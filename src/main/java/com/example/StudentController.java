package com.example;

import com.example.mappers.StudentMapper;
import com.example.model.ApiDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.model.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/student")
@CrossOrigin("*")
@Tag(name = "Operations on students")
@RequiredArgsConstructor
public class StudentController {

    @Autowired
    private final StudentService studentService;
    @Autowired
    private final StudentMapper studentMapper;
    @Autowired
    private final RestTemplate restTemplate;

    @PostMapping("/addStudent")
    @Operation(summary = "Method for adding students")
    public void addStudent(@RequestBody StudentDTO student) {
        studentService.addNewStudent(studentMapper.studentDTOToStudent(student));
    }

    @GetMapping
    @Operation(summary = "Method for getting all students in database")
    public List<StudentDTO> getStudents() {
        return studentService.getStudents().stream()
                .map(studentMapper::studentToStudentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "{email}")
    @Operation(summary = "Method for getting one student by his/her email address")
    public StudentDTO getStudentByEmail(@PathVariable("email") String email) {
        return studentMapper.studentToStudentDTO(studentService.getStudentByEmail(email));
    }

    @GetMapping(path = "id/{id}")
    @Operation(summary = "Method for getting student's country by his/her id")
    public Collection<String> getCountryById(@PathVariable("id") Long id) {
        return Collections.singletonList(studentService.getCountryByStudentId(id));
    }

    @DeleteMapping(path = "{studentId}")
    @Operation(summary = "Method for deleting student by his/her ID")
    public void deleteStudent(@PathVariable("studentId") Long id) {
        studentService.deleteStudent(id);
    }

    @PutMapping(path = "{studentId}")
    @Operation(summary = "Method for updating students")
    public void updateStudent(@PathVariable("studentId") Long studentId,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) String dateOfBirth,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String country) {
        studentService.updateStudent(studentId, firstName, lastName, dateOfBirth, email, country);
    }

    @GetMapping(path = "regionsByCountry/{studentId}")
    @Operation(summary = "Method for getting students' regions")
    public Collection<RegionAndSubregionDTO> getRegionsByStudentId(@PathVariable("studentId") Long studentId) {
        String country = studentService.getCountryByStudentId(studentId);
        String uri = "https://restcountries.com/v3.1/independent?status=true";

        ApiDTO[] apiObj = restTemplate.getForObject(uri, ApiDTO[].class);

        return studentService.mapApiToRegion(apiObj, country);
    }

}
