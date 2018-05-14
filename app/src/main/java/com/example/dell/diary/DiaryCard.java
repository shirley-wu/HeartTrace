package com.example.dell.diary;

import java.io.Serializable;

/**
 * Created by dell on 2018/5/6.
 */

public class DiaryCard implements Serializable {

    private int year;
    private int month;
    private int day;
    private String weekDay;
    private int emotionImageId;
    private String content;

    public DiaryCard(int year, int month, int day, String weekDay, int emotionImageId, String content) {
        this.year = year;
        this.emotionImageId = emotionImageId;
        this.weekDay = weekDay;
        this.month = month;
        this.day = day;
        this.content = content;
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

    public String getWeekDay() {
        return weekDay;
    }

    public int getEmotionImageId() {
        return emotionImageId;
    }

    public String getContent() {
        return content;
    }
}
