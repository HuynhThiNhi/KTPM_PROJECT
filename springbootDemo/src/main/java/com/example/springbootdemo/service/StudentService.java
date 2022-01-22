package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.Student;
import com.example.springbootdemo.repostitory.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    final private StudentRepository studentRepository;

    public List<Student> findAll(){
        return studentRepository.findAll();
    }
    public Student findById(int id){
        return studentRepository.findById(id);
    }

    public boolean save(Student student) {
       return studentRepository.save(student);

    }

    public void delete(Student student) {
        studentRepository.delete(student);
    }

    public void update(Student student) {
        studentRepository.update(student);
    }

    public List<Student> findStudentsOfAClass(int classId) {
        return studentRepository.findStudentsOfAClass(classId);
    }
}
