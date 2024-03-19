package com.group4;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * Class to crawl web
 * @author Akshat Soni
 * */
public class WebCrawler {
	 public static void main(String[] args) {
	        String[] urls = {"https://www.motorcitychrysler.ca/used/", "https://www.carpages.ca/ontario/windsor/used-cars/", "https://www.autotrader.ca/cars/on/windsor/"};

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

	            CustomPrint.println("HTML code from " + url + " saved to file: " + fileName);
	            CustomPrint.println("\n------------------------------------------------\n");

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
}
