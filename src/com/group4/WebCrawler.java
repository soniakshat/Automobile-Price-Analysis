package com.group4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class to crawl web
 *
 * @author Haseeb Shams
 */
public class WebCrawler {
    static final String folderPath = ".\\res\\pages\\";
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

            File webPageFolder = new File(folderPath);

            if(!webPageFolder.exists()){
                Files.createDirectories(Paths.get(folderPath));
            }

            if(webPageFolder.exists() && webPageFolder.isDirectory()){

                String fileName = "html_output_" + System.currentTimeMillis() + ".txt";
                try (FileWriter writer = new FileWriter(folderPath+fileName)) {
                    writer.write(htmlCode);
                }

                CustomPrint.println("HTML code from " + url + " saved to file: " + fileName);
                CustomPrint.println("\n------------------------------------------------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
