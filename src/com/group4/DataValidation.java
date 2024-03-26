package com.group4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
    public static boolean validateDistanceTravelled(String input) {
        int distance = Integer.parseInt(input);
        return distance >= 0 && distance <= 1000000;
    }

    public static boolean validateModelYear(String input) {
        int year = Integer.parseInt(input);
        return year >= 2000 && year <= 2024;
    }

    public static boolean validateCompany(String input) {
        Pattern pattern = Pattern.compile(Utils.REGEX_COMPANY);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean validatePrice(String input) {
        double price = Double.parseDouble(input);
        return price >= 500 && price <= 900000;
    }

    public static boolean validateEmail(String input) {
        Pattern pattern = Pattern.compile(Utils.REGEX_EMAIL);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean validateUsername(String input) {
        Pattern pattern = Pattern.compile(Utils.REGEX_USERNAME);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean validatePassword(String input) {
        Pattern pattern = Pattern.compile(Utils.REGEX_PASSWORD);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void main(String[] args) {
        // Use Jsoup to scrape data from the website
        try {
            Document doc = Jsoup.connect("http://your-website-url.com").get();
            Element distanceElement = doc.getElementById("distance-id"); // Replace "distance-id" with the actual ID of the distance input element
            String distanceValue = distanceElement.val(); // Assuming the input value is stored in a "value" attribute

            Element modelYearElement = doc.getElementById("year-id"); // Replace "year-id" with the actual ID of the year input element
            String modelYearValue = modelYearElement.val();

            Element companyElement = doc.getElementById("company-id"); // Replace "company-id" with the actual ID of the company input element
            String companyValue = companyElement.val();

            Element priceElement = doc.getElementById("price-id"); // Replace "price-id" with the actual ID of the price input element
            String priceValue = priceElement.val();

            Element emailElement = doc.getElementById("email-id"); // Replace "email-id" with the actual ID of the email input element
            String emailValue = emailElement.val();

            Element usernameElement = doc.getElementById("username-id"); // Replace "username-id" with the actual ID of the username input element
            String usernameValue = usernameElement.val();

            Element passwordElement = doc.getElementById("password-id"); // Replace "password-id" with the actual ID of the password input element
            String passwordValue = passwordElement.val();

            // Validate inputs
            if (validateDistanceTravelled(distanceValue)) {
                CustomPrint.println(STR."Distance travelled input is valid: \{distanceValue}");
            } else {
                CustomPrint.println(STR."Invalid distance travelled input: \{distanceValue}");
            }

            if (validateModelYear(modelYearValue)) {
                CustomPrint.println(STR."Model year input is valid: \{modelYearValue}");
            } else {
                CustomPrint.println(STR."Invalid model year input: \{modelYearValue}");
            }

            if (validateCompany(companyValue)) {
                CustomPrint.println(STR."Company input is valid: \{companyValue}");
            } else {
                CustomPrint.println(STR."Invalid company input: \{companyValue}");
            }

            if (validatePrice(priceValue)) {
                CustomPrint.println(STR."Price input is valid: \{priceValue}");
            } else {
                CustomPrint.println(STR."Invalid price input: \{priceValue}");
            }

            if (validateEmail(emailValue)) {
                CustomPrint.println(STR."Email input is valid: \{emailValue}");
            } else {
                CustomPrint.println(STR."Invalid email input: \{emailValue}");
            }

            if (validateUsername(usernameValue)) {
                CustomPrint.println(STR."Username input is valid: \{usernameValue}");
            } else {
                CustomPrint.println(STR."Invalid username input: \{usernameValue}");
            }

            if (validatePassword(passwordValue)) {
                CustomPrint.println(STR."Password input is valid: \{passwordValue}");
            } else {
                CustomPrint.println(STR."Invalid password input: \{passwordValue}");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
