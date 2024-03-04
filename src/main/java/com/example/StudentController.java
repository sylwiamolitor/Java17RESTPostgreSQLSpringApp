package com.example;

import com.example.model.Student;
import com.example.model.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                              @RequestParam(required = false) String email) {
        studentService.updateStudent(studentId, firstName, lastName, dateOfBirth, email);
    }
}
