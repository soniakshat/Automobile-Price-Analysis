package com.group4;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;

    TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}

class Trie {
    private final TrieNode root;

    Trie() {
        root = new TrieNode();
    }

    public void insertAll(List<String> words) {
        for (String word : words) {
            insert(word);
        }
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                node = new TrieNode();
                current.children.put(ch, node);
            }
            current = node;
        }
        current.isEndOfWord = true;
    }

    // Returns true if the word is in the trie.
    public boolean search(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord;
    }

    // Returns a list of words with the given prefix
    public List<String> findWordsWithPrefix(String prefix) {
        List<String> words = new ArrayList<>();
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return words; // No words with this prefix
            }
            current = node;
        }
        findAllWords(current, prefix, words);
        return words;
    }

    private void findAllWords(TrieNode node, String prefix, List<String> words) {
        if (node.isEndOfWord) {
            words.add(prefix);
        }
        for (char ch : node.children.keySet()) {
            findAllWords(node.children.get(ch), prefix + ch, words);
        }
    }
}

public class WordCompletion {
    private static final Trie trie = new Trie();

    public static void insertWordsForCompletion(Set<String> bagOfWords){
        trie.insertAll(Driver.bagOfWords.stream().toList());
    }

    public static void main(String[] args) {
        // Example usage
        // Inserting some sample automobile data into the trie

        Driver.bagOfWords.add("Toyota Camry");
        Driver.bagOfWords.add("Toyota Corolla");
        Driver.bagOfWords.add("Toyota Highlander");
        Driver.bagOfWords.add("Honda Civic");
        Driver.bagOfWords.add("Honda Accord");
        Driver.bagOfWords.add("Ford Mustang");

        trie.insertAll(Driver.bagOfWords.stream().toList());
        completionSuggestions("Hon");
    }

    public static List<String> completionSuggestions(String incompleteWord) {
        List<String> suggestionList = trie.findWordsWithPrefix(incompleteWord);
        return suggestionList;
    }
}

