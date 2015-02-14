import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Oleksii on 2/14/2015.
 */
@RunWith(JUnit4.class)
public class SearchTests {
    @Before
    public void setup(){
        TestHelper.init();
        Pages.searchPage.open();
    }

    @Test
    public void baseElementsPresence(){
        Assert.assertTrue(Pages.searchPage.from().isDisplayed());
        Assert.assertTrue(Pages.searchPage.to().isDisplayed());
        Assert.assertTrue(Pages.searchPage.departureDate().isDisplayed());
        Assert.assertTrue(Pages.searchPage.departureTime().isDisplayed());
        Assert.assertTrue(Pages.searchPage.roundTrip().isDisplayed());
        Assert.assertTrue(Pages.searchPage.search().isDisplayed());
    }

    @Test
    public void typeKyInFromFieldDropDownCorrect(){
        String[] expectedResults = {
            "Kyaniv Pereval",
            "Kyanivka",
            "Kybyntsi",
            "Kychyranky",
            "Kyiv",
            "Kyivska Rusanivka",
            "Kyj",
            "Kykshor",
            "Kyn",
            "Kynnu"
        };

        Pages.searchPage.typeFrom("Ky");
        List<String> results = Pages.searchPage.getFromDropDown();
        Assert.assertTrue(TestHelper.matchArrayList(expectedResults, results));
    }

    @Test
    public void typeIvanoInToFieldDropDownCorrect(){
        String[] expectedResults = {
                "Ivano-Frankivsk",
                "Ivano-Kopyne",
                "Ivanovka",
                "Ivanovka",
                "Ivanovka",
                "Ivanovo",
                "Ivanovo S",
                "Ivanovskaia",
                "Ivanovskii"
        };

        Pages.searchPage.typeTo("Ivano");
        List<String> results = Pages.searchPage.getToDropDown();
        Assert.assertTrue(TestHelper.matchArrayList(expectedResults, results));
    }

    @Test
    public void departureDateContainsTodayDate() {
        String today = new SimpleDateFormat("MM.dd.yyyy").format(new Date());
        String departureDate = Pages.searchPage.getDepartureDate();

        Assert.assertEquals(today, departureDate);
    }

    @Test
    public void changeDepartureDateSearchButtonContainsThisDate(){
        GregorianCalendar date = new GregorianCalendar(2015, 3, 8);
        Pages.searchPage.setDepartureDate(date);
        GregorianCalendar resultDate = Pages.searchPage.getSearchButtonDate();

        Assert.assertTrue(date.equals(resultDate));
    }

    @Test
    public void fromKyToIvanoSearchErrorMessageCorrect() {
        Pages.searchPage.typeFrom("Ky");
        Pages.searchPage.typeTo("Ivano");
        Pages.searchPage.clickSearch();
        SearchPage.ErrorMessage error = Pages.searchPage.getErrorMessage();
        Assert.assertEquals("Error filling in the form", error.title);
        Assert.assertEquals("Select a departure point from the drop down list", error.messages.get(0));
        Assert.assertEquals("Select your destination from the drop down list", error.messages.get(1));
        error.clickOk();
        Pages.searchPage.assertErrorMessageNotDisplayed();
    }

    @Test
    public void emptySearchErrorMessageCorrect() {
        Pages.searchPage.clickSearch();
        SearchPage.ErrorMessage error = Pages.searchPage.getErrorMessage();
        Assert.assertEquals("Error filling in the form", error.title);
        Assert.assertEquals("Enter a departure station", error.messages.get(0));
        Assert.assertEquals("Enter a destination station", error.messages.get(1));
        error.clickOk();
        Pages.searchPage.assertErrorMessageNotDisplayed();
    }

    @After
    public void cleanup(){
        TestHelper.browser.quit();
    }
}
