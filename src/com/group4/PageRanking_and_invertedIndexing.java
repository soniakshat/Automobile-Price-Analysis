package com.group4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PageRanking_and_invertedIndexing {
    private List<Map<String, Object>> pages; // List to store pages
    private final Map<String, Set<Integer>> invertedIndex; // Inverted index for efficient keyword search

    public PageRanking_and_invertedIndexing() {
        pages = new ArrayList<>(); // Initialize list of pages
        invertedIndex = new HashMap<>(); // Initialize inverted index
    }

    // Method to read pages from JSON file
    public void readPages(String filename) {
        JSONParser parser = new JSONParser();

        try {
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filename));

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Map<String, Object> page = new HashMap<>();
                page.put("name", jsonObject.get("name"));
                page.put("price", jsonObject.get("price"));
                page.put("fuelType", jsonObject.get("fuelType"));
                page.put("transmission", jsonObject.get("transmission"));
                page.put("image", jsonObject.get("image"));
                page.put("kms", jsonObject.get("kms"));
                pages.add(page); // Add page to the list
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Rank pages by price within the specified range
    public void rankByPrice(int minPrice, int maxPrice) {
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            int price = Integer.parseInt(page.get("price").toString());
            if (price >= minPrice && price <= maxPrice) {
                filteredPages.add(page);
            }
        }
        // Sort filtered pages by price in increasing order
        filteredPages.sort(Comparator.comparingInt(p -> Integer.parseInt(p.get("price").toString())));
        pages = filteredPages; // Update pages with filtered and sorted pages
    }

    // Rank pages by transmission type
    public void rankByTransmission(int selectionTransmissionType) {
        String[] transmissionTypes = {"Automatic", "NA", "Manual"};
        String selectedTransmission = transmissionTypes[selectionTransmissionType - 1];

        // Filter pages by selected transmission type
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            String transmission = page.get("transmission").toString();
            if (transmission.equalsIgnoreCase(selectedTransmission)) {
                filteredPages.add(page);
            }
        }
        pages = filteredPages; // Update pages with filtered pages
    }

    // Rank pages by fuel type
    public void rankByFuelType(int selectionFuelType) {
        String[] fuelTypes = {"Gas", "Diesel", "Electric", "Hybrid", "Other"};
        String selectedFuelType = fuelTypes[selectionFuelType - 1];

        // Filter pages by selected fuel type
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            String fuelType = page.get("fuelType").toString();
            if (fuelType.equalsIgnoreCase(selectedFuelType)) {
                filteredPages.add(page);
            }
        }
        pages = filteredPages; // Update pages with filtered pages
    }

    // Rank pages by image availability
    public void rankByImageAvailability() {
        // Sort pages by image availability
        Collections.sort(pages, (p1, p2) -> {
            String imageUrl1 = p1.get("image") != null ? p1.get("image").toString() : "";
            String imageUrl2 = p2.get("image") != null ? p2.get("image").toString() : "";
            return imageUrl1.compareTo(imageUrl2);
        });
    }

    // Rank pages by kilometers driven
    public void rankByKmsDriven() {
        // Sort pages by kilometers driven
        Collections.sort(pages, Comparator.comparingInt(p -> Integer.parseInt(p.get("kms").toString())));
    }

    // Rank pages by name containing the specified search name
    public void rankByName(String searchName) {
        // Sort pages by name containing the search name
        Collections.sort(pages, (p1, p2) -> {
            String name1 = p1.get("name").toString();
            String name2 = p2.get("name").toString();
            boolean containsName1 = name1.toLowerCase().contains(searchName.toLowerCase());
            boolean containsName2 = name2.toLowerCase().contains(searchName.toLowerCase());
            if (containsName1 && !containsName2) {
                return -1;
            } else if (!containsName1 && containsName2) {
                return 1;
            } else {
                return name1.compareToIgnoreCase(name2);
            }
        });
    }

    // Method to print ranked pages
    public void printRankedPages() {
        for (Map<String, Object> page : pages) {
            System.out.println("Name: " + page.get("name") + ", Price: " + page.get("price"));
        }
    }

    // Method to get ranked pages
    public List<Map<String, Object>> getRankedPages() {
        return pages;
    }

    // Method to build inverted index for efficient keyword search
    void buildInvertedIndex() {
        for (int i = 0; i < pages.size(); i++) {
            Map<String, Object> page = pages.get(i);
            String name = page.get("name").toString().toLowerCase();
            List<String> keywords = Arrays.asList(name.split("\\s+"));

            for (String keyword : keywords) {
                invertedIndex.putIfAbsent(keyword, new HashSet<>());
                invertedIndex.get(keyword).add(i);
            }
        }
    }

    // Method to perform search using inverted index
    public List<Map<String, Object>> searchByKeyword(String keyword) {
        if (!invertedIndex.containsKey(keyword.toLowerCase())) {
            return Collections.emptyList();
        }

        Set<Integer> indices = invertedIndex.get(keyword.toLowerCase());
        List<Map<String, Object>> result = new ArrayList<>();
        for (int index : indices) {
            result.add(pages.get(index));
        }
        return result;
    }

    // Method to filter pages within the specified price range
    public List<Map<String, Object>> getPagesInPriceRange(int minPrice, int maxPrice) {
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            int price = Integer.parseInt(page.get("price").toString());
            if (price >= minPrice && price <= maxPrice) {
                filteredPages.add(page);
            }
        }
        return filteredPages;
    }

    // Method to filter pages within the specified kms range
    public List<Map<String, Object>> getPagesInKmsRange(int minKms, int maxKms) {
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            int kms = Integer.parseInt(page.get("kms").toString());
            if (kms >= minKms && kms <= maxKms) {
                filteredPages.add(page);
            }
        }
        return filteredPages;
    }

    // Method to filter pages by transmission type
    public List<Map<String, Object>> getPagesByTransmission(String selectedTransmission) {
        return pages.stream()
                .filter(page -> page.get("transmission").toString().equalsIgnoreCase(selectedTransmission))
                .collect(Collectors.toList());
    }

    // Method to filter pages by fuel type
    public List<Map<String, Object>> getPagesByFuelType(String selectedFuelType) {
        return pages.stream()
                .filter(page -> page.get("fuelType").toString().equalsIgnoreCase(selectedFuelType))
                .collect(Collectors.toList());
    }

    // Method to filter pages with image availability
    public List<Map<String, Object>> getPagesWithImage() {
        return pages.stream()
                .filter(page -> page.get("image") != null)
                .collect(Collectors.toList());
    }
}