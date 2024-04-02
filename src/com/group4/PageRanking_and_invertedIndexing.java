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
    private final List<Map<String, Object>> pages; // List to store pages
    private final Map<String, Set<Integer>> invertedIndex; // Inverted index for efficient keyword search

    public PageRanking_and_invertedIndexing() {
        pages = new ArrayList<>(); // Initialize list of pages
        invertedIndex = new HashMap<>(); // Initialize inverted index
    }

    /**
     * This method builds an inverted index to facilitate keyword-based search.
     * It maps each keyword to a set of indices indicating the pages containing that keyword.
     */
    void buildInvertedIndex() {
        for (int i = 0; i < pages.size(); i++) {
            Map<String, Object> page = pages.get(i);
            // Extract the name of the page and convert it to lowercase
            String name = page.get("name").toString().toLowerCase();
            // Split the name into individual keywords based on whitespace
            String[] keywords = name.split("\\s+");

            // Iterate over each keyword in the page's name
            for (String keyword : keywords) {
                // Add the keyword to the inverted index if it does not exist, initializing an empty set
                invertedIndex.putIfAbsent(keyword, new HashSet<>());
                // Add the index of the current page to the set associated with the keyword in the inverted index
                invertedIndex.get(keyword).add(i);
            }
        }
    }

    /**
     * This method performs a keyword-based search using the inverted index.
     * It returns a list of pages containing the specified keyword.
     *
     * @param keyword The keyword to search for
     * @return A list of pages containing the specified keyword
     */
    public List<Map<String, Object>> searchByKeyword(String keyword) {
        // Check if the keyword exists in the inverted index
        if (!invertedIndex.containsKey(keyword.toLowerCase())) {
            // If the keyword is not found, return an empty list
            return Collections.emptyList();
        }

        // Retrieve the set of indices associated with the keyword in the inverted index
        Set<Integer> indices = invertedIndex.get(keyword.toLowerCase());
        // Create a list to store the pages containing the keyword
        List<Map<String, Object>> result = new ArrayList<>();
        // Iterate over the indices and retrieve the corresponding pages from the pages list
        for (int index : indices) {
            result.add(pages.get(index));
        }
        // Return the list of pages containing the keyword
        return result;
    }

    /**
     * This method reads pages from a JSON file and populates the 'pages' list with the parsed data.
     *
     * @param filename The name of the JSON file to read pages from
     */
    public void readPages(String filename) {
        JSONParser parser = new JSONParser();

        try {
            // Parse the JSON file using the JSONParser
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filename));

            // Iterate over each object in the JSON array
            for (Object o : jsonArray) {
                // Convert each object to a JSONObject
                JSONObject jsonObject = (JSONObject) o;

                // Create a new Map to represent a page
                Map<String, Object> page = new HashMap<>();

                // Populate the page map with data from the JSON object
                page.put("name", jsonObject.get("name"));
                page.put("price", jsonObject.get("price"));
                page.put("fuelType", jsonObject.get("fuelType"));
                page.put("transmission", jsonObject.get("transmission"));
                page.put("image", jsonObject.get("image"));
                page.put("kms", jsonObject.get("kms"));

                // Add the populated page map to the 'pages' list
                pages.add(page);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace(); // Print stack trace in case of I/O or parsing exception
        }
    }

    /**
     * This method ranks the pages by name based on the specified search name.
     *
     * @param searchName The name to be searched within the page names for ranking.
     */
    public void rankByName(String searchName) {
        // Sort pages by name containing the search name
        pages.sort((p1, p2) -> {
            String name1 = p1.get("name").toString();
            String name2 = p2.get("name").toString();
            boolean containsName1 = name1.toLowerCase().contains(searchName.toLowerCase());
            boolean containsName2 = name2.toLowerCase().contains(searchName.toLowerCase());
            if (containsName1 && !containsName2) {
                return -1; // Page containing search name comes before page without search name
            } else if (!containsName1 && containsName2) {
                return 1; // Page containing search name comes after page without search name
            } else {
                return name1.compareToIgnoreCase(name2); // Sort alphabetically if both pages contain or do not contain search name
            }
        });
    }

    /**
     * This method retrieves pages within the specified price range.
     *
     * @param minPrice The minimum price of the range
     * @param maxPrice The maximum price of the range
     * @return A list of pages within the specified price range
     */
    public List<Map<String, Object>> getPagesInPriceRange(int minPrice, int maxPrice) {
        // Create a list to store the filtered pages within the price range
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        // Iterate over each page in the pages list
        for (Map<String, Object> page : pages) {
            // Retrieve the price of the current page and parse it to an integer
            int price = Integer.parseInt(page.get("price").toString());
            // Check if the price falls within the specified range
            if (price >= minPrice && price <= maxPrice) {
                // If the price is within the range, add the page to the filteredPages list
                filteredPages.add(page);
            }
        }
        // Return the list of filtered pages within the price range
        return filteredPages;
    }

    /**
     * This method retrieves pages within the specified kilometers driven range.
     *
     * @param minKms The minimum value of kilometers driven for the range
     * @param maxKms The maximum value of kilometers driven for the range
     * @return A list of pages within the specified kilometers driven range
     */
    public List<Map<String, Object>> getPagesInKmsRange(int minKms, int maxKms) {
        // Create a list to store the filtered pages within the kilometers driven range
        List<Map<String, Object>> filteredPages = new ArrayList<>();
        // Iterate over each page in the pages list
        for (Map<String, Object> page : pages) {
            // Retrieve the kilometers driven of the current page and parse it to an integer
            int kms = Integer.parseInt(page.get("kms").toString());
            // Check if the kilometers driven falls within the specified range
            if (kms >= minKms && kms <= maxKms) {
                // If the kilometers driven is within the range, add the page to the filteredPages list
                filteredPages.add(page);
            }
        }
        // Return the list of filtered pages within the kilometers driven range
        return filteredPages;
    }

    /**
     * This method retrieves pages filtered by a specified transmission type.
     *
     * @param selectedTransmission The transmission type to filter the pages
     * @return A list of pages filtered by the specified transmission type
     */
    public List<Map<String, Object>> getPagesByTransmission(String selectedTransmission) {
        // Use Java Streams to filter the pages by the specified transmission type
        // Filter each page based on whether its transmission type matches the selectedTransmission
        // Ignore case during the comparison
        return pages.stream()
                .filter(page -> page.get("transmission").toString().equalsIgnoreCase(selectedTransmission))
                .collect(Collectors.toList()); // Collect the filtered pages into a list and return
    }

    /**
     * This method retrieves pages filtered by a specified fuel type.
     *
     * @param selectedFuelType The fuel type to filter the pages
     * @return A list of pages filtered by the specified fuel type
     */
    public List<Map<String, Object>> getPagesByFuelType(String selectedFuelType) {
        // Use Java Streams to filter the pages by the specified fuel type
        // Filter each page based on whether its fuel type matches the selectedFuelType
        // Ignore case during the comparison
        return pages.stream()
                .filter(page -> page.get("fuelType").toString().equalsIgnoreCase(selectedFuelType))
                .collect(Collectors.toList()); // Collect the filtered pages into a list and return
    }

    /**
     * This method retrieves pages that have an associated image.
     *
     * @return A list of pages with images
     */
    public List<Map<String, Object>> getPagesWithImage() {
        // Use Java Streams to filter the pages based on whether they have an associated image
        // Filter each page based on whether the "image" field is not null
        return pages.stream()
                .filter(page -> page.get("image") != null)
                .collect(Collectors.toList()); // Collect the filtered pages into a list and return
    }
}
