package com.example.managerstaff.models;

public class StatisticalTimeUser {

    private String dayOfWeek;
    private String dayOfWeekName;
    private boolean isDayOff;

    public StatisticalTimeUser() {
        this.isDayOff=true;
    }

    public StatisticalTimeUser(String dayOfWeek, String dayOfWeekName, boolean isDayOff) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfWeekName = dayOfWeekName;
        this.isDayOff = isDayOff;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public void setDayOfWeekName(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
    }

    public boolean isDayOff() {
        return isDayOff;
    }

    public void setDayOff(boolean dayOff) {
        isDayOff = dayOff;
    }

}
