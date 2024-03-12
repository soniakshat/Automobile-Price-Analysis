package com.group4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Class for inverted indexing
 *  @author Harsh Verma
 * */
public class InvertedIndexing {
    private Map<String, Set<String>> invertedIndexing;

    public InvertedIndexing() {
        this.invertedIndexing = new HashMap<>();
    }

    public void addDocument(String document, List<String> terms) {
        for (String term : terms) {
            invertedIndexing.computeIfAbsent(term.toLowerCase(), k -> new HashSet<>()).add(document);
        }
    }

    public Set<String> search(String term) {
        return invertedIndexing.getOrDefault(term.toLowerCase(), new HashSet<>());
    }
}
