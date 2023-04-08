package com.example.selflearning;

public class Subject {
    public String name, description;
    public Integer course, semester;

    public Subject(String name, String description, Integer course, Integer semester) {
        this.name = name;
        this.description = description;
        this.course = course;
        this.semester = semester;
    }

    public Subject() {}
}
