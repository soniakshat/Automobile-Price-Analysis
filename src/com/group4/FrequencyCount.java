package com.group4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * Class to count frequency of words in provided text
 *  @author Akshat Soni
 * */
public class FrequencyCount {
    private final static Map<String, Integer> wordFrequency = new HashMap<>();

    // Priority queue maxHeap to maintain the top words
    private final static PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
            (a, b) -> b.getValue() - a.getValue());

    /**
     * Reads a .txt file and returns a string
     * with all the content of that .txt file
     *
     * @param filePath The path to the .txt file to read.
     * @return The content of the file as a string.
     */
    public static String readTextFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    /**
     * Clean the input text by removing anything except alphanumeric values and white space
     * and make the text case in sensitive by making it to lowercase
     *
     * @param text The input raw text
     * @return The lowercase alphanumeric clean text
     **/
    public static String cleanText(String text){
        if(text == null || text.isEmpty()){
            return  null;
        }
        return text.toLowerCase().replaceAll("[^\\w\\s]", "");
    }

    /**
     * Adds words from the given text to the word frequency map.
     * Removes punctuation, including the full stop, from the input text.
     *
     * @param text The input text to add words from.
     */
    public static void addWordsToMap(String text) {
        // Returns if the input is null or empty
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        // clean text
        text = cleanText(text);

        // Creating a words array to store each word from the clean text
        String[] words = text.split("\\s+");

        // Adding each word to the map and incrementing the value by 1
        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        // Update the max heap with the new data
        updateMaxHeap();
    }

    /**
     * Removes one occurrence of each word from the specified sentence
     * from the word frequency map. If a word is not present, it does nothing.
     *
     * @param text The sentence containing words to be removed.
     */
    public static void removeWordsFromMap(String text) {
        // Returns if the input is null or empty
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        // clean text
        text = cleanText(text);

        // Creating an array to store each word from the cleaned text
        String[] words = text.split("\\s+");

        // Decreasing the value by one from the map for each word
        for (String word : words) {
            if (wordFrequency.containsKey(word)) {
                int currentFrequency = wordFrequency.get(word);
                if (currentFrequency > 1) {
                    // Decreasing the value of frequency by 1
                    wordFrequency.put(word, currentFrequency - 1);
                } else {
                    // Removing the word itself
                    wordFrequency.remove(word);
                }
            }
        }

        // Update the max heap with the new data
        updateMaxHeap();
    }

    /**
     * Updates the max heap with the current data from the word frequency map.
     */
    private static void updateMaxHeap() {
        maxHeap.clear();
        maxHeap.addAll(wordFrequency.entrySet());
    }

    /**
     * Cleans the word frequency map by clearing its contents.
     */
    public static void cleanWordFrequencyMap() {
        // Clearing the wordFrequency HashMap
        wordFrequency.clear();
        // Clearing the max heap as well
        maxHeap.clear();
    }

    /**
     * Prints the top k words and their frequencies from the word frequency map.
     *
     * @param k The number of top words to print.
     */
    public static void topKWords(int k) {
        if (maxHeap == null || maxHeap.isEmpty()) {
            CustomPrint.println("Max Heap is empty. Cannot get top " + k + " words.");
            return;
        }

        // Printing top k words from the max heap priority queue
        CustomPrint.println("Top " + k + " words and their frequencies:");
        for (int i = 0; i < k; i++) {
            if (maxHeap.isEmpty()) {
                CustomPrint.println("Wanted " + k + ", but there are only " + (i) + " unique words in the text file.");
                return;
            }
            Map.Entry<String, Integer> topEntry = maxHeap.poll();
            CustomPrint.println(topEntry.getKey() + ": " + topEntry.getValue() + " times");
        }
    }

    /**
     * Prints the frequency of each word in the word frequency map.
     */
    public static void printWordFrequencies() {
        CustomPrint.println("\nWord frequencies:");
        // Prints the word frequency of each word in the map
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            CustomPrint.println(entry.getKey() + ": " + entry.getValue() + " times");
        }
    }

    /**
     * Prints the contents of the max heap.
     */
    public static void printMaxHeap() {
        CustomPrint.println("\nMax Heap contents:");

        while (!maxHeap.isEmpty()) {
            Map.Entry<String, Integer> entry = maxHeap.poll();
            CustomPrint.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        int k = 5;
        String filePath = ".\\res\\doc\\sherlock.txt";
        String text = readTextFile(filePath);
        CustomPrint.println("Reading file from location: " + filePath);
        CustomPrint.println("File reading ended " + (text.isEmpty()? "failed" : "successful"));
        // Adding words from the provided text to the word frequency map
        addWordsToMap(text);
        // Printing the top k words and their frequencies
        topKWords(k);
        // Sentence to add more words to the map
        CustomPrint.println("\n3000 entries of word \"Sherlock\" and \"Holmes\" is added, reanalyzing top " + k + " words:");
        String textToAdd = "";
        for (int i = 0; i < 3000; i++) {
            textToAdd += " Sherlock Holmes ";
        }
        // Adding more words from the provided text to the word frequency map
        addWordsToMap(textToAdd);
        // Printing the top k words and their frequencies after adding more words
        topKWords(k);
        // Sentence to remove words from the word frequency map
        filePath = ".\\res\\doc\\sherlock_sub.txt";
        String sentenceToRemove = readTextFile(filePath);
        CustomPrint.println("\nReading file from location: " + filePath);
        CustomPrint.println("File reading ended " + (sentenceToRemove.isEmpty()? "failed" : "successful"));
        // Removing words from the map based on the provided sentence
        removeWordsFromMap(sentenceToRemove);
        CustomPrint.println("Many words removed, reanalyzing top " + k + " words:");
        // Printing the top k words and their frequencies after removal
        topKWords(k);
        CustomPrint.println("\nCleaning map");
        // Clearing the word frequency map and max heap
        cleanWordFrequencyMap();
        // Printing top k words after cleaning the map
        topKWords(k);
    }
}
