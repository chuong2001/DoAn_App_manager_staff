package com.example.managerstaff.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class TimeOut {
    @SerializedName("id")
    @Expose
    private int idTimeOut;
    @SerializedName("day_out")
    @Expose
    private String dayOut;
    @SerializedName("time_out")
    @Expose
    private String timeOut;

    public TimeOut() {
    }

    public TimeOut(int idTimeOut, String dayOut, String timeOut) {
        this.idTimeOut = idTimeOut;
        this.dayOut = dayOut;
        this.timeOut = timeOut;
    }

    public int getIdTimeOut() {
        return idTimeOut;
    }

    public void setIdTimeOut(int idTimeOut) {
        this.idTimeOut = idTimeOut;
    }

    public String getDayOut() {
        return dayOut;
    }

    public void setDayOut(String dayOut) {
        this.dayOut = dayOut;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
}
