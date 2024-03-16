// This class is used to test the Page Ranking algorithm and Inverted Index algorithm for a sample Automobile Page

package com.group4;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ImplementInvertedIndexing_pageRanking {
    public static void main(String[] args) {
        // Extended sample pages with associated data
        List<CarPage> pages = List.of(
                new CarPage("https://example.com/page1", 10, List.of("Mazda", "CX-5", "price")),
                new CarPage("https://example.com/page2", 15, List.of("Ford", "Mustang", "deal")),
                new CarPage("https://example.com/page3", 8, List.of("Toyota", "Camry", "buy")),
                new CarPage("https://example.com/page4", 20, List.of("Chevrolet", "Equinox", "best", "deal")),
                new CarPage("https://example.com/page5", 12, List.of("Mazda", "MX-5", "price")),
                new CarPage("https://example.com/page6", 18, List.of("Ford", "Explorer", "deal")),
                new CarPage("https://example.com/page7", 14, List.of("Toyota", "Corolla", "buy")),
                new CarPage("https://example.com/page8", 22, List.of("Chevrolet", "Malibu", "best", "deal"))
        );

        // Testing Page Ranking
        PageRanking.rankPages(pages);

        // Testing Inverted Indexing
        InvertedIndexing invertedIndex = new InvertedIndexing();

        // Adding documents to the inverted index
        for (CarPage page : pages) {
            invertedIndex.addDocument(page.getUrl(), page.getSearchKeywords());
        }

        try (// User input for searching a car model
        Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a car model to search: ");
            String searchModel = scanner.nextLine();

            // Searching for the car model in the inverted index
            Set<String> searchResults = invertedIndex.search(searchModel.toLowerCase());

            // Printing search results
            if (!searchResults.isEmpty()) {
                System.out.println("\nMatching Car Models:");
                for (String result : searchResults) {
                    System.out.println(result);
                }
            } else {
                System.out.println("\nNo matching car models found.");
            }
        }
        // Printing ranked pages
        System.out.println("\nRanked Pages:");
        for (CarPage page : pages) {
            System.out.println("URL: " + page.getUrl() + ", Rank: " + page.calculateRank());
        }
    }
}