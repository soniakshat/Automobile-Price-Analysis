package com.group4;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public static String[] CrawlUrls = {"https://www.motorcitychrysler.ca/used/", "https://www.kijijiautos.ca/cars/#od=down&sb=rel", "https://www.dashmotors.ca/inventory"};;

    public enum SearchType {
        Name,
        Fuel,
        Transmission,
        Price,
        ImageAvailable,
        KmsDriven,
        Stats,
        Exit, DeleteCacheAndRefreshData,
    }

    public static final String htmlCacheFolder = ".\\res\\generated\\pages\\";
    public static final String jsonCacheFolder = ".\\res\\generated\\json\\";
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
     *
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
     * Generates a bag of words from provided List of Car
     * @param carList List of Car objects
     * @author Akshat Soni
     * */
    public static List<String> generateBagOfWords(List<Car> carList) {
        // Returns if the input is null or empty
        if (carList == null || carList.isEmpty()) {
            return null;
        }
        StringBuilder carData = new StringBuilder();

        for (Car car : carList) {
            carData.append(STR." \{car.getName()}");
            carData.append(STR." \{car.getFuelType()}");
            carData.append(STR." \{car.getKmsDriven()}");
            carData.append(STR." \{car.getTransmissionType()}");
        }

        String data = cleanText(carData.toString());

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

        if (!filePath.endsWith(".txt")) {
            CustomPrint.printError("Read File for Text", STR."Provided File is not a txt file!\n\{filePath}");
        }

        File file = new File(filePath);

        if (!file.exists()) {
            CustomPrint.printError("Read File for Text", "File does not exist");
            return "";
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
     */
    public static boolean isUrlValid(String url) {
        return Pattern.matches(Utils.REGEX_URL, url);
    }

    public static boolean createDirectoryIfNotExist(String path) {
        Path dirPath = Paths.get(path);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectory(dirPath);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Reads the json file and provides a string
     * @param path path to json file
     * @return string of the data of json file
     * @author Akshat Soni
     * */
    public static String readJsonFile(String path) {
        String jsonData = "";
        try {
            jsonData = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    /**
     * Deletes all folder from the provided path
     * @param folderPath path of folder to be deleted
     * @author Akshat Soni
     * */
    public static void deleteAllFilesInFolder(String folderPath) {
        Path folder = Paths.get(folderPath);
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            CustomPrint.printError(STR."Invalid folder path: \{folderPath}");
            return;
        }

        try {
            Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            CustomPrint.printError((STR."Error deleting file \{path.toString()}: \{e.getMessage()}"));
                        }
                    });
            CustomPrint.println(STR."All files in the folder \{folderPath} have been deleted.");
        } catch (IOException e) {
            CustomPrint.printError((STR."Error deleting files in the folder \{folderPath}: \{e.getMessage()}"));
        }
    }

    /**
     * Checks if the jsonFile exists or not
     * @author Akshat Soni
     * */
    public static boolean isJsonFileExists(String fileName) {
        File file = new File(Utils.jsonCacheFolder + fileName);
        return file.exists() && file.isFile() && file.getName().endsWith(".json");
    }

    /**
     * Creates a json file on provided
     * @param fileName name of json file to be created
     * @param jsonArray json array which is to be stored in json file
     * @author Akshat Soni
     * */
    public static void createJsonFile(String fileName, JSONArray jsonArray) {
        File file = new File(jsonCacheFolder + fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            File jsonFolder = new File(Utils.jsonCacheFolder);

            if (!jsonFolder.exists()) {
                Files.createDirectories(Paths.get(Utils.jsonCacheFolder));
            }

            if (jsonFolder.exists() && jsonFolder.isDirectory()) {
                try (FileWriter writer = new FileWriter(Utils.jsonCacheFolder + fileName)) {
                    writer.write(jsonArray.toString(4));
                    fileWriter.flush();
                }
                CustomPrint.println("Selenium Json", STR."Json Data of Cars Saved: \{fileName}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads a json file for array of car objects from provided path
     * @param filePath path to the json flie
     * @return list of car objects
     * @author Akshat Soni
     * */
    public static List<Car> readJsonFileToCarList(String filePath) {
        List<Car> carList = new ArrayList<>();

        String jsonData = Utils.readJsonFile(filePath);

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            int price = jsonObject.getInt("price");
            FuelType fuelType = FuelType.valueOf(jsonObject.getString("fuelType"));
            TransmissionType transmissionType = TransmissionType.valueOf(jsonObject.getString("transmission"));
            int kms = jsonObject.getInt("kms");
            String imgUrl = jsonObject.getString("image");
            imgUrl = imgUrl == null || imgUrl.isEmpty() ? "" : imgUrl;
            carList.add(new Car(name, price, fuelType, transmissionType, kms, imgUrl));
        }
        return carList;
    }

    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String readHtmlFile(String filePath) {
        File htmlFile = new File(filePath);
        StringBuilder htmlContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(htmlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
                htmlContent.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlContent.toString();
    }
}

