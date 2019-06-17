package com.example.personneldepartment.models;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {
    private int id;
    private String number_employment_record;
    private String surname;
    private String name;
    private String patronymic;
    private String date_of_birth;
    private String phone_number;
    private String email;
    private String address;
    private String education;
    private int experience;
    private String resume;
    private short is_employee;
    private Integer id_position;

    public Person() {
    }

    public Person(String number_employment_record, String surname, String name,
                  String patronymic, String date_of_birth, String phone_number, String email,
                  String address, String education, String experience,
                  short is_employee, Integer id_position) {
        this.number_employment_record = number_employment_record;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.education = education;
        this.experience = Integer.parseInt(experience);
        this.is_employee = is_employee;
        this.id_position = id_position;
    }

    public Person(int id, String number_employment_record, String surname, String name,
                  String patronymic, String date_of_birth, String phone_number, String email,
                  String address, String education, String experience,
                  short is_employee, Integer id_position) {
        this.id = id;
        this.number_employment_record = number_employment_record;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.education = education;
        this.experience = Integer.parseInt(experience);
        this.is_employee = is_employee;
        this.id_position = id_position;
    }

    public Person(int id, String number_employment_record, String surname, String name,
                  String patronymic, String date_of_birth, String phone_number, String email,
                  String address, String education, int experience, String resume,
                  short is_employee, Integer id_position) {
        this.id = id;
        this.number_employment_record = number_employment_record;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        setDate_of_birth(date_of_birth);
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.education = education;
        this.experience = experience;
        this.resume = resume;
        this.is_employee = is_employee;
        this.id_position = id_position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber_employment_record() {
        return number_employment_record;
    }

    public void setNumber_employment_record(String number_employment_record) {
        this.number_employment_record = number_employment_record;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth.substring(0, 10);
    }

    public String getFormattedDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(date_of_birth.substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateTime normalDateTime = new DateTime(date.getTime());
        return normalDateTime.toString("yyyy.MM.dd");
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public short getIs_employee() {
        return is_employee;
    }

    public void setIs_employee(short is_employee) {
        this.is_employee = is_employee;
    }

    public Integer getId_position() {
        return id_position;
    }

    public void setId_position(Integer id_position) {
        this.id_position = id_position;
    }
}
