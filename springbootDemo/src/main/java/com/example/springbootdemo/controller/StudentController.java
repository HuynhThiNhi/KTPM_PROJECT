package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.Student;
import com.example.springbootdemo.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/student")
@AllArgsConstructor
public class StudentController {
    final private StudentService studentService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Student>> findAll(){
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id){

        return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findByClassId/{classId}")
    public ResponseEntity<List<Student>> findByClassId(@PathVariable int classId){
        List<Student> students = studentService.findStudentsOfAClass(classId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HashMap<String, String>> addStudent(@RequestBody Student student){
        boolean result = studentService.save(student);
        HashMap<String, String> response = new HashMap<>();
        if(result){
            response.put("message", "Adding a student successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.put("message", "Adding a student unsuccessfully!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HashMap<String, String>> updateStudent(@PathVariable int id, @RequestBody Student student){
        Student updatedStudent = studentService.findById(id);
        HashMap result = new HashMap();
        if (updatedStudent == null){
            result.put("message", "id doesn't exist!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        else{
            student.setId(id);
            studentService.update(student);
            result.put("message", "Updated successfully.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, String>> deleteStudentById(@PathVariable int id){
        Student student = studentService.findById(id);
        HashMap result = new HashMap();
        if (student == null){
            result.put("message", "id doesn't exist!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        else{
            studentService.delete(student);
            result.put("message", "Delete successfully.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
