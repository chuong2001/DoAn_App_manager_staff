package com.example.managerstaff.supports;

import com.example.managerstaff.models.StatisticalTimeUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
