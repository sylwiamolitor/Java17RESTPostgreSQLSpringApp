package com.example;

import com.example.entity.Student;
import com.example.mappers.StudentMapper;
import com.example.model.ApiDTO;
import com.example.model.RegionAndSubregionDTO;
import com.example.model.StudentDTO;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
    public ResponseEntity<Student> addStudent(@Valid @RequestBody StudentDTO student) {
        return ResponseEntity.ok(studentService.addNewStudent(studentMapper.studentDTOToStudent(student)));
    }

    @GetMapping
    @Operation(summary = "Method for getting all students in database (using pagination)")
    public ResponseEntity<Page<StudentDTO>> getStudents(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy
    ) {
        if (offset == null)
            offset = 0;
        if (pageSize == null)
            pageSize = 10;
        if (StringUtils.isEmpty(sortBy))
            sortBy = "id";
        return ResponseEntity.ok(
                (studentService.getStudents(PageRequest.of(offset, pageSize, Sort.by(sortBy)))
                        .map(studentMapper::studentToStudentDTO)));
    }

    @GetMapping(path = "{email}")
    @Operation(summary = "Method for getting one student by his/her email address")
    public ResponseEntity<StudentDTO> getStudentByEmail(@PathVariable("email") String email) {
        Optional<Student> result = studentService.getStudentByEmail(email);
        return result.map(student -> ResponseEntity.ok(studentMapper.studentToStudentDTO(student)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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
