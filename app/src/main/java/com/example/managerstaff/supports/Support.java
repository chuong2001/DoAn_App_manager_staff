package com.example.managerstaff.supports;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.managerstaff.models.Setting;
import com.example.managerstaff.models.StatisticalTimeUser;
import com.example.managerstaff.models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Support {

    public static boolean checkPassValidate(String password){
        if(password.length()<6) return false;
        String regex = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
        Pattern specialCharPattern = Pattern.compile(regex);
        Matcher matcher = specialCharPattern.matcher(password);
        boolean containsSpecialChar = matcher.matches();
        boolean containsLetterOrDigit = password.matches(".*[a-zA-Z0-9].*");
        if(containsSpecialChar && containsLetterOrDigit) return true;
        return false;
    }

    public static List<StatisticalTimeUser> getDatesInMonth(int year, int month) {
        List<StatisticalTimeUser> list=new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            StatisticalTimeUser timeDay=new StatisticalTimeUser();
            Date currentDate = cal.getTime();
            String dateString = fmt.format(currentDate);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            String dayOfWeekName = "";
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    dayOfWeekName = "Chủ Nhật";
                    break;
                case Calendar.MONDAY:
                    dayOfWeekName = "Thứ Hai";
                    break;
                case Calendar.TUESDAY:
                    dayOfWeekName = "Thứ Ba";
                    break;
                case Calendar.WEDNESDAY:
                    dayOfWeekName = "Thứ Tư";
                    break;
                case Calendar.THURSDAY:
                    dayOfWeekName = "Thứ Năm";
                    break;
                case Calendar.FRIDAY:
                    dayOfWeekName = "Thứ Sáu";
                    break;
                case Calendar.SATURDAY:
                    dayOfWeekName = "Thứ Bảy";
                    break;
            }
            timeDay.setDayOfWeek(dateString);
            timeDay.setDayOfWeekName(dayOfWeekName);
            list.add(timeDay);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public static String changeReverDateTime(String inputDate,boolean reverse){
        SimpleDateFormat inputFormat = new SimpleDateFormat((reverse)?"dd-MM-yyyy":"yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat((reverse)?"yyyy-MM-dd":"dd-MM-yyyy");
        try {
            Date date = inputFormat.parse(inputDate);

            String formattedDate = outputFormat.format(date);

            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeNow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String timeNow=dateFormat.format(cal.getTime());
        return timeNow;
    }


    public static double getCoefficient(String time, Setting setting, String dayName){
        String day=time.substring(0,time.length()-4);
        if(day.equals("01-01") || day.equals("10-03") || day.equals("30-04") || day.equals("01-05") || day.equals("02-09")){
            return setting.getHoliday();
        }
        if(dayName.equals("Thứ Bảy") || dayName.equals("Chủ Nhật")){
            return setting.getDayOff();
        }
        return 1;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getNumberWorking(User user, String day){

        String timeStart="",timeEnd="";
        for(int i=0;i<user.getListTimeIns().size();i++){
            if(user.getListTimeIns().get(i).getDayIn().equals(day)){
                timeStart=user.getListTimeIns().get(i).getTimeIn();
                break;
            }
        }

        for(int i=0;i<user.getListTimeOuts().size();i++){
            if(user.getListTimeOuts().get(i).getDayOut().equals(day)){
                timeEnd=user.getListTimeOuts().get(i).getTimeOut();
            }
        }

        if(timeEnd.length()>0 && timeStart.length()>0) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            LocalTime localTime1 = LocalTime.parse(timeStart, formatter);
            LocalTime localTime2 = LocalTime.parse(timeEnd, formatter);

            Duration duration = Duration.between(localTime1, localTime2);

            return (long) duration.toHours();
        }
        return 0;
    }

    public static String formatWage(String wage){
        int t=wage.length();
        String wageFormat=(t<=3)?wage:wage.substring(t-3,t);
        t-=3;
        while(t-3>=0){
            wageFormat=wage.substring(t-3,t)+"."+wageFormat;
            t-=3;
        }
        return wageFormat;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static double getWageOfDay(User user, String day, Setting setting,String dayName) {

        double wage = 0;
        long hours = 0;
        double wageOneDay = user.getWage() * getCoefficient(day, setting, dayName);

        hours = getNumberWorking(user, day);
        hours--;
        if (hours < 8 && hours >= 4) {
            wage = wageOneDay / 2;
        } else {
            if (hours >= 8) {
                wage = wageOneDay + (hours - 8) * setting.getOvertime() * (wageOneDay / 8);
            }
        }

        return wage;

    }

}