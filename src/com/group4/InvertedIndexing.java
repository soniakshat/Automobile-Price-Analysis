package com.group4;

import java.util.*;

/**
 * Class for inverted indexing
 * @author Harsh Verma
 */
public class InvertedIndexing {
    private final Map<String, Set<String>> invertedIndexing;

    public InvertedIndexing() {
        this.invertedIndexing = new HashMap<>();
    }

    public static void searchInFile() {
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

        // User input for searching a car model
        try (Scanner scanner = new Scanner(System.in)) {
            CustomPrint.print("Enter a car model to search: ");
            String searchModel = scanner.nextLine();

            // Searching for the car model in the inverted index
            Set<String> searchResults = invertedIndex.search(searchModel.toLowerCase());

            // Printing search results
            if (!searchResults.isEmpty()) {
                CustomPrint.println("\nMatching Car Models:");
                for (String result : searchResults) {
                    CustomPrint.println(result);
                }
            } else {
                CustomPrint.println("\nNo matching car models found.");
            }
        }
        // Printing ranked pages
        CustomPrint.println("\nRanked Pages:");
        for (CarPage page : pages) {
            CustomPrint.println(STR."URL: \{page.getUrl()}, Rank: \{page.calculateRank()}");
        }
    }

    public void addDocument(String document, List<String> terms) {
        for (String term : terms) {
            invertedIndexing.computeIfAbsent(term.toLowerCase(), _ -> new HashSet<>()).add(document);
        }
    }

    public Set<String> search(String term) {
        return invertedIndexing.getOrDefault(term.toLowerCase(), new HashSet<>());
    }

    public static void main(String[] args) {
        searchInFile();
    }
}

class PageRanking {
    public static List<CarPage> rankPages(List<CarPage> pages) {
        List<CarPage> sortedPages = new ArrayList<>(pages);
        sortedPages.sort(Comparator.comparingDouble(CarPage::calculateRank).reversed());
        return sortedPages;
    }
}

class CarPage {
    private final String url;
    private final int frequencyCount;
    private final List<String> searchKeywords;

    /**
     * Constructs a CarPage object with the given parameters
     *
     * @param url            the URL of the page
     * @param frequencyCount the frequency count of the page
     * @param searchKeywords the search keywords related to the page
     * @author Harsh Verma
     */

    public CarPage(String url, int frequencyCount, List<String> searchKeywords) {
        this.url = url;
        this.frequencyCount = frequencyCount;
        this.searchKeywords = searchKeywords;
    }

    /**
     * Calculates the rank of the page based on its frequency count and search keywords
     *
     * @return the rank of the page
     * @author Harsh Verma
     */

    public double calculateRank() {
        return frequencyCount * 0.6 + searchKeywords.size() * 0.4;
    }

    // Returns the URL of the page
    public String getUrl() {
        return url;
    }


    // Returns the search keywords related to the page
    public List<String> getSearchKeywords() {
        return searchKeywords;
    }
}
