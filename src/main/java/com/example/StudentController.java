package com.example;

import com.example.model.Student;
import com.example.model.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
@CrossOrigin("*")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    @GetMapping
    public List<StudentDTO> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping(path = "{email}")
    public StudentDTO getStudentByEmail(@PathVariable("email") String email) {
        return studentService.getStudentByEmail(email);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long id) {
        studentService.deleteStudent(id);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Long studentId,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) String dateOfBirth,
                              @RequestParam(required = false) String email) {
        studentService.updateStudent(studentId, firstName, lastName, dateOfBirth, email);
    }
}
