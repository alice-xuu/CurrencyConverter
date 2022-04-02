package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQuerySummaryTest extends DatabaseTest {
    @Test
    void testSummaryToday() {
        String currentDateString = getCurrentDate();
        List<String> expectedOutputs = Arrays.asList("2.60", "2.60", "0.00", "2.60", "2.60");

        assertSummaryEquals(expectedOutputs, "INR", "IDR", currentDateString, currentDateString);

    }

    @Test
    void testSummaryFromPast() {
        String currentDateString = getCurrentDate();
        List<String> expectedOutputs = Arrays.asList("2.60", "2.60", "0.00", "2.60", "2.60");

        assertSummaryEquals(expectedOutputs, "INR", "IDR", "2001-10-10", currentDateString);
    }

    @Test
    void testEndBeforeStart() {
        assertThrows(
                DateOutOfRangeException.class,
                () -> databaseQuery.summary("AUD", "USD", "2000-12-15", "1999-12-15")
        );
    }

    @Test
    void testInvalidMonth() {
        assertThrows(
                DateOutOfRangeException.class,
                () -> databaseQuery.summary("AUD", "USD", "2000-12-15", "2000-13-15")
        );
    }

    @Test
    void testInvalidDay() {
        assertThrows(
                DateOutOfRangeException.class,
                () -> databaseQuery.summary("AUD", "USD", "2000-12-15", "2001-1-40")
        );
    }

    @Test
    void testSameFromTo() {
        String cur = "AUD";

        assertThrows(
                InvalidInputException.class,
                () -> databaseQuery.summary(cur, cur, getCurrentDate(), getCurrentDate())
        );
    }
    @Test
    void testFromDoesntExist() {
        String from = "test";
        String to = "AUD";

        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.summary(from, to, getCurrentDate(), getCurrentDate())
        );
    }

    @Test
    void testToDoesntExist() {
        String from = "USD";
        String to = "test";

        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.summary(from, to, getCurrentDate(), getCurrentDate())
        );
    }

    @Test
    void NullFromCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.summary(null, "USD", "2000-12-15", "2001-1-12")
        );
    }

    @Test
    void NullToCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.summary("AUD", null, "2000-12-15", "2001-1-12")
        );
    }

    @Test
    void testAddedRateToday() {
        String currentDateString = getCurrentDate();
        List<String> expectedOutputs = Arrays.asList("2.05", "2.05", "0.78", "1.50", "2.60");
        String to = "IDR";
        String from = "INR";

        updateRateWrapper(from, to, 1.5);
        assertSummaryEquals(expectedOutputs, from, to, currentDateString, currentDateString);
    }


}
