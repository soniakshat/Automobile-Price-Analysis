package com.group4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PageRanking {
    private List<Map<String, Object>> pages;
    private Map<String, Set<Integer>> invertedIndex;

    public PageRanking() {

        pages = new ArrayList<>();
        invertedIndex = new HashMap<>();
    }

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
                pages.add(page);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Rank pages by price
    public void rankByPrice(int minPrice, int maxPrice) {
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            int price = Integer.parseInt(page.get("price").toString());
            if (price >= minPrice && price <= maxPrice) {
                filteredPages.add(page);
            }
        }
        filteredPages.sort(Comparator.comparingInt(p -> Integer.parseInt(p.get("price").toString())));
        pages = filteredPages;
    }

    public void rankByTransmission(int selectionTransmissionType) {
        String[] transmissionTypes = {"Automatic", "NA", "Manual"};
        String selectedTransmission = transmissionTypes[selectionTransmissionType - 1];

        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            String transmission = page.get("transmission").toString();
            if (transmission.equalsIgnoreCase(selectedTransmission)) {
                filteredPages.add(page);
            }
        }
        pages = filteredPages;
    }

    public void rankByFuelType(int selectionFuelType) {
        String[] fuelTypes = {"Gas", "Diesel", "Electric", "Hybrid", "Other"};
        String selectedFuelType = fuelTypes[selectionFuelType - 1];

        List<Map<String, Object>> filteredPages = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            String fuelType = page.get("fuelType").toString();
            if (fuelType.equalsIgnoreCase(selectedFuelType)) {
                filteredPages.add(page);
            }
        }
        pages = filteredPages;
    }

    // Rank pages by image availability
    public void rankByImageAvailability() {
        Collections.sort(pages, (p1, p2) -> {
            String imageUrl1 = p1.get("image") != null ? p1.get("image").toString() : "";
            String imageUrl2 = p2.get("image") != null ? p2.get("image").toString() : "";
            return imageUrl1.compareTo(imageUrl2);
        });
    }

    // Rank pages by kilometers driven
    public void rankByKmsDriven() {
        Collections.sort(pages, Comparator.comparingInt(p -> Integer.parseInt(p.get("kms").toString())));
    }

    // Rank pages by name
    public void rankByName(String searchName) {
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

    public void printRankedPages() {
        for (Map<String, Object> page : pages) {
            System.out.println("Name: " + page.get("name") + ", Price: " + page.get("price"));
        }
    }

    public List<Map<String, Object>> getRankedPages() {
        return pages;
    }

    // Build inverted index
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

    // Perform search using inverted index
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

    // New method to filter pages within the specified kms range
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

    public List<Map<String, Object>> getPagesByTransmission(int selectionTransmissionType) {
        String[] transmissionTypes = {"Automatic", "NA", "Manual"};
        String selectedTransmission = transmissionTypes[selectionTransmissionType - 1];

        return pages.stream()
                .filter(page -> page.get("transmission").toString().equalsIgnoreCase(selectedTransmission))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPagesByFuelType(int selectionFuelType) {
        String[] fuelTypes = {"Gas", "Diesel", "Electric", "Hybrid", "Other"};
        String selectedFuelType = fuelTypes[selectionFuelType - 1];

        return pages.stream()
                .filter(page -> page.get("fuelType").toString().equalsIgnoreCase(selectedFuelType))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPagesWithImage() {
        return pages.stream()
                .filter(page -> page.get("image") != null)
                .collect(Collectors.toList());
    }
}
