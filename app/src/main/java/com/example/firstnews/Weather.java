package com.example.firstnews;

import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Weather {
    String day;
    String date;
    String time;
    String celsius;
    String fahrenheit;
    String image;

    public Weather(String day, String date, String time, String celsius, String fahrenheit, String image) {
        this.day = day;
        this.date = date;
        this.time = time;
        this.celsius = celsius;
        this.fahrenheit = fahrenheit;
        this.image = image;
    }

    public String getDay() {
        return day;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
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

    public String getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(String fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
