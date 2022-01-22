package com.example.springbootdemo.entity;

import annotation.Column;
import annotation.OneToMany;
import annotation.PrimaryKey;
import annotation.Table;
import common.FETCHTYPE;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Table(name="classroom")
@Getter
@Setter
public class Classroom {
    @PrimaryKey(name="id", autoGenerateId = true)
    private Integer id;
    @Column(name="name")
    private String name;
    @OneToMany(tableName="student", refColumn="classroom", fetch = FETCHTYPE.LAZY)
    private List<Student> students = new ArrayList<>();

}

