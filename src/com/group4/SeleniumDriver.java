package com.group4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SeleniumDriver {
    static final List<Car> listCars = new ArrayList<>();

    static void CrawlKijijiAuto() {
        String url = "https://www.kijijiautos.ca/cars/#od=down&sb=rel";
        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

        String htmlCacheFolderPath = Utils.htmlCacheFolder;
        String jsonCacheFolderPath = Utils.jsonCacheFolder;

        String htmlCacheFileName = STR."Cache_\{webSiteName}.html";
        String jsonCacheFileName = STR."\{webSiteName}.json";

        String jsonPath = jsonCacheFolderPath + jsonCacheFileName;
        String htmlPath = htmlCacheFolderPath + htmlCacheFileName;

        if (!Utils.isUrlValid(url)) {
            CustomPrint.printError("Selenium Parser", STR."Invalid Url to parse: \{url}");
            return;
        }

        CustomPrint.println("Parsing", url);

        // If json exist then read from that else create json
        if (isJsonFileExists(jsonCacheFileName)) {
            CustomPrint.println("JsonCache", STR."Found Json Cache \{jsonCacheFileName}");
            List<Car> jsonCars = readJsonFileToCarList(jsonPath);
            listCars.addAll(jsonCars);
        } else {
            // Create a new ChromeDriver
            WebDriver driver = new ChromeDriver();

            File file = new File(htmlPath);

            // If .html cache exist then read that else goto website
            if (file.exists()) {
                CustomPrint.println("Selenium", STR."File Cache Exists :: \{htmlCacheFileName}");
                url = "file://" + file.getAbsolutePath();
            } else {
                // create .html cache here
                try {
                    Document document = Jsoup.connect(url).get();
                    String htmlCode = document.html();

                    Utils.createDirectoryIfNotExist(htmlCacheFolderPath);

                    try (FileWriter writer = new FileWriter(htmlCacheFolderPath + htmlCacheFileName)) {
                        writer.write(htmlCode);
                    }
                    CustomPrint.println("Selenium", STR."Crawled File Saved: \{htmlCacheFileName}");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            CustomPrint.println(STR."Trying to open: \{url}");
            // Open the provided page on chrome
            driver.get(url);

            // minimize the chrome window
            driver.manage().window().minimize();

            // Create a WebDriverWait object with a timeout of 10 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

//        CustomPrint.println(STR."Data: \{url}", driver.findElement(By.tagName("html")).getAttribute("innerHTML"));
            WebElement mainElement = driver.findElement(By.cssSelector("div.bF7UGW"));
            List<WebElement> carElements = wait.until(d -> d.findElements(By.cssSelector("div.ccN7dZ")));

            List<Car> kijijiCars = new ArrayList<>();

            for (WebElement ele : carElements) {
                String imgUrl, name, kmsDriven, location, transmissionType, fuelType, price;
                WebElement image = ele.findElement(By.cssSelector("img.q1E1YI"));
                imgUrl = image.getAttribute("src");
                WebElement otherDetails = ele.findElement(By.cssSelector("div.ecN7dZ"));
                name = otherDetails.findElement(By.cssSelector("h2.G2jAym.fcN7dZ.z2jAym.p2jAym.b2jAym")).getText();

                List<WebElement> det = otherDetails.findElements(By.cssSelector("li.c3n44x"));
                List<String> dets = new ArrayList<>();
                for (WebElement x : det) {
                    dets.add(x.findElement(By.cssSelector("span.G2jAym.y2jAym.q2jAym")).getText());
                }

                kmsDriven = dets.get(0).replaceAll("\\D", "");
                kmsDriven = kmsDriven.isEmpty() ? "-1" : kmsDriven;
                location = dets.get(1);
                transmissionType = dets.get(2);
                fuelType = dets.get(3);

                WebElement priceElement = ele.findElement(By.cssSelector("div.mcN7dZ"));
                price = priceElement.findElement(By.cssSelector("span.G2jAym.d3uM7V.C2jAym.p2jAym.b2jAym")).getText();
                price = price.replaceAll("\\D", "");
                price = price.isEmpty() ? "-1" : price;

//            CustomPrint.println("");
//            CustomPrint.println("Car Details", STR."Name: \{name} \tPrice: $\{price}\nFuel: \{fuelType}\tTransmission: \{transmissionType}\nKms Driven: \{kmsDriven} km\nUrl: \{imgUrl}\nLocation: \{location}");

                FuelType fType;
                if (fuelType.equalsIgnoreCase("gas")) {
                    fType = FuelType.Gas;
                } else if (fuelType.equalsIgnoreCase("electric")) {
                    fType = FuelType.Electric;
                } else if (fuelType.equalsIgnoreCase("hybrid")) {
                    fType = FuelType.Hybrid;
                } else if (fuelType.equalsIgnoreCase("diesel")) {
                    fType = FuelType.Diesel;
                } else {
                    fType = FuelType.Other;
                }

                TransmissionType tType;
                if (transmissionType.equalsIgnoreCase("automatic")) {
                    tType = TransmissionType.Automatic;
                } else if (transmissionType.equalsIgnoreCase("manual")) {
                    tType = TransmissionType.Manual;
                } else {
                    tType = TransmissionType.NA;
                }

                kijijiCars.add(new Car(name, Integer.parseInt(price), fType, tType, Integer.parseInt(kmsDriven), imgUrl));

                listCars.addAll(kijijiCars);
            }

            JSONArray kijijiJsonCars = new JSONArray();
            for (Car car : kijijiCars) {
                kijijiJsonCars.put(car.getJsonObject());
            }
            createJsonFile(jsonCacheFileName, kijijiJsonCars);

            // exit the Chrome window
            driver.quit();
        }
    }

    public static List<Car> crawlListCars() {
        CrawlKijijiAuto();
        return listCars;
    }

    public static void main(String[] args) {
        CrawlKijijiAuto();
    }

    public static boolean isJsonFileExists(String fileName) {
        File file = new File(Utils.jsonCacheFolder + fileName);
        return file.exists() && file.isFile() && file.getName().endsWith(".json");
    }

    public static void createJsonFile(String fileName, JSONArray jsonArray) {
        File file = new File(fileName);
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
}