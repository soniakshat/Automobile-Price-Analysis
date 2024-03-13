package com.group4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * Class to crawl web
 * @author Akshat Soni
 * */
public class WebCrawler {

    public static void main(String[] args) {
        String[] urls = {"https://www.motorcitychrysler.ca/used/", "https://www.carpages.ca/ontario/windsor/used-cars/"};

        for (String url : urls) {
            fetchAndSaveHtml(url);
        }
    }

    public static void fetchAndSaveHtml(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            String htmlCode = document.html();

            String fileName = "html_output_" + System.currentTimeMillis() + ".txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(htmlCode);
            }

            System.out.println("HTML code from " + url + " saved to file: " + fileName);
            System.out.println("\n------------------------------------------------\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
