package com.example.firstnews;

import android.widget.ImageView;

import androidx.annotation.NonNull;


public class Weather {

    String day;
    String date;
    String time;
    String celsius;
    String description;
    String image;

    public Weather(String day, String date, String time, String celsius, String description, String image) {
        this.day = day;
        this.date = date;
        this.time = time;
        this.celsius = celsius;
        this.description = description;
        this.image = image;
    }

    public Weather() {

    }

    @Override
    public String toString() {
        return "Weather{" +
                "day='" + day + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", celsius='" + celsius + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getDay() {
        return day;
    }


    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCelsius() {
        return celsius;
    }

    public void setCelsius(String celsius) {
        this.celsius = celsius;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}