package com.group4;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse website and store the car data
 *
 * @author Akshat Soni, Haseeb Shams
 */
public class HtmlParsing {
    static final List<Car> listCars = new ArrayList<>();
    static final String htmlCacheFolderPath = Utils.htmlCacheFolder;
    static final String jsonCacheFolderPath = Utils.jsonCacheFolder;

    static void CrawlDashMotors() {
        String url = "https://www.dashmotors.ca/inventory";
        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

        String htmlCacheFileName = STR."Cache_\{webSiteName}.html";
        String jsonCacheFileName = STR."\{webSiteName}.json";

        String jsonPath = jsonCacheFolderPath + jsonCacheFileName;
        String htmlFilePath = htmlCacheFolderPath + htmlCacheFileName;

        List<Car> dashMotorsCars = new ArrayList<>();

        if (!Utils.isUrlValid(url)) {
            CustomPrint.printError("Jsoup Parser", STR."Invalid Url to parse: \{url}");
            return;
        }

        CustomPrint.println("Parsing", url);

        // If json exist then read from that else create json
        if (Utils.isJsonFileExists(jsonCacheFileName)) {
            CustomPrint.println("JsonCache", STR."Found Json Cache \{jsonCacheFileName}");
            List<Car> jsonCars = Utils.readJsonFileToCarList(jsonPath);
            listCars.addAll(jsonCars);
        } else {
            // Fetch Data from stored html
            try {
                String htmlContent = Utils.readHtmlFile(htmlFilePath);
                Document doc = Jsoup.parse(htmlContent);

                Elements carListings = doc.select("li.listing-vechile-one");

                for (Element listing : carListings) {
                    String name = listing.select("h2.eziVehicleName a").text();

                    String imageUrl = listing.select("div.veh-img-placeholder img.img-list-respnsive").attr("src");
                    if(!Utils.isUrlValid(imageUrl)) {
                        imageUrl = "NA";
                    }

                    String price = listing.select("span.eziPriceValue").text();
                    price = price.replaceAll("\\D", "");
                    price = price.isEmpty() ? "-1" : price;

                    String transmissionType = listing.select("ul li:contains(Transmission)").text();

                    String fuelType = listing.select("ul li:contains(Fuel)").text();

//                    CustomPrint.println(STR."""
//
//                            Name: \{name}
//                            Price: \{price}
//                            Transmission: \{transmissionType}
//                            Fuel: \{fuelType}
//                            Image: \{imageUrl}
//
//                            """);

                    FuelType fType = getFuel(fuelType);
                    TransmissionType tType = getTransmission(transmissionType);

                    Car car = new Car(name, Integer.parseInt(price), fType, tType, -1, imageUrl);
                    dashMotorsCars.add(car);
                }

                JSONArray jsonArray = new JSONArray();
                for (Car car : dashMotorsCars) {
                    jsonArray.put(car.getJsonObject());
                }

                Utils.createJsonFile(jsonCacheFileName, jsonArray);

            } catch (org.json.JSONException e) {
                System.err.println("An error occurred while processing JSON: " + e.getMessage());
            }
        }
    }

    static void CrawlKijiji() {
        String url = "https://www.kijijiautos.ca/cars/#od=down&sb=rel";
        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

        String htmlCacheFileName = STR."Cache_\{webSiteName}.html";
        String jsonCacheFileName = STR."\{webSiteName}.json";

        String jsonPath = jsonCacheFolderPath + jsonCacheFileName;
        String htmlFilePath = htmlCacheFolderPath + htmlCacheFileName;

        List<Car> cars = new ArrayList<>();

        if (!Utils.isUrlValid(url)) {
            CustomPrint.printError("Jsoup Parser", STR."Invalid Url to parse: \{url}");
            return;
        }

        CustomPrint.println("Parsing", url);

        // If json exist then read from that else create json
        if (Utils.isJsonFileExists(jsonCacheFileName)) {
            CustomPrint.println("JsonCache", STR."Found Json Cache \{jsonCacheFileName}");
            List<Car> jsonCars = Utils.readJsonFileToCarList(jsonPath);
            listCars.addAll(jsonCars);
        } else {
            // Fetch Data from stored html
            try {
                String htmlContent = Utils.readHtmlFile(htmlFilePath);

                Document doc = Jsoup.parse(htmlContent);

                Elements carElements = doc.select("article[data-testid=SearchResultListItem]");

                for (Element carElement : carElements) {
                    String name = carElement.select("h2.fcN7dZ").text();

                    String imageUrl = carElement.select("figure[data-testid=VehicleListItem-figure] img").attr("src");
                    if(!Utils.isUrlValid(imageUrl)) {
                        imageUrl = "NA";
                    }
                    String price = carElement.select("div.g3uM7V[data-testid=VehicleListItem-price] span[data-testid=searchResultItemPrice]").first().text().replaceAll("\\*$", "");
                    price = price.replaceAll("\\D", "");
                    price = price.isEmpty() ? "-1" : price;


                    Elements details = carElement.select("ul.b3n44x li span[data-testid=VehicleListItemAttributeValue]");

                    String kilometers = details.size() > 0 ? details.get(0).text().replaceAll("\\D", "") : "-1";
                    kilometers = kilometers.isEmpty() || kilometers == null ? "-1" : kilometers;
CustomPrint.print("Kms: "+kilometers);
//                    String location = details.size() > 1 ? details.get(1).text() : "N/A";

                    String transmissionType = details.size() > 2 ? details.get(2).text() : "N/A";
                    String fuelType = details.size() > 3 ? details.get(3).text() : "N/A";

                    FuelType fType = getFuel(fuelType);
                    TransmissionType tType = getTransmission(transmissionType);

                    CustomPrint.println(STR."""

                            Name: \{name}
                            Price: \{price}
                            Transmission: \{transmissionType}
                            Fuel: \{fuelType}
                            Image: \{imageUrl}
                            Kilometer: \{kilometers}

                            """);

                    Car car = new Car(name, Integer.parseInt(price), fType, tType, Integer.parseInt(kilometers), imageUrl);
                    cars.add(car);
                }

                JSONArray jsonArray = new JSONArray();
                for (Car car : cars) {
                    jsonArray.put(car.getJsonObject());
                }

                Utils.createJsonFile(jsonCacheFileName, jsonArray);

            } catch (org.json.JSONException e) {
                System.err.println("An error occurred while processing JSON: " + e.getMessage());
            }
        }
    }

