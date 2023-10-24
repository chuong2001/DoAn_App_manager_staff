package com.example.managerstaff.supports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.managerstaff.R;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.activities.LoginActivity;
import com.example.managerstaff.models.CalendarA;
import com.example.managerstaff.models.Comment;
import com.example.managerstaff.models.Image;
import com.example.managerstaff.models.Part;
import com.example.managerstaff.models.Position;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.Setting;
import com.example.managerstaff.models.StatisticalTimeUser;
import com.example.managerstaff.models.TypeCalendar;
import com.example.managerstaff.models.TypePost;
import com.example.managerstaff.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Support {

    public static String UrlMain="http:/192.168.11.103:8000/";

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

    public static boolean checkImageAddInPost(List<Image> list,Image image){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getIdImage()==image.getIdImage()){
                return false;
            }
        }
        return true;
    }

    public static boolean checkCommentOfUser(Post post, int idUser){
        for(int i=0;i<post.getListComments().size();i++){
            if(post.getListComments().get(i).getIdUser()==idUser) return true;
        }
        return false;
    }

    public static String getAuthorization(Activity activity){
        Database database=new Database(activity);
        return database.getSessionAuthorization();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String passwordEncryption(String originalData){
        byte[] encodedBytes = Base64.getEncoder().encode(originalData.getBytes());
        String encodedData = new String(encodedBytes);
        return encodedData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptPassword(String encodedData){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
        String decodedData = new String(decodedBytes);
        return decodedData;
    }


    public static void logOutExpiredToken(Activity activity){
        deleteAuthorization(activity);
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void deleteAuthorization(Activity activity){
        Database database=new Database(activity);
        database.deleteSession(database.getSessionAuthorization());
    }

    public static void showDialogWarningExpiredAu(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_notify_expired);

        TextView txtConfirm=dialog.findViewById(R.id.txt_btn_confirm);
        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logOutExpiredToken(activity);
            }
        });

        dialog.show();

    }

    public static void showDialogWarning(Activity activity,String title){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_notify_expired);

        TextView txtConfirm=dialog.findViewById(R.id.txt_btn_confirm);
        TextView txtTitle=dialog.findViewById(R.id.title);
        txtTitle.setText(title);
        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logOutExpiredToken(activity);
            }
        });

        dialog.show();

    }

    public static double convertStringToDouble(String wage){
        String wageNew="";
        for(int i=0;i<wage.length();i++){
            if(wage.charAt(i)!='.'){
                wageNew+=wage.charAt(i);
            }
        }
        return Double.parseDouble(wageNew);

    }

    public static MultipartBody.Part getBytesFromInputStream(Uri uri, Activity activity) throws IOException {

        if((uri.toString().length()>0 && uri.toString().substring(0,4).equals("http")) || uri.toString().length()==0){
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "Nội dung mặc định");
            MultipartBody.Part part = MultipartBody.Part.createFormData("key", "filename", requestBody);
            return part;
        }

        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteBuffer.toByteArray());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", System.currentTimeMillis()+".jpg", requestFile);
        return body;
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

    public static int compareToDate(String timeStart,String timeEnd){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date startDate = dateFormat.parse(timeStart);
            Date endDate = dateFormat.parse(timeEnd);

            if (startDate.compareTo(endDate)==0) {
                return 0;
            }else{
                if (startDate.compareTo(endDate)<0) {
                    return -1;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int getIdTypePost(List<TypePost> list, String name){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getTypeName().equals(name)) return list.get(i).getIdType();
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean compareToTime(String timeStart, String timeEnd){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime startTime = LocalTime.parse(timeStart, formatter);
        LocalTime endTime = LocalTime.parse(timeEnd, formatter);

        if (startTime.isAfter(endTime)) return false;
        return true;
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

    public static int getIndexOfName(List<String> list,String name){
        for(int i=0;i<list.size();i++){
            if(list.get(i).equalsIgnoreCase(name)){
                return i;
            }
        }
        return 0;
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
        return ((t>0)?wage.substring(0,t)+".":"")+wageFormat;
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

    public static List<String> getListTypePost(List<TypePost> listTypePosts,boolean isFillter){
        List<String> listType=new ArrayList<>();
        if(isFillter) {
            listType.add("Tất cả");
        }
        for(int i=0;i<listTypePosts.size();i++){
            listType.add(listTypePosts.get(i).getTypeName());
        }
        return listType;
    }


    public static String convertListImageToString(List<Image> list){
        String str="";
        for(int i=0;i<list.size();i++){
            str+=list.get(i).getImage()+" ";
        }
        return str.trim();
    }

    public static boolean checkEqualsString(String parent,String str){
        for(int i=0;i<parent.length()-str.length()+1;i++){
            if(parent.substring(i,i+str.length()).equalsIgnoreCase(str)){
                return true;
            }
        }
        return false;
    }

    public static String getFormatString(int day, int month, int year){
        return ((day<10)?"0":"")+day+"-"+((month<10)?"0":"")+month+"-"+year;
    }

    public static boolean checkIsWithinApprox(String timeStart,String timeEnd,String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date startDate = dateFormat.parse(timeStart);
            Date endDate = dateFormat.parse(timeEnd);
            Date checkDate = dateFormat.parse(time);

            if (checkDate.compareTo(startDate) >= 0 && checkDate.compareTo(endDate) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Post> filterPost(List<Post> listPosts, String timeStart, String timeEnd, String type){
        if(timeStart.length()==0 || timeEnd.length()==0 || type.length()==0) return  listPosts;
        List<Post> list=new ArrayList<>();
        for(int i=0;i<listPosts.size();i++){
            if((listPosts.get(i).getTypePost().equals(type)||type.equals("Tất cả")) && checkIsWithinApprox(timeStart,timeEnd,listPosts.get(i).getTimePost().substring(0,10)) ){
                list.add(listPosts.get(i));
            }
        }
        return list;
    }

    public static List<Post> searchListPosts(List<Post> list,String keysearch){
        if(keysearch.length()==0) return list;
        List<Post>listPostSearchs=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(Support.checkEqualsString(list.get(i).getHeaderPost(),keysearch)){
                listPostSearchs.add(list.get(i));
            }
        }
        return listPostSearchs;
    }

    public static List<User> searchListUsers(List<User> list,String keysearch){
        if(keysearch.length()==0) return list;
        List<User>listUserSearchs=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(Support.checkEqualsString(list.get(i).getFullName(),keysearch)){
                listUserSearchs.add(list.get(i));
            }
        }
        return listUserSearchs;
    }

    public static List<String> getNamePart(List<Part> list){
        List<String>listNamePart=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listNamePart.add(list.get(i).getNamePart());
        }
        return listNamePart;
    }

    public static List<String> getNameTypeCalendar(List<TypeCalendar> list){
        List<String>listNameCalendar=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listNameCalendar.add(list.get(i).getTypeName());
        }
        return listNameCalendar;
    }

    public  static void showDialogWarningSetTimeDay(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("Thời gian bạn chọn không hợp lệ.");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    public  static void showDialogNotifySaveSuccess(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("Lưu thành công.");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    public  static void showDialogNotifyUpdateSuccess(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("Cập nhật thành công.");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    public  static void showDialogNotifyUpdateFalse(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("Cập nhật không thành công.");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    public  static void showDialogNotifySaveFalse(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("Lưu không thành công.");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    public  static void showDialogWarningSaveCalender(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("Xin lỗi! Thời gian diễn ra sự kiện đã bị trùng.");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    public static List<String> getNamePosition(List<Position> list){
        List<String>listNamePosition=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listNamePosition.add(list.get(i).getNamePosition());
        }
        return listNamePosition;
    }

    public static String getDayNow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String timeNow=dateFormat.format(cal.getTime());
        return timeNow;
    }

    public static String getTimeNow2(){
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedTime = sdf.format(currentTime);
        return formattedTime;
    }

    public static String defineTime(String time){
        String str[]=time.split("-");
        return "Ngày " +str[0]+" Tháng "+str[1];
    }

    public static String defineTimeDetail(String time){
        String str[]=time.split("-");
        return "Ngày " +str[0]+" Tháng "+str[1]+" Năm "+str[2];
    }


}
