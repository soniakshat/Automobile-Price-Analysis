package com.group4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        List<String> imageUrls = new ArrayList<>();

        // Read HTML content from files and extract alt attribute values
        try {
            String[] fileNames = {".\\res\\generated\\pages\\Cache_carpages.txt", ".\\res\\generated\\pages\\Cache_motorcitychrysler.txt"}; //Update the path to where the txt files are stored by WebCrawler.java
            for (String fileName : fileNames) {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                Document doc = Jsoup.parse(content);
                Elements imgTags = doc.select("img[alt]");
                for (Element imgTag : imgTags) {
                    String altText = imgTag.attr("alt");
                    imageUrls.add(altText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Perform inverted indexing and page ranking on extracted URLs
        performIndexingAndRanking(imageUrls);
    }

    private static void performIndexingAndRanking(List<String> imageUrls) {
        // Perform inverted indexing
        InvertedIndexing invertedIndex = new InvertedIndexing();
        for (String url : imageUrls) {
            invertedIndex.addDocument(url, extractTerms(url));
        }

        // Perform page ranking
        List<CarPage> pages = new ArrayList<>();
        for (String url : imageUrls) {
            pages.add(new CarPage(url, 1, extractTerms(url)));
        }
        PageRanking.rankPages(pages);

        // User input for searching a car model
        try (Scanner scanner = new Scanner(System.in)) {
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

    private static List<String> extractTerms(String url) {
        // Split the URL string into words and return
        return Arrays.asList(url.split("\\s+"));
    }

    public void addDocument(String document, List<String> terms) {
        for (String term : terms) {
            invertedIndexing.computeIfAbsent(term.toLowerCase(), __ -> new HashSet<>()).add(document);
        }
    }

    public Set<String> search(String term) {
        return invertedIndexing.getOrDefault(term.toLowerCase(), Collections.emptySet());
    }

    public static void main(String[] args) {
        searchInFile();
    }
}

class PageRanking {
    public static List<CarPage> rankPages(List<CarPage> pages) {
        pages.sort(Comparator.comparingDouble(CarPage::calculateRank).reversed());
        return pages;
    }
}

class CarPage {
    private final String url;
    private final int frequencyCount;
    private final List<String> searchKeywords;

    public CarPage(String url, int frequencyCount, List<String> searchKeywords) {
        this.url = url;
        this.frequencyCount = frequencyCount;
        this.searchKeywords = searchKeywords;
    }

    public double calculateRank() {
        return frequencyCount * 0.6 + searchKeywords.size() * 0.4;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getSearchKeywords() {
        return searchKeywords;
    }
}