package com.group4;

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
    public static final Set<String> bagOfWords = new HashSet<>();
    private static final List<Car> listCars = new ArrayList<>();

    private static final SearchFrequency trackNameSearch = new SearchFrequency();
    private static final SearchFrequency trackFuelSearch = new SearchFrequency();
    private static final SearchFrequency trackTransmissionSearch = new SearchFrequency();

    public static void main(String[] args) {
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
                This program will give you information about a car by crawling below mentioned websites.
                """);


        CustomPrint.println("=".repeat(30));

        listCars.addAll(HtmlParsing.crawlListCars());

        bagOfWords.addAll(Utils.generateBagOfWords(listCars));

        CustomPrint.println(listCars);

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
                    FuelType fuel = FuelType.values()[selectionFuelType - 1];
                    getCarByFuelType(selectionFuelType);
                    trackFuelSearch.trackSearch(fuel.name());
                }
                case Transmission -> {
                    CustomPrint.println("Select a transmission type from below: ");
                    for (int i = 0; i < TransmissionType.values().length; i++) {
                        CustomPrint.println(STR."\{i + 1}. \{TransmissionType.values()[i]}");
                    }
                    CustomPrint.print("Enter your choice: ");
                    int selectionTransmissionType = new Scanner(System.in).nextInt();
                    TransmissionType transmission = TransmissionType.values()[selectionTransmissionType - 1];
                    getCarByTransmission(selectionTransmissionType);
                    trackTransmissionSearch.trackSearch(transmission.name());
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
                case Exit -> {
                    CustomPrint.println("Exiting program...");
                }
                case Stats -> {
                    CustomPrint.println("Search Stats");
//                    if (trackNameSearch.isResultAvailable()) {
//                        CustomPrint.println("Search By Name");
//                        trackNameSearch.displayTopKSearch(3);
//                    }
//                    if (trackFuelSearch.isResultAvailable()) {
//                        CustomPrint.println("Search By Fuel");
//                        trackFuelSearch.displayTopKSearch(3);
//                    }
//                    if (trackTransmissionSearch.isResultAvailable()) {
//                        CustomPrint.println("Search By Transmission");
//                        trackTransmissionSearch.displayTopKSearch(3);
//                    }

                    CustomPrint.println("Search By Name");
                    trackNameSearch.displaySearchFrequency();

                    Utils.delay(500);

                    CustomPrint.println("Search By Fuel");
                    trackFuelSearch.displaySearchFrequency();

                    Utils.delay(500);

                    CustomPrint.println("Search By Transmission");
                    trackTransmissionSearch.displaySearchFrequency();
//
                }
                case DeleteCacheAndRefreshData -> {
                    CustomPrint.println("Deleting Cache...");
                    Utils.deleteAllFilesInFolder(Utils.jsonCacheFolder);
                    Utils.deleteAllFilesInFolder(Utils.htmlCacheFolder);
                    listCars.clear();
                    bagOfWords.clear();
                    CustomPrint.println("Regathering data...");
//                    listCars.addAll(SeleniumDriver.crawlListCars());
//                    bagOfWords.addAll(Utils.generateBagOfWords(listCars));
                }
                case null, default -> {
                    CustomPrint.print("Please select a valid choice: ");
                }
            }
        } while (choice != Utils.SearchType.Exit);
    }

    /**
     * get cars from the provided range of kms
     *
     * @param minKms min kms
     * @param maxKms max kms
     * @author Akshat Soni
     */
    private static void getCarsByKms(int minKms, int maxKms) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            int carKmsDriven = car.getKmsDriven();
            if (carKmsDriven > 0 && (carKmsDriven >= minKms && carKmsDriven <= maxKms)) {
                listRequiredCars.add(car);
            }
        }

        // Sort the cars by their kms driven in ascending order
        listRequiredCars.sort(Comparator.comparingInt(Car::getKmsDriven));

        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        performJSONProcessingAndRanking("kms", 0, null);
    }

    /**
     * get cars from the provided range of price
     *
     * @param minPrice min price
     * @param maxPrice max price
     * @author Akshat Soni
     */
    private static void getCarByPrice(int minPrice, int maxPrice) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            int carPrice = car.getPrice();
            if (carPrice > 0 && (carPrice >= minPrice && carPrice <= maxPrice)) {
                listRequiredCars.add(car);
            }
        }

        // Sort the cars by their price in ascending order
        listRequiredCars.sort(Comparator.comparingInt(Car::getPrice));

        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        performJSONProcessingAndRanking("price", minPrice, String.valueOf(maxPrice));
    }

    /**
     * get cars from the provided transmission type
     *
     * @param selectionTransmissionType transmission type
     * @author Akshat Soni
     */
    private static void getCarByTransmission(int selectionTransmissionType) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            TransmissionType carTransmissionType = car.getTransmissionType();
            if (carTransmissionType == TransmissionType.values()[selectionTransmissionType - 1]) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        performJSONProcessingAndRanking("transmission", selectionTransmissionType, null);
    }

    /**
     * get cars from the provided fuel type
     *
     * @param selectionFuelType fuel type
     * @author Akshat Soni
     */
    private static void getCarByFuelType(int selectionFuelType) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            FuelType carFuelType = car.getFuelType();
            if (carFuelType == FuelType.values()[selectionFuelType - 1]) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        performJSONProcessingAndRanking("fuelType", selectionFuelType, null);
    }

    /**
     * get cars by their name
     *
     * @param searchName name of car
     * @author Akshat Soni
     */
    private static void getCarByName(String searchName) {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            String name = car.getName().toLowerCase();
            List<String> breakdownName = Utils.generateBagOfWords(name);

            if (breakdownName != null && !breakdownName.isEmpty() && breakdownName.contains(searchName)) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }

        if (listRequiredCars.isEmpty()) {
            List<String> listCompletedWords = WordCompletion.completionSuggestions(searchName);
            if (!listCompletedWords.isEmpty()) {
                CustomPrint.print("AutoComplete", "It seems that the input word is incomplete.\nDid you mean anyone of the following: ");
                CustomPrint.println(listCompletedWords);
                CustomPrint.print("Do you want to search again? [y/n]");
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
        performJSONProcessingAndRanking("name", 0, searchName);
    }

    /**
     * get cars which has their image available
     *
     * @author Akshat Soni
     */
    private static void getCarsByImage() {
        List<Car> listRequiredCars = new ArrayList<>();
        for (Car car : listCars) {
            String imageUrl = car.getImageUrl();
            if (imageUrl != null && !imageUrl.isBlank()) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        performJSONProcessingAndRanking("image", 0, null);
    }

    private static void performJSONProcessingAndRanking(String searchMethod, int intValueParam, String strValueParam) {
//        InvertedIndexing invertedIndexing = new InvertedIndexing();
//        invertedIndexing.buildIndex(".\\res\\generated\\json\\kijijiautos.json");

        PageRanking pageRanking = new PageRanking();
        pageRanking.readPages(".\\res\\generated\\json\\kijijiautos.json");

        // Example usage of inverted index
//        Map<String, Set<Integer>> invertedIndex = invertedIndexing.getInvertedIndex();
//        System.out.println("Inverted Index:");
//        for (Map.Entry<String, Set<Integer>> entry : invertedIndex.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }

        // Example usage of page ranking
        switch (searchMethod) {
            case "name" -> {
                System.out.println("\nRanked Pages by Name:");
                pageRanking.rankByName(strValueParam);
                pageRanking.printRankedPages();
            }
            case "price" -> {
                System.out.println("\nRanked Pages by Price:");
                pageRanking.rankByPrice(intValueParam, Integer.parseInt(strValueParam));
                pageRanking.printRankedPages();
            }
            case "kms" -> {
                System.out.println("\nRanked Pages by Kms Driven:");
                pageRanking.rankByKmsDriven();
                pageRanking.printRankedPages();
            }
            case "transmission" -> {
                System.out.println("\nRanked Pages by Transmission:");
                pageRanking.rankByTransmission(intValueParam);
                pageRanking.printRankedPages();
            }
            case "fuelType" -> {
                System.out.println("\nRanked Pages by Fuel Type:");
                pageRanking.rankByFuelType(intValueParam);
                pageRanking.printRankedPages();
            }
            case "image" -> {
                System.out.println("\nRanked Pages by Image Availability:");
                pageRanking.rankByImageAvailability();
                pageRanking.printRankedPages();
            }
        }
    }

    /**
     * provides the selected search type
     *
     * @author Akshat Soni
     */
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
        CustomPrint.println("7. Delete Cache and Refresh Data");
        CustomPrint.println("8. Exit from program\n");

        CustomPrint.println("Please enter your choice");

        int choice = new Scanner(System.in).nextInt();

        return switch (choice) {
            case 1 -> Utils.SearchType.Name;
            case 2 -> Utils.SearchType.Price;
            case 3 -> Utils.SearchType.Transmission;
            case 4 -> Utils.SearchType.Fuel;
            case 5 -> Utils.SearchType.ImageAvailable;
            case 6 -> Utils.SearchType.Stats;
            case 7 -> Utils.SearchType.DeleteCacheAndRefreshData;
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
