package com.group4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SeleniumDriver {
    static final List<Car> listCars = new ArrayList<>();

    static void CrawlKijijiAuto() {
        String url = "https://www.kijijiautos.ca/cars/#od=down&sb=rel";

        if (!Utils.isUrlValid(url)) {
            CustomPrint.printError("Selenium Parser", STR."Invalid Url to parse: \{url}");
            return;
        }

        CustomPrint.println("Parsing", url);

        // Create a new ChromeDriver
        WebDriver driver = new ChromeDriver();

        String folderPath = Utils.htmlCacheFolder;

        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");
        String fileName = STR."Cache_\{webSiteName}.html";
        File file = new File(folderPath + fileName);
        if (file.exists()) {
            CustomPrint.println("Selenium", STR."File Cache Exists :: \{fileName}");
            url = "file://" + file.getAbsolutePath();
        } else {
            try {
                Document document = Jsoup.connect(url).get();
                String htmlCode = document.html();

                File webPageFolder = new File(folderPath);

                if (!webPageFolder.exists()) {
                    Files.createDirectories(Paths.get(folderPath));
                }

                if (webPageFolder.exists() && webPageFolder.isDirectory()) {
                    try (FileWriter writer = new FileWriter(folderPath + fileName)) {
                        writer.write(htmlCode);
                    }
                    CustomPrint.println("Selenium", STR."Crawled File Saved: \{fileName}");
                }

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
            listCars.add(new Car(name, Integer.parseInt(price), fType, tType, Integer.parseInt(kmsDriven), imgUrl));
        }

        // exit the Chrome window
        driver.quit();
    }

    public static List<Car> crawlListCars() {
        CrawlKijijiAuto();
        return listCars;
    }

    public static void main(String[] args) {
        CrawlKijijiAuto();
    }
}