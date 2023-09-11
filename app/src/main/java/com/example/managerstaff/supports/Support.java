package com.example.managerstaff.supports;

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

}
