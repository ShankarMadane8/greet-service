package com.example.greetservice.controller;

import com.example.greetservice.entity.Student;
import com.example.greetservice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "CRUD operations for student entities with batch processing support")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Create a new student", description = "Creates a new student record in the system. The student will be saved with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student created successfully", content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Parameter(description = "Student object to be created", required = true) @RequestBody Student student) {
        return ResponseEntity.ok(studentService.saveStudent(student));
    }

    @Operation(summary = "Get all students", description = "Retrieves a list of all students in the system, including their status (PENDING/PROCESSED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students", content = @Content(schema = @Schema(implementation = Student.class)))
    })
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Operation(summary = "Get student by ID", description = "Retrieves a specific student by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found", content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "ID of the student to retrieve", required = true) @PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete student", description = "Deletes a student record from the system by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID of the student to delete", required = true) @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
