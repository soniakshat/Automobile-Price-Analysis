package com.group4;

public class Utils {
    public static final String REGEX_DISTANCE_TRAVELLED = "\\d+"; // Matches positive integers
    public static final String REGEX_MODEL_YEAR = "\\d{4}"; // Matches four-digit numbers
    public static final String REGEX_COMPANY = "[a-zA-Z\\s]+"; // Matches alphabets and spaces
    public static final String REGEX_PRICE = "\\d+(\\.\\d+)?"; // Matches positive decimal numbers
    public static final String REGEX_EMAIL = "[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}"; // Matches email format
    public static final String REGEX_USERNAME = "^[wd]+$"; // Matches any AlphaNumeric Character
    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"; // Matches a password
    public static final String REGEX_CANADIAN_CURRENCY = "(\\$\\d+(\\.\\d{2})?)|(\\d+(\\.\\d{2})?\\$)"; // Matches Canadian Currency $xx.yy or xx.yy$
    public static final String REGEX_CANADIAN_POSTAL_CODES = "([A-Z][0-9][A-Z] [0-9][A-Z][0-9])"; // Matches Canadian Postal codes
    public static final String REGEX_CANADIAN_PHONE_NUMBER = "(\\(\\d{3}\\) \\d{3}-\\d{4})|(\\d{3}-\\d{3}-\\d{4})|(\\d{3} \\d{3} \\d{4})";
}
