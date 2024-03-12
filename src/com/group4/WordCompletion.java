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
    private TrieNode root;

    Trie() {
        root = new TrieNode();
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
    public static void main(String[] args) {
        // Example usage
        Trie trie = new Trie();
        // Inserting some sample automobile data into the trie
        trie.insert("Toyota Camry");
        trie.insert("Toyota Corolla");
        trie.insert("Toyota Highlander");
        trie.insert("Honda Civic");
        trie.insert("Honda Accord");
        trie.insert("Ford Mustang");

        String prefix = "Hon";
        List<String> completions = trie.findWordsWithPrefix(prefix);
        CustomPrint.println("Automobile models with prefix '" + prefix + "': " + completions);
    }
}
