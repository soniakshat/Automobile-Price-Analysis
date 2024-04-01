package com.group4;

import jdk.jshell.execution.Util;

import static java.lang.StringTemplate.STR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main class that will run everything1
 *
 * @author Akshat Soni
 */
public class Driver {
    private static final String[] urls = {"https://www.motorcitychrysler.ca/used/",
            "https://www.carpages.ca/ontario/windsor/used-cars/",
            "https://www.nawabmotors.ca/cars"};
    public static final Set<String> bagOfWords = new HashSet<>();
    private static final List<Car> listCars = new ArrayList<>();

    private static final SearchFrequency trackNameSearch = new SearchFrequency();
    private static final SearchFrequency trackFuelSearch = new SearchFrequency();
    private static final SearchFrequency trackTransmissionSearch = new SearchFrequency();

    public static void main(String[] args) {
        Utils.createDirectoryIfNotExist(Utils.htmlCacheFolder);
        Utils.createDirectoryIfNotExist(Utils.jsonCacheFolder);
//        basicIntegration();
        advanceIntegration();
    }

    /**
     * method that integrates all the functionality of the program
     *
     * @author Akshat Soni
     */
    private static void advanceIntegration() {

        CustomPrint.println("=".repeat(30));

        CustomPrint.println("Instructions", """
                This program will give you information about a car by crawling below mentioned websites.""");


        CustomPrint.println("=".repeat(30));

        listCars.addAll(SeleniumDriver.crawlListCars());

        bagOfWords.addAll(Utils.generateBagOfWords(listCars));

        WordCompletion.insertWordsForCompletion(bagOfWords);

        Utils.SearchType choice;
        do {
            choice = askForChoiceAdvance();

            switch (choice) {
                case Name -> {
                    CustomPrint.print("Enter a car name to search: ");
                    String carName = new Scanner(System.in).nextLine().toLowerCase();
                    String suggestedCarName = SpellChecking.checkSpelling(carName);

                    if (!suggestedCarName.equalsIgnoreCase(carName)) {
                        CustomPrint.print("Do you want to search for suggested name? [y/n]:  ");
                        String c = "";
                        c = new Scanner(System.in).next();
                        if (c.equalsIgnoreCase("y")) {
                            getCarByName(suggestedCarName);
                            trackNameSearch.trackSearch(suggestedCarName);
                            break;
                        } else if (c.equalsIgnoreCase("n")) {
                            getCarByName(carName);
                            trackNameSearch.trackSearch(carName);
                            break;
                        } else {
                            CustomPrint.printError("Spell Mistake Found", "Invalid Choice!");
                            break;
                        }
                    }
                    getCarByName(carName);
                    trackNameSearch.trackSearch(carName);
                }
                case Fuel -> {
                    CustomPrint.println("Select a fuel type from below: ");
                    for (int i = 0; i < FuelType.values().length; i++) {
                        CustomPrint.println(STR."\{i + 1}. \{FuelType.values()[i]}");
                    }
                    CustomPrint.print("Enter your choice: ");
                    int selectionFuelType = new Scanner(System.in).nextInt();

                    getCarByFuelType(selectionFuelType);
                    trackFuelSearch.trackSearch(String.valueOf(selectionFuelType));
                }
                case Transmission -> {
                    CustomPrint.println("Select a transmission type from below: ");
                    for (int i = 0; i < TransmissionType.values().length; i++) {
                        CustomPrint.println(STR."\{i + 1}. \{TransmissionType.values()[i]}");
                    }
                    CustomPrint.print("Enter your choice: ");
                    int selectionTransmissionType = new Scanner(System.in).nextInt();

                    getCarByTransmission(selectionTransmissionType);
                    trackTransmissionSearch.trackSearch(String.valueOf(selectionTransmissionType));
                }
                case Price -> {
                    CustomPrint.println("Provide a price range for search.");
                    CustomPrint.print("Minimum Price: ");
                    int minPrice = new Scanner(System.in).nextInt();
                    CustomPrint.print("Maximum Price: ");
                    int maxPrice = new Scanner(System.in).nextInt();

                    if ((minPrice < 0 || maxPrice <= 0) || (maxPrice < minPrice)) {
                        CustomPrint.printError("Search By Price", "Please provide a valid range.");
                        break;
                    }

                    getCarByPrice(minPrice, maxPrice);
                }
                case KmsDriven -> {
                    CustomPrint.println("Provide a kms range for search.");
                    CustomPrint.print("Minimum Kms: ");
                    int minKms = new Scanner(System.in).nextInt();
                    CustomPrint.print("Maximum Kms: ");
                    int maxKms = new Scanner(System.in).nextInt();

                    if ((minKms < 0 || maxKms <= 0) || (maxKms < minKms)) {
                        CustomPrint.printError("Search By Kms", "Please provide a valid range.");
                        break;
                    }

                    getCarsByKms(minKms, maxKms);
                }
                case ImageAvailable -> {
                    getCarsByImage();
                }
                case Stats -> {
                    CustomPrint.println("Search Stats");
                    CustomPrint.println("Search By Name");
                    trackNameSearch.displayTopKSearch(3);
//                    CustomPrint.println("Search By Fuel");
//                    trackFuelSearch.displaySearchFrequency();
//                    CustomPrint.println("Search By Transmission");
//                    trackTransmissionSearch.displaySearchFrequency();
                }
                case Exit -> {
                    CustomPrint.println("Exiting program...");
                }
                case null, default -> {
                    CustomPrint.print("Please select a valid choice: ");
                }
            }
        } while (choice != Utils.SearchType.Exit);
    }

