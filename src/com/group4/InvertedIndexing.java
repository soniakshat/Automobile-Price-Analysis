package com.group4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndexing {
    private Map<String, Set<Integer>> invertedIndex;

    public InvertedIndexing() {
        invertedIndex = new HashMap<>();
    }

    public void buildIndex(String filename) {
        JSONParser parser = new JSONParser();

        try {
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filename));

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String[] words = ((String) jsonObject.get("name")).split("\\s+");
                for (String word : words) {
                    invertedIndex.computeIfAbsent(word.toLowerCase(), k -> new HashSet<>()).add(i);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Set<Integer>> getInvertedIndex() {
        return invertedIndex;
    }
}
