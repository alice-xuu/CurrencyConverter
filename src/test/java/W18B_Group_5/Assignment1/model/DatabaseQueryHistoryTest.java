package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQueryHistoryTest extends DatabaseTest {
    @Test
    void testRateToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDateString = dtf.format(now);
        try {
            List<String> result = databaseQuery.history("AUD", "USD",currentDateString, currentDateString);
            assertEquals(1, result.size());
            assertEquals(String.format("%s AUD USD 1.7", currentDateString), result.get(0));
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (DateOutOfRangeException e) {
            fail("Valid date is seen as out of range");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }

    @Test
    void testRateFromPast() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDateString = dtf.format(now);
        try {
            List<String> result = databaseQuery.history("AUD", "USD", "2000-12-1", currentDateString);
            assertEquals(1, result.size());
            assertEquals(String.format("%s AUD USD 1.7", currentDateString), result.get(0));
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (DateOutOfRangeException e) {
            fail("Valid date is seen as out of range");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }

    @Test
    void testEndBeforeStart() {
        assertThrows(
                DateOutOfRangeException.class,
                () -> databaseQuery.history("AUD", "USD", "2000-12-15", "1999-12-15")
        );
    }

    @Test
    void testInvalidMonth() {
        assertThrows(
                DateOutOfRangeException.class,
                () -> databaseQuery.history("AUD", "USD", "2000-12-15", "2000-13-15")
        );
    }

    @Test
    void testInvalidDay() {
        assertThrows(
                DateOutOfRangeException.class,
                () -> databaseQuery.history("AUD", "USD", "2000-12-15", "2001-1-40")
        );
    }

    @Test
    void NullFromCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.history(null, "USD", "2000-12-15", "2001-1-12")
        );
    }

    @Test
    void NullToCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.history("AUD", null, "2000-12-15", "2001-1-12")
        );
    }

    @Test
    void testSameFromTo() {
        String cur = "AUD";

        assertThrows(
                InvalidInputException.class,
                () -> databaseQuery.history(cur, cur, getCurrentDate(), getCurrentDate())
        );
    }
    @Test
    void testFromDoesntExist() {
        String from = "test";
        String to = "AUD";

        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.history(from, to, getCurrentDate(), getCurrentDate())
        );
    }

    @Test
    void testToDoesntExist() {
        String from = "USD";
        String to = "test";

        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.history(from, to, getCurrentDate(), getCurrentDate())
        );
    }

    @Test
    void testAddedRateToday() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDateString = dtf.format(now);
        String to = "USD";
        String from = "AUD";

        List<String> expectedOutputs = new ArrayList<>();
        expectedOutputs.add(String.format("%s AUD USD 1.7", currentDateString));
        expectedOutputs.add(String.format("%s AUD USD 0.5", currentDateString));

        try {
            databaseQuery.updateRate(from, to, 0.5);
            List<String> result = databaseQuery.history(from, to, currentDateString, currentDateString);
            assertEquals(expectedOutputs.size(), result.size());
            for (String line: result) {
                assertTrue(expectedOutputs.contains(line));
                expectedOutputs.remove(line);
            }
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (DateOutOfRangeException e) {
            fail("Valid date is seen as out of range");
        } catch(DailyRateAddedException e){
            fail("today's rate has already been added");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }
}
