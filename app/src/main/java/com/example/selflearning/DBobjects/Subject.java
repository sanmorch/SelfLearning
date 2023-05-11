package com.example.selflearning.DBobjects;

public class Subject {
    public String id, name, description;
    public Integer course, semester;

    public Subject(String id, String name, String description, Integer course, Integer semester) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.course = course;
        this.semester = semester;
    }

    public Subject() {}
}
