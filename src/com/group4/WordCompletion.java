package com.group4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WordCompletion {
    private static Map<String, String> myDictionary = new HashMap<>();

    // Function to return similar words
    private static List<String> fuzzySearch(String query) {
        List<String> similarWords = new ArrayList<>();

        // Calculate Edit distance between two words
        for (String word : myDictionary.keySet()) {
            int distance = calculateEditDistance(query, word.toLowerCase());
            if (distance <= 3) { // Set a threshold(max distance) for similarity
                similarWords.add(word);
            }
        }
        return similarWords;
    }

    // Function for edit distance
    private static int calculateEditDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int a = 0; a <= s1.length(); a++) {
            for (int b = 0; b <= s2.length(); b++) {
                if (a == 0) {
                    dp[a][b] = b;
                } else if (b == 0) {
                    dp[a][b] = a;
                } else {
                    dp[a][b] = min(
                            dp[a - 1][b - 1] + (s1.charAt(a - 1) == s2.charAt(b - 1) ? 0 : 1),
                            dp[a - 1][b] + 1,
                            dp[a][b - 1] + 1);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    // To get minimum distance
    private static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }


    public static void main(String[] args) {

        // Pre-Adding words and meanings to the dictionary
        System.out.println("Pre adding words: ");
        myDictionary.put("consider", "deem to be");
        myDictionary.put("minute", "infinitely or immeasurably small");
        myDictionary.put("accord", "concurrence of opinion");
        myDictionary.put("evident", "clearly revealed to the mind or the senses or judgement");
        myDictionary.put("practice", "a customary way of operation or behaviour");
        myDictionary.put("intend", "have in mind as purpose");
        myDictionary.put("concern", "something that interests you because it is important");
        myDictionary.put("commit", "deem to be");
        myDictionary.put("establish", "set up or found");
        myDictionary.put("approach", "move forward");
        myDictionary.put("obtain", "to come into possesion of somthing");
        myDictionary.put("abstain", "restrain oneself");
        System.out.println("\n\n");
        // PROGRAM
        // Print Menu
        System.out.println("Enter '1' to ADD a word to dictionary \n"
                + "Enter '2' to REMOVE a word from dictionary \n"
                + "Enter '3' to LOOK for a word's meaning \n"
                + "Enter '4' to LIST all words in the dictionary \n"
                + "Enter 'X' to EXIT");

        String word, meaning;
        String choice=null;
        Scanner scn = new Scanner(System.in);
        // looping switch
        while(choice!="X") {
            System.out.println("\nEnter your choice: ");
            choice = scn.nextLine();
            // switch case for the menu
            switch(choice) {
                // ADD
                case "1":
                    System.out.println("Enter Word: ");
                    word = scn.nextLine();
                    if (myDictionary.containsKey(word) != true) {
                        System.out.println("Enter its Meaning: ");
                        meaning = scn.nextLine();
                        myDictionary.put(word, meaning);
                    }else
                        System.out.print("Word already present!! Try another word.\n");
                    break;
                // REMOVE
                case "2":
                    System.out.println("Enter Word to remove: ");
                    word = scn.nextLine();
                    if (myDictionary.containsKey(word) == true)
                        myDictionary.remove(word);
                    else
                        System.out.println("WORD NOT FOUND!!\n"
                                + "Suggested words for:\n '" + word + "': " + fuzzySearch(word));
                    break;
                // SEARCH | LOOKUP
                case "3":
                    System.out.println("Enter Word for its meaning: ");
                    word = scn.nextLine();
                    if (myDictionary.containsKey(word) == true)
                        System.out.println("Meaning: " + myDictionary.get(word));
                    else
                        System.out.println("WORD NOT FOUND!!\n"
                                + "Suggested words for:\n '" + word + "': " + fuzzySearch(word));
                    break;
                // LIST	ALL
                case "4":
                    System.out.println("Your Dictionary: ");
                    CustomPrint.println(myDictionary);
                    break;
                // EXIT
                case "X": case "x":
                    System.out.println("Thank You");
                    System.exit(0);
                    // WRONG INPUT
                default:
                    System.out.println("Plz enter a valid choice: ");
            }
        }
        scn.close();
    }
}