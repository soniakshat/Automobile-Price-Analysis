package com.group4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PageRanking {
    private List<Map<String, Object>> pages;

    public PageRanking() {
        pages = new ArrayList<>();
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
        Collections.sort(filteredPages, Comparator.comparingInt(p -> Integer.parseInt(p.get("price").toString())));
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
}
