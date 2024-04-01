package com.group4;

import java.util.*;

/**
 * Class to find search frequency
 *
 * @author Aniket Patel
 */
public class SearchFrequency {
    private final TreeMap<String, Integer> searchFrequency;

    static FrequencyCount fq = new FrequencyCount();

    public SearchFrequency() {
        searchFrequency = new TreeMap<>();
    }

    // Method to track search frequency
    public void trackSearch(String word) {
        word = word.toLowerCase(); // Convert word to lowercase for case-insensitive search
        fq.addWordsToMap(word);
        searchFrequency.put(word, searchFrequency.getOrDefault(word, 0) + 1);
    }

    // Method to display search frequency for all words
    public void displaySearchFrequency() {
        System.out.println("Search Frequency:");
        for (Map.Entry<String, Integer> entry : searchFrequency.entrySet()) {
            System.out.println(STR."\{entry.getKey()}: \{entry.getValue()}");
        }
    }

    public void displayTopKSearch(int k){
        fq.topKWords(k);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SearchFrequency tracker = new SearchFrequency();

        while (true) {
            System.out.print("Enter a word to search (type 'X' or 'x' to exit): ");
            String word = scanner.nextLine();

            if (word.equalsIgnoreCase("x")) {
                break;
            }

            tracker.trackSearch(word);
            tracker.displaySearchFrequency();
        }

        scanner.close();
    }

}