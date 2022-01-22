package com.example.springbootdemo.entity;

import annotation.*;
import common.FETCHTYPE;
import lombok.Getter;
import lombok.Setter;

/*  TABLE Student
 *  id   INT
 *  name Varchar(45)
 *  age  INT
 *  classroom INT (FK)
 */
@Table(name="student")
@Setter
@Getter
public class Student {
    @PrimaryKey(name="id", autoGenerateId = true)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name = "age")
    private Integer age;
    @ManyToOne(tableName="classroom", refColumn="id", columnName="classroom", fetch = FETCHTYPE.EAGER)
    private Classroom classroom;
}
