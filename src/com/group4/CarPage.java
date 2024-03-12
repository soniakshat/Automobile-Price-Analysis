
package com.group4;

import java.util.List;
/**
 * This class provides a simple interface of a page containing information about a car
 * @author Harsh Verma
 */
public class CarPage {
    private String url;
    private int frequencyCount;
    private List<String> searchKeywords;
    
    /**
     * Constructs a CarPage object with the given parameters
     * 
     * @param url the URL of the page
     * @param frequencyCount the frequency count of the page
     * @param searchKeywords the search keywords related to the page
     * @author Harsh Verma
     */

    public CarPage(String url, int frequencyCount, List<String> searchKeywords) {
        this.url = url;
        this.frequencyCount = frequencyCount;
        this.searchKeywords = searchKeywords;
    }

    /**
     * Calculates the rank of the page based on its frequency count and search keywords
     * 
     * @return the rank of the page
     * @author Harsh Verma
     */

    public double calculateRank() {
        return frequencyCount * 0.6 + searchKeywords.size() * 0.4;
    }

    // Returns the URL of the page
    public String getUrl() {
        return url;
    }

    
    // Returns the search keywords related to the page
    public List<String> getSearchKeywords() {
        return searchKeywords;
    }
}
