package com.group4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class HtmlParser {
    private static final String directoryPath = ".\\res\\generated\\pages\\";

    public static void main(String[] args) {
        SearchCarInPage("Jeep");
    }

    public static void SearchCarInPage(String carName) {
        try {
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        parseCarInformation(file, carName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseCarInformation(File file, String carName) throws IOException {
        CustomPrint.print("Parser", STR."Parsing through: \{file.getName()}");
        Document doc = Jsoup.parse(file, "UTF-8");

        Elements carElements = doc.getElementsByAttributeValueContaining("title", carName);

        for (Element carElement : carElements) {

            String title = carElement.attr("title");

            Element imgElement = carElement.selectFirst("img[src]");
            String imgUrl = imgElement != null ? imgElement.attr("src") : "N/A";

            CustomPrint.println(STR."Title: \{title}");
            CustomPrint.println(STR."Image URL: \{imgUrl}\n");
        }
    }
}

