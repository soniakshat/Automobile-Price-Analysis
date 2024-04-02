package com.group4;

import static java.lang.StringTemplate.STR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
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

//        CustomPrint.println(listCars);

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
                        Scanner scanner = new Scanner(System.in);
                        String selection = "";
                        try {
                            CustomPrint.print("Do you want to search for suggested name? [y/n]:  ");
                            selection = scanner.next();
                        } catch (InputMismatchException e) {
                            CustomPrint.printError( "Invalid input. Please enter a correct choice:");
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

                    FuelType fuel = FuelType.values()[selectionFuelType - 1];
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
                    TransmissionType transmission = TransmissionType.values()[selectionTransmissionType - 1];
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
                        CustomPrint.printError( "Invalid input. Please enter a correct choice:");
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
                        CustomPrint.printError( "Invalid input. Please enter a correct choice:");
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
                    listCars.addAll(HtmlParsing.crawlListCars());
                    bagOfWords.addAll(Utils.generateBagOfWords(listCars));
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
        performJSONProcessingAndRanking("kms", minKms, String.valueOf(maxKms));
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

    private static void performJSONProcessingAndRanking(String searchMethod, Integer intValueParam, String strValueParam) {
        List<Map<String, Object>> allRankedPages = new ArrayList<>();
        File folder = new File("./");
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    PageRanking pageRanking = new PageRanking();
                    pageRanking.readPages(file.getAbsolutePath());

                    switch (searchMethod) {
                        case "name" -> {
                            pageRanking.rankByName(strValueParam);
                            pageRanking.buildInvertedIndex();
                            List<Map<String, Object>> filteredPages = pageRanking.searchByKeyword(strValueParam);
                            allRankedPages.addAll(filteredPages);
                        }
                        case "price" -> {
                            int minPrice = intValueParam != null ? intValueParam : 0;
                            int maxPrice = strValueParam != null ? Integer.parseInt(strValueParam) : Integer.MAX_VALUE;
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesInPriceRange(minPrice, maxPrice);

                            // Sort filtered pages by price in increasing order
                            filteredPages.sort(Comparator.comparingInt(p -> Integer.parseInt(p.get("price").toString())));

                            allRankedPages.addAll(filteredPages);
                        }
                        case "kms" -> {
                            int minKms = intValueParam != null ? intValueParam : 0;
                            int maxKms = strValueParam != null ? Integer.parseInt(strValueParam) : Integer.MAX_VALUE;
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesInKmsRange(minKms, maxKms);
                            allRankedPages.addAll(filteredPages);
                        }
                        case "transmission" -> {
                            int selectionTransmissionType = intValueParam != null ? intValueParam : 0;
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesByTransmission(selectionTransmissionType);
                            allRankedPages.addAll(filteredPages);
                        }
                        case "fuelType" -> {
                            int selectionFuelType = intValueParam != null ? intValueParam : 0;
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesByFuelType(selectionFuelType);
                            allRankedPages.addAll(filteredPages);
                        }
                        case "image" -> {
                            List<Map<String, Object>> filteredPages = pageRanking.getPagesWithImage();
                            allRankedPages.addAll(filteredPages);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing file: " + file.getName());
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
            System.out.println("No JSON files found in the directory.");
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
