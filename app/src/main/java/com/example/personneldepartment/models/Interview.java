package com.example.personneldepartment.models;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Interview {
    private int id;
    private String date;
    private DateTime dateTime;
    private int id_candidate;
    private int id_vacancy;

    public Interview(int id ,String dateTime, int id_candidate, int id_vacancy) {
        this.id = id;
        this.date = dateTime;
        this.id_candidate = id_candidate;
        this.id_vacancy = id_vacancy;
    }

    public Interview(String dateTime, int id_candidate, int id_vacancy) {
        this.date = dateTime;
        this.id_candidate = id_candidate;
        this.id_vacancy = id_vacancy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeString(){
        return dateTime.toString("HH:mm");
    }

    public String getDateString(){
        return dateTime.toString("yyyy.MM.dd");
    }

    public void fixDateTime(){
        fixDateTime(date, "yyyy-MM-dd-HH:mm:ss");
    }

    public void fixDateTime(String date, String pattern){
        StringBuilder stringBuilder = new StringBuilder(date);
        stringBuilder.setCharAt(10, '-');
        try {
            Date bufferDate = new SimpleDateFormat(pattern).parse(stringBuilder.toString());
            dateTime = new DateTime(bufferDate.getTime());
        } catch (ParseException e) {
            dateTime = new DateTime(0);
            e.printStackTrace();
        }
    }

    public String getDateTimeString(){
        return dateTime.toString("yyyy-MM-dd HH:mm");
    }

    public int getId_candidate() {
        return id_candidate;
    }

    public void setId_candidate(int id_candidate) {
        this.id_candidate = id_candidate;
    }

    public int getId_vacancy() {
        return id_vacancy;
    }

    public void setId_vacancy(int id_vacancy) {
        this.id_vacancy = id_vacancy;
    }
}
