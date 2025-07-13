package Opinion;

import TranslateAPI.TranslateAPIClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import utility.UtilityMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpanishNews {

    public WebDriver driver;
    public List<String> titles;
    public List<String> englishTitles;
    public Map<String, Integer> wordCounts;
    String platformName;

    @BeforeClass
    public void openURL() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
//        driver.manage().window().maximize();
        driver.get("https://elpais.com/");
        UtilityMethods.waitForElementVisible(driver, By.xpath("//button[@id='didomi-notice-agree-button']"),10);
        driver.findElement(By.xpath("//button[@id='didomi-notice-agree-button']")).click();
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        platformName = caps.getPlatformName().toString().toLowerCase();
        System.out.println("Platfrom name : "+platformName);
    }

    @Test(priority = 1)
    public void openOpinionArticles() {
        if(platformName.equals("android")) {
            driver.get("https://elpais.com/opinion/");
        } else {
            UtilityMethods.waitForElementVisible(driver, By.xpath("//header[@data-dtm-region]//a[contains(@href,'opinion')]"), 10);
            driver.findElement(By.xpath("//header[@data-dtm-region]//a[contains(@href,'opinion')]")).isDisplayed();
            driver.findElement(By.xpath("//header[@data-dtm-region]//a[contains(@href,'opinion')]")).click();
        }
    }

    @Test(priority = 2)
    public void fetchFiveArticles() {
        UtilityMethods.waitForElementVisible(driver, By.xpath("//h1//a[contains(@data-mrf-link,'opinion')]"),10);
        titles = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String currTitle = driver.findElement(By.xpath("(//article/header/h2/a)["+i+"]")).getText();
            titles.add(currTitle);
            System.out.println("Spanish Title of article number " + i + " : " + currTitle);
            String paragraph = driver.findElement(By.xpath("(//article/p)["+i+"]")).getText();
            System.out.println("Spanish Content of article number " + i + " : " + paragraph);
            try {
                String imgUrl = driver.findElement(By.xpath("//article/header/h2/a[text()='"+currTitle+"']/parent::h2/parent::header/parent::article/figure//img")).getAttribute("src");
                UtilityMethods.downloadImage(imgUrl, "images/image" + i + ".jpg");
            } catch (Exception ignored) {
                System.out.println("Image doesnt exist for current article");
            }
        }
    }

    @Test(dependsOnMethods = {"fetchFiveArticles"})
    public void translateTitleEnglish() {
        englishTitles = new ArrayList<>();
        for(int i = 0; i < titles.size(); i++){
            String englishTitle = TranslateAPIClass.translateAPI(titles.get(i));
            englishTitles.add(englishTitle);
            System.out.println("English Title of article number " + i + " : " + englishTitle);
        }
    }

    @Test(dependsOnMethods = {"translateTitleEnglish"})
    public void analyseWordsPrintMoreThanTwice() {
        wordCounts = UtilityMethods.replaceWordsCheckDuplicate(englishTitles);

        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    @AfterClass
    public void close() {
        driver.quit();
    }

}
