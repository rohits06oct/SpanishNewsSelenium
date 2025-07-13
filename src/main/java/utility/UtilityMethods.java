package utility;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilityMethods {

    static {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("RAPIDAPI_KEY", dotenv.get("RAPIDAPI_KEY"));
        System.setProperty("RAPIDAPI_HOST", dotenv.get("RAPIDAPI_HOST"));
        System.setProperty("BrowserStackUN", dotenv.get("BrowserStackUN"));
        System.setProperty("BrowserStackPASS", dotenv.get("BrowserStackPASS"));
    }

    public static void waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void downloadImage(String url, String path) {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(path));
        } catch (Exception e) {
            System.out.println("Image download failed: " + e.getMessage());
        }
    }

    public static Map<String, Integer> replaceWordsCheckDuplicate(List<String> englishTitles){
        Map<String, Integer> wordCounts = new HashMap<>();

        for (String englishTitle : englishTitles) {
            englishTitle = englishTitle.replace(",", " ");
            String[] words = englishTitle.split(" ");

            for (String s : words) {
                String word = s.toLowerCase().trim();
                if (!word.equals("")) {
                    if (wordCounts.containsKey(word)) {
                        wordCounts.put(word, wordCounts.get(word) + 1);
                    } else {
                        wordCounts.put(word, 1);
                    }
                }
            }
        }
        return wordCounts;
    }

}
