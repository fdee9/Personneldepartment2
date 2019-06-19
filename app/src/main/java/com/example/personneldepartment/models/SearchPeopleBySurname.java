package com.example.personneldepartment.models;

public class SearchPeopleBySurname {
    private String surname;
    private short is_employee;

    public SearchPeopleBySurname(String surname, short is_employee) {
        this.surname = surname;
        this.is_employee = is_employee;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public short getIs_employee() {
        return is_employee;
    }

    public void setIs_employee(short is_employee) {
        this.is_employee = is_employee;
    }
}
