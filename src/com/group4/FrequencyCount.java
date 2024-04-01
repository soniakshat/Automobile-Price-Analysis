package com.group4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class to count frequency of words in provided text
 *
 * @author Akshat Soni
 */
public class FrequencyCount {
    private final Map<String, Integer> wordFrequency = new HashMap<>();

    // Priority queue maxHeap to maintain the top words
    private final PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
            (a, b) -> b.getValue() - a.getValue());

    /**
     * Adds words from the given text to the word frequency map.
     * Removes punctuation, including the full stop, from the input text.
     *
     * @param text The input text to add words from.
     */
    public void addWordsToMap(String text) {
        addWordsToMap(Utils.generateBagOfWords(text));
    }

    /**
     * Adds words from the given text to the word frequency map.
     * Removes punctuation, including the full stop, from the input text.
     *
     * @param words The list of words to add.
     */
    public void addWordsToMap(List<String> words) {
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
    public void removeWordsFromMap(String text) {
        removeWordsFromMap(Utils.generateBagOfWords(text));
    }

    /**
     * Removes one occurrence of each word from the specified sentence
     * from the word frequency map. If a word is not present, it does nothing.
     *
     * @param words The list of words to remove.
     */
    public void removeWordsFromMap(List<String> words) {
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
    private void updateMaxHeap() {
        maxHeap.clear();
        maxHeap.addAll(wordFrequency.entrySet());
    }

    /**
     * Cleans the word frequency map by clearing its contents.
     */
    public void cleanWordFrequencyMap() {
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
    public void topKWords(int k) {
        if (maxHeap.isEmpty()) {
            CustomPrint.println(STR."Max Heap is empty. Cannot get top \{k} words.");
            return;
        }

        // Printing top k words from the max heap priority queue
        CustomPrint.println(STR."Top \{k} words and their frequencies:");
        for (int i = 0; i < k; i++) {
            if (maxHeap.isEmpty()) {
//                CustomPrint.println(STR."Wanted \{k}, but there are only \{i} unique words in the text file.");
                return;
            }
            Map.Entry<String, Integer> topEntry = maxHeap.poll();
            CustomPrint.println(STR."\{topEntry.getKey()}: \{topEntry.getValue()} times");
        }
    }

    public boolean isFrequencyDataEmpty(){
        return maxHeap.isEmpty();
    }
    public static void main(String[] args) {
        FrequencyCount frequencyCount = new FrequencyCount();
        int k = 5;
        String filePath = ".\\res\\doc\\sherlock.txt";
        String text = Utils.readTextFile(filePath);
        CustomPrint.println(STR."Reading file from location: \{filePath}");
        CustomPrint.println(STR."File reading ended \{text.isEmpty() ? "failed" : "successful"}");
        // Adding words from the provided text to the word frequency map
        frequencyCount.addWordsToMap(text);
        // Printing the top k words and their frequencies
        frequencyCount.topKWords(k);
        // Sentence to add more words to the map
        CustomPrint.println(STR."\n3000 entries of word \"Sherlock\" and \"Holmes\" is added, reanalyzing top \{k} words:");

        // Adding more words from the provided text to the word frequency map
        frequencyCount.addWordsToMap(" Sherlock Holmes ".repeat(3000)
                // Adding more words from the provided text to the word frequency map
        );

        // Printing the top k words and their frequencies after adding more words
        frequencyCount.topKWords(k);
        // Sentence to remove words from the word frequency map
        filePath = ".\\res\\doc\\sherlock_sub.txt";
        String sentenceToRemove = Utils.readTextFile(filePath);
        CustomPrint.println(STR."\nReading file from location: \{filePath}");
        CustomPrint.println(STR."File reading ended \{sentenceToRemove.isEmpty() ? "failed" : "successful"}");
        // Removing words from the map based on the provided sentence
        frequencyCount.removeWordsFromMap(sentenceToRemove);
        CustomPrint.println(STR."Many words removed, reanalyzing top \{k} words:");
        // Printing the top k words and their frequencies after removal
        frequencyCount.topKWords(k);
        CustomPrint.println("\nCleaning map");
        // Clearing the word frequency map and max heap
        frequencyCount.cleanWordFrequencyMap();
        // Printing top k words after cleaning the map
        frequencyCount.topKWords(k);
    }
}
