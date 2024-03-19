package com.group4;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellChecking {

    private final Set<String> dictionary;

    public SpellChecking(Set<String> dictionary) {
        this.dictionary = dictionary;
    }

    // Method to find the closest word in the dictionary using edit distance
    public String findClosestWord(String word) {
        int minDistance = 4;
        String closestWord = null;

        for (String dictWord : dictionary) {
            int distance = editDistance(word, dictWord);
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = dictWord;
            }
        }

        return closestWord;
    }

    // Method to calculate the edit distance between two words
    private int editDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        // Example dictionary
        Set<String> dictionary = new HashSet<>();
        dictionary.add("consider");
        dictionary.add("minute");
        dictionary.add("accord");
        dictionary.add("evident");
        dictionary.add("practice");
        dictionary.add("intend");
        dictionary.add("concern");
        dictionary.add("commit");
        dictionary.add("establish");
        dictionary.add("approach");
        dictionary.add("obtain");
        dictionary.add("abstain");

        SpellChecking spellChecker = new SpellChecking(dictionary);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word for spell check: ");
        String inputWord = scanner.nextLine().toLowerCase();

        if (dictionary.contains(inputWord)) {
            System.out.println("Word is spelled correctly.");
        } else {
            String closestWord = spellChecker.findClosestWord(inputWord);
            if (closestWord != null) {
                System.out.println("Did you mean: " + closestWord + "?");
            } else {
                System.out.println("No alternative word suggestion found.");
            }
        }

        scanner.close();
    }
}