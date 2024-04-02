package com.group4;

import java.io.File;
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
    public static Set<String> bagOfWords = new HashSet<>();
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

        String instruction = "This program will give you information about a car by crawling below mentioned websites.";
        CustomPrint.println("=".repeat(instruction.length()+18));
        CustomPrint.println("Instructions", STR."""
                \{instruction}
                                                        _______
                                                       //  ||\\ \\
                                                 _____//___||_\\ \\___
                                                 )  _          _    \\
                                                 |_/ \\________/ \\___|
                                                ___\\_/________\\_/______
                """);

        CustomPrint.println("=".repeat(instruction.length()+18));

        listCars.addAll(HtmlParsing.crawlListCars());

        bagOfWords.addAll(Utils.generateBagOfWords(listCars));

        WordCompletion.insertWordsForCompletion(bagOfWords);

        Utils.SearchType choice;
        do {
            choice = askForChoiceAdvance();

            switch (choice) {
                case Name -> {

                    Scanner carScanner = new Scanner(System.in);
                    String carName = "";
                    try {
                        CustomPrint.print("Enter a car name to search: ");
                        carName = carScanner.next();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        carScanner.next();
                        carName = carScanner.next(); // Discard the invalid input and wait for a new input
                    }
                    String suggestedCarName = SpellChecking.checkSpelling(carName);

                    if (!suggestedCarName.equalsIgnoreCase(carName)) {
                        Scanner scanner = new Scanner(System.in);
                        String selection = "";
                        try {
                            CustomPrint.print("Do you want to search for suggested name? [y/n]:  ");
                            selection = scanner.next();
                        } catch (InputMismatchException e) {
                            CustomPrint.printError("Invalid input. Please enter a correct choice:");
                            scanner.next();
                            selection = scanner.next(); // Discard the invalid input and wait for a new input
                        }

                        if (selection.equalsIgnoreCase("y")) {
                            getCarByName(suggestedCarName);
                            trackNameSearch.trackSearch(suggestedCarName);
                            break;
                        } else if (selection.equalsIgnoreCase("n")) {
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

                    Scanner scanner = new Scanner(System.in);
                    int selectionFuelType = -1;
                    try {
                        CustomPrint.print("Please enter your choice: ");
                        selectionFuelType = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        scanner.next();
                        selectionFuelType = scanner.nextInt(); // Discard the invalid input and wait for a new input
                    }

                    FuelType fuel;
                    if (selectionFuelType < 1 || selectionFuelType > FuelType.values().length) {
                        CustomPrint.printError("Invalid Fuel Data");
                      break;
                    } else {
                        fuel = FuelType.values()[selectionFuelType - 1];
                    }

                    getCarByFuelType(selectionFuelType);
                    trackFuelSearch.trackSearch(fuel.name());
                }
                case Transmission -> {
                    CustomPrint.println("Select a transmission type from below: ");
                    for (int i = 0; i < TransmissionType.values().length; i++) {
                        CustomPrint.println(STR."\{i + 1}. \{TransmissionType.values()[i]}");
                    }

                    Scanner scanner = new Scanner(System.in);
                    int selectionTransmissionType = -1;
                    try {
                        CustomPrint.print("Please enter your choice: ");
                        selectionTransmissionType = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        scanner.next();
                        selectionTransmissionType = scanner.nextInt(); // Discard the invalid input and wait for a new input
                    }
                    TransmissionType transmission;

                    if (selectionTransmissionType < 1 || selectionTransmissionType > TransmissionType.values().length) {
                        CustomPrint.printError("Invalid Transmission Data");
                        break;
                    } else {
                     transmission=   TransmissionType.values()[selectionTransmissionType - 1];
                    }
                    getCarByTransmission(selectionTransmissionType);
                    trackTransmissionSearch.trackSearch(transmission.name());
                }
                case Price -> {
                    CustomPrint.println("Provide a price range for search.");

                    Scanner scanner = new Scanner(System.in);
                    int minPrice = -1;
                    try {
                        CustomPrint.print("Minimum Price: ");
                        minPrice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        scanner.next();
                        minPrice = scanner.nextInt(); // Discard the invalid input and wait for a new input
                    }


                    Scanner scanner1 = new Scanner(System.in);
                    int maxPrice = -1;
                    try {
                        CustomPrint.print("Maximum Price: ");
                        maxPrice = scanner1.nextInt();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        scanner1.next();
                        maxPrice = scanner1.nextInt(); // Discard the invalid input and wait for a new input
                    }

                    if ((minPrice < 0 || maxPrice <= 0) || (maxPrice < minPrice)) {
                        CustomPrint.printError("Search By Price", "Please provide a valid range.");
                        break;
                    }

                    getCarByPrice(minPrice, maxPrice);
                }
                case KmsDriven -> {
                    CustomPrint.println("Provide a kms range for search.");
                    Scanner scanner = new Scanner(System.in);
                    int minKms = -1;
                    try {
                        CustomPrint.print("Minimum Kms: ");
                        minKms = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        scanner.next();
                        minKms = scanner.nextInt(); // Discard the invalid input and wait for a new input
                    }


                    Scanner scanner1 = new Scanner(System.in);
                    int maxKms = -1;
                    try {
                        CustomPrint.print("Maximum Kms: ");
                        maxKms = scanner1.nextInt();
                    } catch (InputMismatchException e) {
                        CustomPrint.printError("Invalid input. Please enter a correct choice:");
                        scanner1.next();
                        maxKms = scanner1.nextInt(); // Discard the invalid input and wait for a new input
                    }

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
                    CustomPrint.println("Search Stats are as follows:\n");
                    CustomPrint.println("\nSearch By Name");
                    trackNameSearch.displaySearchFrequency();

                    Utils.delay(1500);

                    CustomPrint.println("\nSearch By Fuel");
                    trackFuelSearch.displaySearchFrequency();

                    Utils.delay(1500);

                    CustomPrint.println("\nSearch By Transmission");
                    trackTransmissionSearch.displaySearchFrequency();
//
                }
//                case DeleteCacheAndRefreshData -> {
//                    CustomPrint.println("Deleting Cache...");
//                    Utils.deleteAllFilesInFolder(Utils.jsonCacheFolder);
//                    Utils.deleteAllFilesInFolder(Utils.htmlCacheFolder);
//                    listCars.clear();
//                    bagOfWords.clear();
//                    CustomPrint.println("Regathering data...");
//                    listCars.addAll(HtmlParsing.crawlListCars());
//                    bagOfWords.addAll(Utils.generateBagOfWords(listCars));
//                    CustomPrint.println("Data refreshed!");
//                }
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
        jsonProcessing_pageRanking_and_invertedIndexing("kms", minKms, String.valueOf(maxKms));
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
        jsonProcessing_pageRanking_and_invertedIndexing("price", minPrice, String.valueOf(maxPrice));
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
            if (selectionTransmissionType < 1 || selectionTransmissionType > TransmissionType.values().length) {
                CustomPrint.printError("Invalid Transmission Data");
                return;
            } else {
                TransmissionType transmissionType = TransmissionType.values()[selectionTransmissionType - 1];
                if (carTransmissionType == transmissionType) {
                    listRequiredCars.add(car);
                }
            }

        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        jsonProcessing_pageRanking_and_invertedIndexing("transmission", selectionTransmissionType, null);
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
            if (selectionFuelType < 1 || selectionFuelType > TransmissionType.values().length) {
                CustomPrint.printError("Invalid Fuel Data");
                return;
            } else {
                FuelType transmissionType = FuelType.values()[selectionFuelType - 1];
                if (carFuelType == transmissionType) {
                    listRequiredCars.add(car);
                }
            }
            if (carFuelType == FuelType.values()[selectionFuelType - 1]) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        jsonProcessing_pageRanking_and_invertedIndexing("fuelType", selectionFuelType, null);
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

                Scanner scanner = new Scanner(System.in);
                String selection = "";
                try {
                    CustomPrint.print("Do you want to search again? [y/n]");
                    selection = scanner.next();
                } catch (InputMismatchException e) {
                    CustomPrint.printError("Search Selection", "Invalid input. Please enter a correct choice:");
                    scanner.next();
                    selection = scanner.next(); // Discard the invalid input and wait for a new input
                }

                if (selection.equalsIgnoreCase("y")) {
                    CustomPrint.print("Enter a name from the provided suggestions: ");
                    String newName = new Scanner(System.in).next();
                    if (listCompletedWords.contains(newName)) {
                        getCarByName(newName);
                    } else {
                        CustomPrint.printError("Invalid Selection.");
                    }
                } else if (selection.equalsIgnoreCase("n")) {
                    return;
                } else {
                    CustomPrint.printError("Suggested Name", "Invalid Choice!");
                }
            } else {
                CustomPrint.printError("Search Car By Name", "No car found...");
            }
        }
        jsonProcessing_pageRanking_and_invertedIndexing("name", 0, searchName);
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
            if (!Utils.isUrlValid(imageUrl)) {
                continue;
            }
            if (imageUrl != null && !imageUrl.isBlank()) {
                listRequiredCars.add(car);
            }
        }
        for (Car car : listRequiredCars) {
            CustomPrint.println(car);
        }
        jsonProcessing_pageRanking_and_invertedIndexing("image", 0, null);
    }

    /**
     * This method performs JSON processing and ranking of pages, and inverted indexing based on the specified search method and parameters.
     * It reads JSON files from the current directory, ranks and filters pages according to the specified criteria,
     * and prints the filtered ranked pages.
     *
     * @param searchMethod  The method to be used for ranking and filtering (e.g., "name", "price", "kms", etc.).
     * @param intValueParam Integer parameter for filtering (e.g., minimum price, minimum kms, transmission type, fuel type).
     * @param strValueParam String parameter for filtering (e.g., name to search, maximum price).
     */
    private static void jsonProcessing_pageRanking_and_invertedIndexing(String searchMethod, Integer intValueParam, String strValueParam) {
        List<Map<String, Object>> allRankedPages = new ArrayList<>(); // List to store all filtered ranked pages
        File folder = new File(Utils.jsonCacheFolder); // Current directory
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json")); // List of JSON files in the directory

        if (files != null) { // Check if there are JSON files in the directory
            for (File file : files) { // Iterate through each JSON file
                try {
                    PageRanking_and_invertedIndexing pageRanking = new PageRanking_and_invertedIndexing(); // Create a PageRanking_and_invertedIndexing object
                    pageRanking.readPages(file.getAbsolutePath()); // Read pages from the JSON file

                    switch (searchMethod) {
                        case "name" -> {
                            pageRanking.rankByName(strValueParam); // Rank pages by name containing the specified search name
                            pageRanking.buildInvertedIndex(); // Build inverted index for efficient keyword search
                            List<Map<String, Object>> filteredPages = pageRanking.searchByKeyword(strValueParam); // Search pages by keyword
                            allRankedPages.addAll(filteredPages); // Add filtered pages to the list
                        }
                        case "price" -> {
                            int minPrice = intValueParam != null ? intValueParam : 0; // Minimum price parameter
                            int maxPrice = strValueParam != null ? Integer.parseInt(strValueParam) : Integer.MAX_VALUE; // Maximum price parameter
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesInPriceRange(minPrice, maxPrice); // Filter pages by price range

                            // Sort filtered pages by price in increasing order
                            filteredPages.sort(Comparator.comparingInt(p -> Integer.parseInt(p.get("price").toString())));

                            allRankedPages.addAll(filteredPages); // Add filtered pages to the list
                        }
                        case "kms" -> {
                            int minKms = intValueParam != null ? intValueParam : 0; // Minimum kms parameter
                            int maxKms = strValueParam != null ? Integer.parseInt(strValueParam) : Integer.MAX_VALUE; // Maximum kms parameter
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesInKmsRange(minKms, maxKms); // Filter pages by kms range
                            allRankedPages.addAll(filteredPages); // Add filtered pages to the list
                        }
                        case "transmission" -> {
                            int selectionTransmissionType = intValueParam != null ? intValueParam : 0; // Transmission type parameter
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesByTransmission(String.valueOf(selectionTransmissionType)); // Filter pages by transmission type
                            allRankedPages.addAll(filteredPages); // Add filtered pages to the list
                        }
                        case "fuelType" -> {
                            int selectionFuelType = intValueParam != null ? intValueParam : 0; // Fuel type parameter
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesByFuelType(String.valueOf(selectionFuelType)); // Filter pages by fuel type
                            allRankedPages.addAll(filteredPages); // Add filtered pages to the list
                        }
                        case "image" -> {
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesWithImage(); // Filter pages with image availability
                            allRankedPages.addAll(filteredPages); // Add filtered pages to the list
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing file: " + file.getName()); // Print error message if an exception occurs during processing
                    e.printStackTrace();
                }
            }

            // Print all filtered pages together
            System.out.println("\nFiltered Ranked Pages:");
            for (int i = 0; i < allRankedPages.size(); i++) {
                Map<String, Object> page = allRankedPages.get(i);
                System.out.println("Rank: " + (i + 1) + ", Name: " + page.get("name") + ", Price: " + page.get("price"));
            }
        } else {
            System.out.println("No JSON files found in the directory."); // Print message if no JSON files are found in the directory
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
//        CustomPrint.println("7. Delete Cache and Refresh Data");
        CustomPrint.println("7. Exit from program\n");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        try {
            CustomPrint.print("Please enter your choice: ");
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            CustomPrint.printError("Search Selection", "Invalid input. Please enter a correct choice:");
            scanner.next();
            choice = scanner.nextInt(); // Discard the invalid input and wait for a new input
        }

        return switch (choice) {
            case 1 -> Utils.SearchType.Name;
            case 2 -> Utils.SearchType.Price;
            case 3 -> Utils.SearchType.Transmission;
            case 4 -> Utils.SearchType.Fuel;
            case 5 -> Utils.SearchType.ImageAvailable;
            case 6 -> Utils.SearchType.Stats;
//            case 7 -> Utils.SearchType.DeleteCacheAndRefreshData;
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
