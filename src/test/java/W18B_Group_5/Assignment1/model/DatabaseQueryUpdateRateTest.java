package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQueryUpdateRateTest extends DatabaseTest{

    @Test
    void testSameFromTo() {
        String cur = "AUD";

        assertThrows(
                InvalidInputException.class,
                () -> databaseQuery.updateRate(cur, cur, 1)
        );
    }

    @Test
    void testRateAddedAlready() {
        String from = "USD";
        String to = "AUD";

        try {
            databaseQuery.updateRate(from, to, 1);
        } catch (DailyRateAddedException | InvalidInputException | CurrencyDoesntExistException e) {
            fail("Valid updateRate causes an exception");
        }

        assertThrows(
                DailyRateAddedException.class,
                () -> databaseQuery.updateRate(from, to, 1)
        );
    }

    @Test
    void testFromDoesntExist() {
        String from = "test";
        String to = "AUD";

        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.updateRate(from, to, 1)
        );
    }

    @Test
    void testToDoesntExist() {
        String from = "USD";
        String to = "test";

        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.updateRate(from, to, 1)
        );
    }
}
