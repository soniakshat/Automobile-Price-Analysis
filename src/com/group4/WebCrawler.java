package com.group4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Class to crawl web
 *
 * @author Haseeb Shams
 */
public class WebCrawler {
    private static final String folderPath = ".\\res\\generated\\pages\\";
    private static String[] urls = {"https://www.motorcitychrysler.ca/used/",
            "https://www.carpages.ca/ontario/windsor/used-cars/",
            "https://www.autotrader.ca/cars/on/windsor/"};

    public void main(String[] args) {
		CrawlWebsites(urls);
    }

	public static void CrawlWebsites(String[] urls){
		for (String url : urls) {
			fetchAndSaveHtml(url);
		}
	}

    private static void fetchAndSaveHtml(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            String htmlCode = document.html();

            File webPageFolder = new File(folderPath);

            if (!webPageFolder.exists()) {
                Files.createDirectories(Paths.get(folderPath));
            }

            if (webPageFolder.exists() && webPageFolder.isDirectory()) {

                String webSiteName = url.replaceAll(".*www\\.(.*?)\\..*", "$1");

                String fileName = "Cache_" + webSiteName + ".txt";
                try (FileWriter writer = new FileWriter(folderPath + fileName)) {
                    writer.write(htmlCode);
                }
                CustomPrint.println("Crawler", "Crawled File Saved: " + fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
