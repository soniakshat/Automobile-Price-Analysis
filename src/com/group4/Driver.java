package com.group4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * Main class that will run everything
 * @implNote the file ./res/doc/dictionary.txt contains the list of words from Oxford dictionary
 * @author Akshat Soni
 * */
public class Driver {
    private static String[] urls = {"https://www.motorcitychrysler.ca/used/",
            "https://www.carpages.ca/ontario/windsor/used-cars/",
            "https://www.autotrader.ca/cars/on/windsor/"};

    public static void main(String[] args) {
        Utils.Task choice;
        do{
            choice = askForChoice();
            switch (choice) {
                case Utils.Task.CrawlWebsite:
                    CustomPrint.println("URL to be crawled: " + urls);
                    WebCrawler.CrawlWebsites(urls);
                    break;

                case Utils.Task.DeleteCacheAndReCrawl:
                    deleteAllFilesInFolder(".\\res\\generated\\pages\\");
                    WebCrawler.CrawlWebsites(urls);
                    break;

                case Utils.Task.RankPage:
                    System.out.println("Page Ranking - Search word: ");
//                RankPages.searchInFile();
                    break;

                case Utils.Task.WordSuggestion:
//                System.out.println("Words Suggestion - Search word: ");
//                word = scanner.nextLine();
//                corrector = new AutoComplete();
//
//                corrector.loadSpellCorrector();
//                String identicalWord = corrector.findIdenticalWord(word);
//                if (identicalWord.length() == 0)
//                    System.out.println("There are no similar words. Please enter the valid word to search");
//                else
//                    System.out.println("Identical suggestion found: " + identicalWord);

                    break;

                case Utils.Task.AutoComplete:
//                System.out.println("Auto Complete - Enter word: ");
//                word = scanner.nextLine();
//                corrector = new AutoComplete();
//
//                corrector.loadSpellCorrector();
//                ArrayList<String> autoCompleteStr = corrector.autoComplete(word);
//                System.out.println(autoCompleteStr.toString());
                    break;

                case Utils.Task.Exit:
                    System.out.println("Exiting program.");
                    break;

                default:
                    System.out.println("Please select a valid choice.");
                    break;
            }
        }while(choice != Utils.Task.Exit);
    }

    private static boolean isUrlValid(String url) {
        return Pattern.matches(Utils.REGEX_URL, url);
    }

    private static Utils.Task askForChoice(){
        System.out.println("Select a search engine feature listed below.");
        System.out.println("---------------------------------------------\n");
        System.out.println("1. Crawl Websites");
        System.out.println("2. Delete cache and Re crawl");
        System.out.println("3. Rank the web pages according to the occurrence of a word");
        System.out.println("4. Words Suggestion");
        System.out.println("5. AutoComplete");
        System.out.println("6. Exit from program\n");

        System.out.println("Please enter your choice");

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
            CustomPrint.printError("Invalid folder path: " + folderPath);
            return;
        }

        try {
            Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            CustomPrint.printError(("Error deleting file " + path.toString() + ": " + e.getMessage()));
                        }
                    });
            CustomPrint.println("All files in the folder " + folderPath + " have been deleted.");
        } catch (IOException e) {
            CustomPrint.printError(("Error deleting files in the folder " + folderPath + ": " + e.getMessage()));
        }
    }
}
