package com.group4;

import static java.lang.StringTemplate.STR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Main class that will run everything
 * @author Akshat Soni
 */
public class Driver {
    private static final String[] urls = {"https://www.motorcitychrysler.ca/used/",
            "https://www.carpages.ca/ontario/windsor/used-cars/",
            "https://www.autotrader.ca/cars/on/windsor/"};
    private static final Set<String> bagOfWords = new HashSet<>();

    public static void main(String[] args) {
        basicIntegration();
//        advanceIntegration();
    }

    private static void advanceIntegration() {
        String exit;
        do{
            CustomPrint.println("=".repeat(30));
            CustomPrint.println("Instructions", """
                    This program will give you information about a car by crawling below mentioned websites.""");
            for(String url: urls){
                CustomPrint.println("Source", url);
            }
            CustomPrint.println("=".repeat(30));

            Set<String> bagOfWords;
            WebCrawler.CrawlWebsites(urls);

            CustomPrint.print("To exit program enter q");
            exit = new Scanner(System.in).next();
        }while(!exit.equals("q"));
    }

    private static void basicIntegration() {
        Utils.Task choice;
        do {
            choice = askForChoice();
            switch (choice) {
                case Utils.Task.CrawlWebsite: {
                    CustomPrint.print(STR."URL to be crawled: ");
                    for(String u : urls){
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

    private static Utils.Task askForChoice() {
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