    static void CrawlChrysler() {
        String url = "https://www.motorcitychrysler.ca/used/";

        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

        String htmlCacheFileName = STR."Cache_\{webSiteName}.html";
        String jsonCacheFileName = STR."\{webSiteName}.json";

        String jsonPath = jsonCacheFolderPath + jsonCacheFileName;
        String htmlFilePath = htmlCacheFolderPath + htmlCacheFileName;

        if (!Utils.isUrlValid(url)) {
            CustomPrint.printError("Jsoup Parser", STR."Invalid Url to parse: \{url}");
            return;
        }

        CustomPrint.println("Parsing", url);

        // If json exist then read from that else create json
        if (Utils.isJsonFileExists(jsonCacheFileName)) {
            CustomPrint.println("JsonCache", STR."Found Json Cache \{jsonCacheFileName}");
            List<Car> jsonCars = Utils.readJsonFileToCarList(jsonPath);
            listCars.addAll(jsonCars);
        } else {
            // Fetch Data from stored html
            try {
                String htmlContent = Utils.readHtmlFile(htmlFilePath);
                Document doc = Jsoup.parse(htmlContent);

                List<Car> cars = new ArrayList<>();

                Elements carListings = doc.select("div.vehicle-list-cell");

                for (Element carListing : carListings) {
                    String name = carListing.select("span[itemprop=model]").text();

                    String imageUrl = carListing.select("img[itemprop=image]").attr("src");
                    if(!Utils.isUrlValid(imageUrl)) {
                        imageUrl = "NA";
                    }
                    String price = carListing.select("span[itemprop=price]").text();
                    price = price.replaceAll("\\D", "");
                    price = price.isEmpty() ? "-1" : price;

                    String kmsDriven = carListing.select("span[itemprop=mileageFromOdometer] span[itemprop=value]").text();
                    kmsDriven = kmsDriven.replaceAll("\\D", "");
                    kmsDriven = kmsDriven.isEmpty() ? "-1" : kmsDriven;
                    String engine = carListing.select("td[itemprop=vehicleEngine]").text();

                    String transmissionType = carListing.select("td[itemprop=vehicleTransmission]").text();

                    String bodyStyle = carListing.select("td[itemprop=bodyType]").text();

                    String fuelType = "N/A";

                    FuelType fType = getFuel(fuelType);
                    TransmissionType tType = getTransmission(transmissionType);

//                    CustomPrint.println(STR."""
//
//                            Name: \{name}
//                            Price: \{price}
//                            Transmission: \{transmissionType}
//                            Fuel: \{fuelType}
//                            Image: \{imageUrl}
//                            Kms: \{kmsDriven}
//
//                            """);

                    Car car = new Car(name, Integer.parseInt(price), fType, tType, Integer.parseInt(kmsDriven), imageUrl);
                    cars.add(car);
                }

                JSONArray jsonArray = new JSONArray();
                for (Car car : cars) {
                    jsonArray.put(car.getJsonObject());
                }

                Utils.createJsonFile(jsonCacheFileName, jsonArray);

            } catch (org.json.JSONException e) {
                System.err.println("An error occurred while processing JSON: " + e.getMessage());
            }
        }
    }

    /**
     * Crawls different websites and fetch the car data and provide list of car object
     *
     * @return list of car object
     * @author Akshat Soni
     */
    public static List<Car> crawlListCars() {
        WebCrawler.CrawlWebsites();
        CrawlDashMotors();
        CrawlKijiji();
        CrawlChrysler();
        return listCars;
    }

    public static void main(String[] args) {
        Utils.createDirectoryIfNotExist(htmlCacheFolderPath);
        Utils.createDirectoryIfNotExist(jsonCacheFolderPath);

        CrawlDashMotors();
        CrawlKijiji();
        CrawlChrysler();
    }

    private static FuelType getFuel(String fuelType){
        FuelType fType ;
        if (fuelType.equalsIgnoreCase("gas") || fuelType.equalsIgnoreCase("gasoline")) {
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
        return fType;
    }

    private static TransmissionType getTransmission(String transmissionType) {
        TransmissionType tType;
        if (transmissionType.equalsIgnoreCase("automatic") || transmissionType.equalsIgnoreCase("auto")) {
            tType = TransmissionType.Automatic;
        } else if (transmissionType.equalsIgnoreCase("manual") || transmissionType.equalsIgnoreCase("stick")) {
            tType = TransmissionType.Manual;
        } else {
            tType = TransmissionType.NA;
        }
        return tType;
    }
}
