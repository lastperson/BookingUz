import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

/**
 * Created by Oleksii on 2/14/2015.
 */
public class TestHelper {
    public static WebDriver browser;

    public static void init() {
        System.setProperty("webdriver.chrome.driver", "C:/JavaLibs/chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().window().maximize();
    }

    private static void waitElement(String xPath) {
        int timeout = 30000;
        int sleep = 100;
        while (browser.findElements(By.xpath(xPath)).size() == 0 && timeout > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout -= sleep;
        }
        if (timeout <= 0) {
            Assert.fail("Element with xpath: " + xPath + " was not found in " + timeout + " msec.");
        }
    }

    public static void waitElementAbsent(String xPath) {
        int timeout = 30000;
        int sleep = 100;
        while (browser.findElements(By.xpath(xPath)).size() > 0 && timeout > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout -= sleep;
        }
        if (timeout <= 0) {
            Assert.fail("Element with xpath: " + xPath + " still on page in " + timeout + " msec.");
        }
    }

    public static WebElement findElement(String xPath) {
        waitElement(xPath);
        return browser.findElement(By.xpath(xPath));
    }

    public static List<WebElement> findElements(String xPath) {
        waitElement(xPath);
        return browser.findElements(By.xpath(xPath));
    }

    public static boolean matchArrayList(String[] expectedResults, List<String> results) {
        if (expectedResults.length != results.size()) {
            return false;
        }

        for (int i = 0; i < expectedResults.length; i++) {
            if (!expectedResults[i].equals(results.get(i))) {
                return false;
            }
        }
        return true;
    }
}
