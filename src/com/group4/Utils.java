package com.group4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public enum Task {
        CrawlWebsite,
        DeleteCacheAndReCrawl,
        RankPage,
        WordSuggestion,
        AutoComplete,
        Exit,
    }

    public static final String REGEX_DISTANCE_TRAVELLED = "\\d+"; // Matches positive integers
    public static final String REGEX_URL = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}([-a-zA-Z0-9@:%._\\+~#?&//=]*)";
    public static final String REGEX_MODEL_YEAR = "\\d{4}"; // Matches four-digit numbers
    public static final String REGEX_COMPANY = "[a-zA-Z\\s]+"; // Matches alphabets and spaces
    public static final String REGEX_PRICE = "\\d+(\\.\\d+)?"; // Matches positive decimal numbers
    public static final String REGEX_EMAIL = "[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}"; // Matches email format
    public static final String REGEX_USERNAME = "^[wd]+$"; // Matches any AlphaNumeric Character
    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"; // Matches a password
    public static final String REGEX_CANADIAN_CURRENCY = "(\\$\\d+(\\.\\d{2})?)|(\\d+(\\.\\d{2})?\\$)"; // Matches Canadian Currency $xx.yy or xx.yy$
    public static final String REGEX_CANADIAN_POSTAL_CODES = "([A-Z][0-9][A-Z] [0-9][A-Z][0-9])"; // Matches Canadian Postal codes
    public static final String REGEX_CANADIAN_PHONE_NUMBER = "(\\(\\d{3}\\) \\d{3}-\\d{4})|(\\d{3}-\\d{3}-\\d{4})|(\\d{3} \\d{3} \\d{4})";

    /**
     * Generates a bag of words from the provided string after cleaning it and making it case in-sensitive
     * @param data The input raw text
     * @return Bag of Words
     * @author Akshat Soni
     **/
    public static List<String> generateBagOfWords(String data) {
        // Returns if the input is null or empty
        if (data == null || data.trim().isEmpty()) {
            return null;
        }
        data = cleanText(data);

        // Creating a words array to store each word from the clean text
        List<String> words = Arrays.stream(data.split("\\s+")).toList();

        return words;
    }

    /**
     * Clean the input text by removing anything except alphanumeric values and white space
     * and make the text case in sensitive by making it to lowercase
     *
     * @param text The input raw text
     * @return The lowercase alphanumeric clean text
     * @author Akshat Soni
     **/
    private static String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text.toLowerCase().replaceAll("[^\\w\\s]", "");
    }

    /**
     * Reads a .txt file and returns a string
     * with all the content of that .txt file
     *
     * @param filePath The path to the .txt file to read.
     * @return The content of the file as a string.
     * @author Akshat Soni
     */
    public static String readTextFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    /**
     * @param url Url to check if it's valid
     * @return true if it's valid url and false if non valid url
     * @author Akshat Soni
     * */
    public static boolean isUrlValid(String url) {
        return Pattern.matches(Utils.REGEX_URL, url);
    }
}

