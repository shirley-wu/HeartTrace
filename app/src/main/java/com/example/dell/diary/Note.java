package com.example.dell.diary;

/**
 * Created by Helen_L on 2018/5/15.
 */

public class Note {
    private int year;
    private int month;
    private int day;
    private String weekDay;
    private String content;
    private String date;

    public Note(int year, int month, int day, String weekDay, String content) {
        this.year = year;
        this.weekDay = weekDay;
        this.month = month;
        this.day = day;
        this.content = content;
        this.date = year + "." + month + "." + day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getDate(){ return date;}

    public String getWeekDay() {
        return weekDay;
    }


    public String getContent() {
        return content;
    }
}
