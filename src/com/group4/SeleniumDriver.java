package com.group4;

import org.json.JSONArray;
import org.json.JSONObject;
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

/**
 * Class to parse website and store the car data
 *
 * @author Akshat Soni
 */
public class SeleniumDriver {
    static final List<Car> listCars = new ArrayList<>();
    static final String htmlCacheFolderPath = Utils.htmlCacheFolder;
    static final String jsonCacheFolderPath = Utils.jsonCacheFolder;

    /**
     * Crawl kijiji auto website and store the car data in json file
     *
     * @author Akshat Soni
     */
    static void CrawlKijijiAuto() {
        String url = "https://www.kijijiautos.ca/cars/#od=down&sb=rel";
        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

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
        if (Utils.isJsonFileExists(jsonCacheFileName)) {
            CustomPrint.println("JsonCache", STR."Found Json Cache \{jsonCacheFileName}");
            List<Car> jsonCars = Utils.readJsonFileToCarList(jsonPath);
            listCars.addAll(jsonCars);
        } else {
            // Create a new ChromeDriver
            WebDriver driver = new ChromeDriver();

            File file = new File(htmlPath);

            // If .html cache exist then read that else goto website
            if (file.exists()) {
                CustomPrint.println("Selenium", STR."File Cache Exists :: \{htmlCacheFileName}");
                url = "file://" + file.getAbsolutePath();
            }

            CustomPrint.println(STR."Trying to open: \{url}");
            // Open the provided page on chrome
            driver.get(url);

            String htmlCode = driver.findElement(By.tagName("html")).getAttribute("innerHTML");

            if (!new File(htmlPath).exists()) {
                try (FileWriter writer = new FileWriter(htmlCacheFolderPath + htmlCacheFileName)) {
                    writer.write(htmlCode);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

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

            Utils.createJsonFile(jsonCacheFileName, kijijiJsonCars);

            // exit the Chrome window
            driver.quit();
        }
    }

    /**
     * Crawls different websites and fetch the car data and provide list of car object
     *
     * @return list of car object
     * @author Akshat Soni
     */
    public static List<Car> crawlListCars() {
        CrawlKijijiAuto();
        return listCars;
    }

    public static void main(String[] args) {
        Utils.createDirectoryIfNotExist(htmlCacheFolderPath);
        Utils.createDirectoryIfNotExist(jsonCacheFolderPath);
        CrawlKijijiAuto();
    }
}