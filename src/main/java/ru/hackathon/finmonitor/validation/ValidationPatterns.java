package ru.hackathon.finmonitor.validation;

public class ValidationPatterns {

    public static final String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(20[0-9]{2})$";
    public static final String INN_PATTERN = "^\\d{11}$";
    public static final String PHONE_PATTERN = "^(\\+7|8)\\d{10}$";
} 