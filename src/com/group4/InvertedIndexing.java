package com.group4;

import java.util.*;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void searchInFile(String word) {
        List<CarPage> carPages = new ArrayList<>();

        // Read HTML content from files and extract alt attribute values
        try {
            String[] fileNames = {".\\res\\generated\\pages\\Cache_carpages.txt", ".\\res\\generated\\pages\\Cache_motorcitychrysler.txt"}; //Update the path to where the txt files are stored by WebCrawler.java
            for (String fileName : fileNames) {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                Document doc = Jsoup.parse(content);
                Elements imgTags = doc.select("img[alt]");
                for (Element imgTag : imgTags) {
                    String altText = imgTag.attr("alt");
                    String url = imgTag.attr("src");
                    if (!Pattern.compile("Image \\d+ of \\d+").matcher(altText).find()) { // Exclude images of format "Image X of Y"
                        carPages.add(new CarPage(url, altText, 1, extractTerms(altText)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Perform inverted indexing
        InvertedIndexing invertedIndex = new InvertedIndexing();
        for (CarPage page : carPages) {
            invertedIndex.addDocument(page.getModel(), extractTerms(page.getModel()));
        }

        // Perform page ranking
        PageRanking.rankPages(carPages);

        // Filter out models with URLs not starting with "https"
        carPages.removeIf(page -> !page.getUrl().startsWith("https"));

        // Searching for the word in the inverted index
        Set<String> searchResults = invertedIndex.search(word.toLowerCase());

        // Printing search results
        if (!searchResults.isEmpty()) {
            System.out.println("\nMatching Car Models:");
            for (String result : searchResults) {
                System.out.println(result);
            }
        } else {
            System.out.println("\nNo matching car models found.");
        }

        // Sort car pages based on the input word or ranking
        Comparator<CarPage> comparator = Comparator.comparingDouble(CarPage::calculateRank).reversed();
        if (!word.isEmpty()) {
            comparator = Comparator.comparingInt(page -> {
                String model = page.getModel().toLowerCase();
                return model.contains(word.toLowerCase()) ? 0 : 1;
            });
        }
        carPages.sort(comparator);

        // Printing ranked models with corresponding URLs
        System.out.println("\nRanked Models:");
        for (CarPage page : carPages) {
            System.out.println("\nModel: " + page.getModel() + ", \nRank: " + page.calculateRank() + ", \nURL: " + page.getUrl());
        }
    }

    private static List<String> extractTerms(String model) {
        // Split the model string into words and return
        return Arrays.asList(model.split("\\s+"));
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
        searchInFile("honda"); // Provide a default search word here
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
    private final String model;
    private final int frequencyCount;
    private final List<String> searchKeywords;

    public CarPage(String url, String model, int frequencyCount, List<String> searchKeywords) {
        this.url = url;
        this.model = model;
        this.frequencyCount = frequencyCount;
        this.searchKeywords = searchKeywords;
    }

    public double calculateRank() {
        return frequencyCount * 0.6 + searchKeywords.size() * 0.4;
    }

    public String getUrl() {
        return url;
    }

    public String getModel() {
        return model;
    }

    public List<String> getSearchKeywords() {
        return searchKeywords;
    }
}
