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
    static final String htmlCacheFolderPath = Utils.htmlCacheFolder;

    public void main(String[] args) {
        CrawlWebsites();
    }

    public static List<String> CrawlWebsites() {
        List<String> filePaths = new ArrayList<>();
        for (String url : Utils.CrawlUrls) {
            filePaths.add(fetchAndSaveHtml(url));
        }
        return filePaths;
    }

    private static String fetchAndSaveHtml(String url) {
        String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

        String htmlFileName = STR."Cache_\{webSiteName}.html";

        String htmlFilePath = htmlCacheFolderPath + htmlFileName;


        if (new File(htmlCacheFolderPath + htmlFileName).exists()) {
            CustomPrint.println("Crawler", STR."File Cache Exists :: \{htmlFileName}");
            return htmlCacheFolderPath + htmlFileName;
        } else {
            try {
                CustomPrint.println("Crawling", url);
                Document document = Jsoup.connect(url).get();
                String htmlCode = document.html();

                File webPageFolder = new File(htmlCacheFolderPath);

                if (!webPageFolder.exists()) {
                    Files.createDirectories(Paths.get(htmlCacheFolderPath));
                }

                if (webPageFolder.exists() && webPageFolder.isDirectory()) {
                    try (FileWriter writer = new FileWriter(htmlCacheFolderPath + htmlFileName)) {
                        writer.write(htmlCode);
                    }
                    CustomPrint.println("Crawler", STR."Crawled File Saved: \{htmlFileName}");
                    return htmlCacheFolderPath + htmlFileName;
                } else {
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
