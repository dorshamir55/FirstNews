package com.example.firstnews;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Weather {

    String day;
    @SerializedName("dt_txt")
    String date;
    @SerializedName("dt_txt")
    String time;
    @SerializedName("temp")
    Double celsius;
    @SerializedName("temp")
    Double fahrenheit;
    @SerializedName("icon")
    String image;

    public Weather(String day, String date, String time, Double celsius, Double fahrenheit, String image) {
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

    public Double getCelsius() {
        return celsius;
    }

    public void setCelsius(Double celsius) {
        this.celsius = celsius;
    }

    public Double getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(Double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
