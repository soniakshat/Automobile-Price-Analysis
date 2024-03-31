package com.group4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Class to crawl web
 *
 * @author Haseeb Shams
 */
public class WebCrawler {
    private static final String[] urls = {"https://www.motorcitychrysler.ca/used/",
            "https://www.carpages.ca/ontario/windsor/used-cars/",
            "https://www.nawabmotors.ca/cars"};

    public void main(String[] args) {
        CrawlWebsites(urls);
    }

    public static List<String> CrawlWebsites(String[] urls) {
        List<String> filePaths = new ArrayList<>();
        for (String url : urls) {
            filePaths.add(fetchAndSaveHtml(url));
        }
        return filePaths;
    }

    private static String fetchAndSaveHtml(String url) {
        String folderPath = Utils.htmlCacheFolder;

        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");
        String fileName = STR."Cache_\{webSiteName}.txt";

        if (new File(folderPath + fileName).exists()) {
            CustomPrint.println("Crawler", STR."File Cache Exists :: \{fileName}");
            return folderPath + fileName;
        } else {
            try {
                Document document = Jsoup.connect(url).get();
                String htmlCode = document.html();

                File webPageFolder = new File(folderPath);

                if (!webPageFolder.exists()) {
                    Files.createDirectories(Paths.get(folderPath));
                }

                if (webPageFolder.exists() && webPageFolder.isDirectory()) {
                    try (FileWriter writer = new FileWriter(folderPath + fileName)) {
                        writer.write(htmlCode);
                    }
                    CustomPrint.println("Crawler", STR."Crawled File Saved: \{fileName}");
                    return folderPath + fileName;
                }
                else{
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
