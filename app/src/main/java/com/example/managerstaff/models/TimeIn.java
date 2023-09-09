package com.example.managerstaff.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class TimeIn {
    @SerializedName("id")
    @Expose
    private int idTimeIn;
    @SerializedName("day_in")
    @Expose
    private String dayIn;
    @SerializedName("time_in")
    @Expose
    private String timeIn;

    public TimeIn() {
    }

    public TimeIn(int idTimeIn, String dayIn, String timeIn) {
        this.idTimeIn = idTimeIn;
        this.dayIn = dayIn;
        this.timeIn = timeIn;
    }

    public int getIdTimeIn() {
        return idTimeIn;
    }

    public void setIdTimeIn(int idTimeIn) {
        this.idTimeIn = idTimeIn;
    }

    public String getDayIn() {
        return dayIn;
    }

    public void setDayIn(String dayIn) {
        this.dayIn = dayIn;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }
}
