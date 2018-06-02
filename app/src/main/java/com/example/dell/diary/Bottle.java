package com.example.dell.diary;

/**
 * Created by Helen_L on 2018/5/15.
 */

public class Bottle {

    private int year;
    private int month;
    private int day;
    private String weekDay;
    private int bottleId;
    private String readme;
    private int imageId;
    private String bottleName;

    public Bottle(int year, int month, int day, String weekDay, int bottleId, String readme, int imageId, String bottleName) {
        this.year = year;
        this.bottleId = bottleId;
        this.weekDay = weekDay;
        this.month = month;
        this.day = day;
        this.readme = readme;
        this.imageId = imageId;
        this.bottleName = bottleName;
    }
    public Bottle(String bottleName,String reademe, int imageId) {
        this.imageId = imageId;
        this.bottleName = bottleName;
        this.readme = reademe;
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

    public int getBottleId() {
        return bottleId;
    }

    public String getReadme() {
        return readme;
    }

    public int getImageId(){ return imageId; }

    public String getBottleName(){ return bottleName; }

}