    private static void getCarsByKms(int minKms, int maxKms) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            int carKmsDriven = car.getKmsDriven();
            if (carKmsDriven <= 0 || !(carKmsDriven >= minKms && carKmsDriven <= maxKms)) {
                continue;
            } else {
                listRequiredCars.add(car);
            }
        }

        // Sort the cars by their kms driven in ascending order
        listRequiredCars.sort(Comparator.comparingInt(Car::getKmsDriven));

        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
    }

    private static void getCarByPrice(int minPrice, int maxPrice) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            int carPrice = car.getPrice();
            if (carPrice <= 0 || !(carPrice >= minPrice && carPrice <= maxPrice)) {
                continue;
            } else {
                listRequiredCars.add(car);
            }
        }

        // Sort the cars by their price in ascending order
        listRequiredCars.sort(Comparator.comparingInt(Car::getPrice));

        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
    }

    private static void getCarByTransmission(int selectionTransmissionType) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            TransmissionType carTransmissionType = car.getTransmissionType();
            if (carTransmissionType != TransmissionType.values()[selectionTransmissionType - 1]) {
                continue;
            } else {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
    }

    private static void getCarByFuelType(int selectionFuelType) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            FuelType carFuelType = car.getFuelType();
            if (carFuelType != FuelType.values()[selectionFuelType - 1]) {
                continue;
            } else {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
    }

    private static void getCarByName(String searchName) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            String name = car.getName().toLowerCase();
            List<String> breakdownName = Utils.generateBagOfWords(name);

            if (!breakdownName.isEmpty() && breakdownName.contains(searchName)) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }

        if (listRequiredCars.isEmpty()) {
            List<String> listCompletedWords = WordCompletion.completionSuggestions(searchName);
            if (!listCompletedWords.isEmpty()) {
                CustomPrint.println("Do you want to search again? [y/n]");
                String c = new Scanner(System.in).next();
                if (c.equalsIgnoreCase("y")) {
                    CustomPrint.print("Enter a name from the provided suggestions: ");
                    String newName = new Scanner(System.in).next();
                    if (listCompletedWords.contains(newName)) {
                        getCarByName(newName);
                    } else {
                        CustomPrint.printError("Invalid Selection.");
                    }
                } else if (c.equalsIgnoreCase("n")) {
                    return;
                } else {
                    CustomPrint.printError("Suggested Name", "Invalid Choice!");
                }
            } else {
                CustomPrint.printError("Search Car By Name", "No car found...");
            }
        }
    }

    private static void getCarsByImage() {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            String imageUrl = car.getImageUrl();
            if (imageUrl == null || imageUrl.isBlank()) {
                continue;
            } else {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
    }

    private static void basicIntegration() {
        Utils.Task choice;
        do {
            choice = askForChoiceBasic();
            switch (choice) {
                case Utils.Task.CrawlWebsite: {
                    CustomPrint.print("URL to be crawled: ");
                    for (String u : urls) {
                        CustomPrint.print(STR."\{u}, ");
                    }
                    WebCrawler.CrawlWebsites(urls);
                    break;
                }

                case Utils.Task.DeleteCacheAndReCrawl: {
                    deleteAllFilesInFolder(".\\res\\generated\\pages\\");
                    WebCrawler.CrawlWebsites(urls);
                    break;
                }

                case Utils.Task.RankPage: {
                    CustomPrint.println("Page Ranking - Search word: ");
                    String word = new Scanner(System.in).next();
                    InvertedIndexing.searchInFile(word);
                    break;
                }

                case Utils.Task.WordSuggestion: {
                    CustomPrint.println("Words Suggestion - Search word: ");
                    String word = new Scanner(System.in).next();
                    SpellChecking.checkSpelling(word);
                    break;
                }

                case Utils.Task.AutoComplete: {
                    CustomPrint.println("Auto Complete - Enter word: ");
                    String word = new Scanner(System.in).next();
                    WordCompletion.completionSuggestions(word);
                    break;
                }

                case Utils.Task.Exit: {
                    CustomPrint.println("Exiting program.");
                    break;
                }

                default: {
                    CustomPrint.println("Please select a valid choice.");
                    break;
                }
            }
        } while (choice != Utils.Task.Exit);
    }

    private static Utils.Task askForChoiceBasic() {
        CustomPrint.println("\n---------------------------------------------");
        CustomPrint.println("Select a search engine feature listed below.");
        CustomPrint.println("---------------------------------------------\n");
        CustomPrint.println("1. Crawl Websites");
        CustomPrint.println("2. Delete cache and Re crawl");
        CustomPrint.println("3. Rank the web pages according to the occurrence of a word");
        CustomPrint.println("4. Words Suggestion");
        CustomPrint.println("5. AutoComplete");
        CustomPrint.println("6. Exit from program\n");

        CustomPrint.println("Please enter your choice");

        int choice = new Scanner(System.in).nextInt();

        return switch (choice) {
            case 1 -> Utils.Task.CrawlWebsite;
            case 2 -> Utils.Task.DeleteCacheAndReCrawl;
            case 3 -> Utils.Task.RankPage;
            case 4 -> Utils.Task.WordSuggestion;
            case 5 -> Utils.Task.AutoComplete;
            default -> Utils.Task.Exit;
        };
    }

    private static Utils.SearchType askForChoiceAdvance() {
        CustomPrint.println("\n-----------------------");
        CustomPrint.println("Select a search method.");
        CustomPrint.println("-----------------------\n");
        CustomPrint.println("1. Search car by name");
        CustomPrint.println("2. Search by price");
        CustomPrint.println("3. Search by transmission");
        CustomPrint.println("4. Search by fuel");
        CustomPrint.println("5. Search by image available");
        CustomPrint.println("6. Show search stats");
        CustomPrint.println("7. Exit from program\n");

        CustomPrint.println("Please enter your choice");

        int choice = new Scanner(System.in).nextInt();

        return switch (choice) {
            case 1 -> Utils.SearchType.Name;
            case 2 -> Utils.SearchType.Price;
            case 3 -> Utils.SearchType.Transmission;
            case 4 -> Utils.SearchType.Fuel;
            case 5 -> Utils.SearchType.ImageAvailable;
            case 6 -> Utils.SearchType.Stats;
            default -> Utils.SearchType.Exit;
        };
    }

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
}
