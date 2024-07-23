package com.example;

import com.example.entity.Student;
import com.example.model.ApiDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.model.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
@CrossOrigin("*")
@Tag(name = "Operations on students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public ModelMapper modelMapper;

    @PostMapping("/addStudent")
    @Operation(summary = "Method for adding students")
    public void addStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    @GetMapping
    @Operation(summary = "Method for getting all students in database")
    public List<StudentDTO> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping(path = "{email}")
    @Operation(summary = "Method for getting one student by his/her email address")
    public StudentDTO getStudentByEmail(@PathVariable("email") String email) {
        return studentService.getStudentByEmail(email);
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
        RestTemplate restTemplate = new RestTemplate();
        ApiDTO[] apiObj = restTemplate.getForObject(uri, ApiDTO[].class);

        return studentService.mapApiToRegion(apiObj, country);
    }
}
