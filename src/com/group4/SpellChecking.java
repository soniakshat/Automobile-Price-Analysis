package com.group4;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellChecking {
//    private static final Set<String> dictionary = new HashSet<>();

    // Method to find the closest word in the dictionary using edit distance
    public static String findClosestWord(String word) {
        int minDistance = 4;
        String closestWord = null;

        for (String dictWord : Driver.bagOfWords) {
            int distance = editDistance(word, dictWord);
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = dictWord;
            }
        }

        return closestWord;
    }

    // Method to calculate the edit distance between two words
    private static int editDistance(String word1, String word2) {
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
        Driver.bagOfWords.add("consider");
        Driver.bagOfWords.add("minute");
        Driver.bagOfWords.add("accord");
        Driver.bagOfWords.add("evident");
        Driver.bagOfWords.add("practice");
        Driver.bagOfWords.add("intend");
        Driver.bagOfWords.add("concern");
        Driver.bagOfWords.add("commit");
        Driver.bagOfWords.add("establish");
        Driver.bagOfWords.add("approach");
        Driver.bagOfWords.add("obtain");
        Driver.bagOfWords.add("abstain");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word for spell check: ");
        String inputWord = scanner.nextLine().toLowerCase();
        checkSpelling(inputWord);
        scanner.close();
    }

    public static String checkSpelling(String inputWord) {
        if (!Driver.bagOfWords.contains(inputWord)) {
            String closestWord = findClosestWord(inputWord);
            if (closestWord != null) {
                CustomPrint.println("SpellChecker", STR."Did you mean: \{closestWord}?");
                return closestWord;
            } else {
                CustomPrint.printError("Spell Checker", "No alternative word suggestion found.");
                return inputWord;
            }
        }
        return inputWord;
    }
}