package com.example.firstnews;

import android.os.Build;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Day {
    String year;
    String day;
    String month;

    public Day(String year, String day, String month) {
        this.year = year;
        this.day = day;
        this.month = month;
    }

    public static String getDayFromDate(String day, String month, String year) {
        int y = Integer.parseInt(year), m = Integer.parseInt(month), d = Integer.parseInt(day);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return DateTimeFormatter.ofPattern("EEEE")
                    .format(LocalDate.of(y, m, d));
        }
        return "null";
    }

    public String getYear () {
        return year;
    }

    public void setYear (String year){
        this.year = year;
    }

    public String getDay () {
        return day;
    }

    public void setDay (String day){
        this.day = day;
    }

    public String getMonth () {
        return month;
    }

    public void setMonth (String month){
        this.month = month;
    }
}
