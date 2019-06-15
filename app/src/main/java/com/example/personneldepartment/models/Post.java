package com.example.personneldepartment.models;

public class Post {
    private int id;
    private String name;
    private String department;
    private double salary;
    private String min_education;
    private String min_experience;

    public Post() {
    }

    public Post(String name, String department, String salary, String min_education, String min_experience) {
        this.name = name;
        this.department = department;
        try {
            this.salary = Double.parseDouble(salary);
        } catch (NumberFormatException exp){
            this.salary = 0;
        }
        this.min_education = min_education;
        this.min_experience = min_experience;
    }

    public Post(int id, String name, String department, String salary, String min_education, String min_experience) {
        this.id = id;
        this.name = name;
        this.department = department;
        try {
            this.salary = Double.parseDouble(salary);
        } catch (NumberFormatException exp){
            this.salary = 0;
        }
        this.min_education = min_education;
        this.min_experience = min_experience;
    }

    public Post(int id, String name, String department, double salary, String min_education, String min_experience) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.min_education = min_education;
        this.min_experience = min_experience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getMin_education() {
        return min_education;
    }

    public void setMin_education(String min_education) {
        this.min_education = min_education;
    }

    public String getMin_experience() {
        return min_experience;
    }

    public void setMin_experience(String min_experience) {
        this.min_experience = min_experience;
    }
}
