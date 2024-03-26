package com.group4;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to find patterns using Regular Expressions
 *
 * @author Akshat Soni
 */
public class Patterns {
    public static void findEmails(String input) {
        if (input == null || input.isEmpty()) {
            return;
        }
        String pattern = Utils.REGEX_EMAIL;
        CustomPrint.println("Emails:");

        findPatterns(input, pattern);
    }

    public static void findCanadianPhoneNumbers(String input) {
        if (input == null || input.isEmpty()) {
            return;
        }
        String pattern = Utils.REGEX_CANADIAN_PHONE_NUMBER;
        CustomPrint.println("Canadian Phone Numbers:");
        findPatterns(input, pattern);
    }

    public static void findCanadianPostalCodes(String input) {
        if (input == null || input.isEmpty()) {
            return;
        }
        String pattern = Utils.REGEX_CANADIAN_POSTAL_CODES;
        CustomPrint.println("Canadian Postal Codes:");
        findPatterns(input, pattern);
    }

    public static void findCanadianCurrencyAmounts(String input) {
        if (input == null || input.isEmpty()) {
            return;
        }
        String pattern = Utils.REGEX_CANADIAN_CURRENCY;
        CustomPrint.println("Canadian Currency Amounts:");
        findPatterns(input, pattern);
    }

    public static void findPatterns(String input, String pattern) {
        if (input == null || pattern == null || input.isEmpty() || pattern.isEmpty()) {
            return;
        }

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        List<String> listResults = new ArrayList<>();
        while (matcher.find()) {
            listResults.add(matcher.group());
        }
        if(!listResults.isEmpty()){
            CustomPrint.println("Result", STR."\{listResults}\n");
        }
        else{
            CustomPrint.printError("Pattern", "No Pattern Found");
        }
    }

    public static void main(String[] args) {
        String input = """
                Hello, example@email.com
                test@example.com
                emailtest@example.org N9A 4N4 the price was 123.52$ or 123$? my email is example@example.com and my phone number is (123) 456-7890. My address is 123 Main St, Toronto, ON A1A 1A1. The price is $123.45, but we can give you a discount of $123 or 123.45.""";

        CustomPrint.println("Input", input);
        CustomPrint.print("\n");
        findEmails(input);
        findCanadianPhoneNumbers(input);
        findCanadianPostalCodes(input);
        findCanadianCurrencyAmounts(input);
    }
}
