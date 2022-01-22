package com.example.springbootdemo.repostitory;

import com.example.springbootdemo.config.ConnectionConfig;
import com.example.springbootdemo.entity.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class StudentRepository {

    public List<Student> findAll(){
        return ConnectionConfig.dbConnection.select(Student.class).run();
    }
    public Student findById(int id){
        List<Student> students = ConnectionConfig.dbConnection.select(Student.class).where("id = " + id).run();
        if (students.size() == 0)
            return null;
        return students.get(0);
    }
    public boolean save(Student student) {
        try {
            ConnectionConfig.dbConnection.insert(student);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
    public void delete(Student student) {
        ConnectionConfig.dbConnection.delete(student);
    }
    public void update(Student student) {
        ConnectionConfig.dbConnection.update(student);
    }
    public List<Student> findStudentsOfAClass(int classId) {
        return ConnectionConfig.dbConnection.select(Student.class).where("classroom = " + classId).run();
    }
}
