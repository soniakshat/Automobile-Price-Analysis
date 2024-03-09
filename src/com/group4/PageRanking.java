package com.group4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Class for page ranking
 *  @author Akshat Soni
 * */
public class PageRanking {
    public static List<CarPage> rankPages(List<CarPage> pages) {
        List<CarPage> sortedPages = new ArrayList<>(pages);
        Collections.sort(sortedPages, Comparator.comparingDouble(CarPage::calculateRank).reversed());
        return sortedPages;
    }
}