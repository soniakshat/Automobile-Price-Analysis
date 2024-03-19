package com.group4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class HtmlParser {

    public static void main(String[] args) {
        String carName = "Jeep Gladiator"; // Example car name provided by the user
        String directoryPath = "/Users/haseebshams/eclipse-workspace/Project-class/";

        try {
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".html")) {
                        parseCarInformation(file, carName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseCarInformation(File file, String carName) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");

        Elements carElements = doc.getElementsByAttributeValueContaining("title", carName);

        for (Element carElement : carElements) {
       
            String title = carElement.attr("title");

            Element imgElement = carElement.selectFirst("img[src]");
            String imgUrl = imgElement != null ? imgElement.attr("src") : "N/A";

      
            System.out.println("Title: " + title);
            System.out.println("Image URL: " + imgUrl);
            System.out.println();
        }
    }
}

