package com.example.flascash.validator;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static boolean isStrongPassword(String password) {
        return Pattern.matches(STRONG_PASSWORD_REGEX, password);
    }

    public static void main(String[] args) {
        String password1 = "StrongPass1!";
        String password2 = "weakpass";

        System.out.println("Password 1 is strong: " + isStrongPassword(password1)); // true
        System.out.println("Password 2 is strong: " + isStrongPassword(password2)); // false
    }
}

