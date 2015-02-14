import junit.framework.Test;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Oleksii on 2/14/2015.
 */
public class SearchPage {
    private String url = "http://booking.uz.gov.ua/en/";
    private String searchXPath = "//button[contains(text(), 'Search for trains')]";
    private String popupXPath = "//div[contains(@class,'vToolsPopup') and center]";

    public void open() {
        TestHelper.browser.get(url);
    }

    public WebElement from() {
        return getField("From");
    }

    private WebElement getField(String label) {
        return TestHelper.findElement("//div[text()='" + label + "']/../input");
    }

    public WebElement to() {
        return getField("To");
    }

    public WebElement departureDate() {
        return TestHelper.findElement("//label[contains(., 'Departure date')]/input");
    }

    public WebElement departureTime() {
        return TestHelper.findElement("//label[contains(., 'Departure time from')]/select");
    }

    public WebElement roundTrip() {
        return TestHelper.findElement("//label[contains(., 'Round trip')]/input");
    }

    public WebElement search() {
        return TestHelper.findElement(searchXPath);
    }

    public void typeFrom(String value) {
        from().clear();
        from().sendKeys(value);
    }

    private List<String> getDropDown(String label) {
        List<WebElement> divs = TestHelper.findElements("//div[text()='" + label + "']/../div/div");
        List<String> result = new ArrayList<String>();
        for (WebElement div : divs) {
            result.add(div.getAttribute("title"));
        }
        return result;
    }

    public List<String> getFromDropDown() {
        return getDropDown("From");
    }

    public List<String> getToDropDown() {
        return getDropDown("To");
    }

    public void typeTo(String value) {
        to().clear();
        to().sendKeys(value);
    }

    public String getDepartureDate() {
        return departureDate().getAttribute("value");
    }

    public void setDepartureDate(GregorianCalendar date) {
        String monthYear = new SimpleDateFormat("MMMM yyyy").format(date.getTime());
        String day = new SimpleDateFormat("d").format(date.getTime());

        departureDate().click();
        TestHelper.findElement("//caption[text()='"+monthYear+"']/..//td[text()='"+day+"']").click();
    }

    public GregorianCalendar getSearchButtonDate() {
        String date = TestHelper.findElement(searchXPath + "/span").getText();
        try {
            GregorianCalendar gregDate = new GregorianCalendar();
            gregDate.setTime(new SimpleDateFormat("MM.dd.yyyy").parse(date));
            return gregDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clickSearch() {
        search().click();
    }

    public ErrorMessage getErrorMessage() {
        String title = TestHelper.findElement(popupXPath + "//span").getText();
        List<WebElement> messages = TestHelper.findElements(popupXPath + "//p");
        List<String> resultMessages = new ArrayList<String>();
        for (WebElement element : messages) {
            resultMessages.add(element.getText());
        }
        return new ErrorMessage(title, resultMessages, popupXPath + "/center/button");
    }

    public void assertErrorMessageNotDisplayed() {
        TestHelper.waitElementAbsent(popupXPath);
    }

    public class ErrorMessage {
        public String title;
        public List<String> messages;
        private String okXPath;
        public ErrorMessage(String title, List<String> messages, String okXPath) {
            this.title = title;
            this.messages = messages;
            this.okXPath = okXPath;
        }
        public void clickOk(){
            TestHelper.findElement(okXPath).click();
        }
    }
}
