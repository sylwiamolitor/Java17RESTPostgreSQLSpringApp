package com.example;

import com.example.entity.Student;
import com.example.mappers.StudentMapper;
import com.example.model.ApiDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.model.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
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
    public ResponseEntity<Student> addStudent(@Valid @RequestBody StudentDTO student) throws URISyntaxException {
        return ResponseEntity.ok(studentService.addNewStudent(studentMapper.studentDTOToStudent(student)));
    }

    @GetMapping
    @Operation(summary = "Method for getting all students in database")
    public ResponseEntity<List<StudentDTO>> getStudents() {
        return ResponseEntity.ok(studentService.getStudents().stream()
                .map(studentMapper::studentToStudentDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping(path = "{email}")
    @Operation(summary = "Method for getting one student by his/her email address")
    public ResponseEntity<StudentDTO> getStudentByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(studentMapper.studentToStudentDTO(studentService.getStudentByEmail(email).get()));
    }

    @GetMapping(path = "id/{id}")
    @Operation(summary = "Method for getting student's country by his/her id")
    public ResponseEntity<Collection<String>> getCountryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Collections.singletonList(studentService.getCountryByStudentId(id)));
    }

    @DeleteMapping(path = "{studentId}")
    @Operation(summary = "Method for deleting student by his/her ID")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted");
    }

    @PutMapping(path = "{studentId}")
    @Operation(summary = "Method for updating students")
    public ResponseEntity<Student> updateStudent(@PathVariable("studentId") Long studentId,
                                                 @Valid @RequestBody StudentDTO student) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, studentMapper.studentDTOToStudent(student)));
    }

    @GetMapping(path = "regionsByCountry/{studentId}")
    @Operation(summary = "Method for getting students' regions")
    public ResponseEntity<Collection<RegionAndSubregionDTO>> getRegionsByStudentId(@PathVariable("studentId") Long studentId) {
        String country = studentService.getCountryByStudentId(studentId);
        String uri = "https://restcountries.com/v3.1/independent?status=true";

        ApiDTO[] apiObj = restTemplate.getForObject(uri, ApiDTO[].class);

        return ResponseEntity.ok(studentService.mapApiToRegion(apiObj, country));
    }

}
