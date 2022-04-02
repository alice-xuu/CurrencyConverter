package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQueryGetCurrenciesTest extends DatabaseTest{
    @Test
    void testGetInitialCurrencies() {
        List<String> expected = Arrays.asList("AUD", "USD", "EURO", "INR", "RMB", "IDR");

        List<String> actual = databaseQuery.getCurrencies();

        // Sort lists since we dont care about order
        Collections.sort(expected);
        Collections.sort(actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testGetAddedCurrency() {
        String newCur = "test";
        List<String> expected = Arrays.asList("AUD", "USD", "EURO", "INR", "RMB", "IDR", newCur);

        try {
            databaseQuery.addCurrency(newCur);

        } catch (CurrencyAlreadyExistsException | InvalidInputException e) {
            e.printStackTrace();
            fail("Could not add new currency");
        }

        List<String> actual = databaseQuery.getCurrencies();

        // Sort lists since we dont care about order
        Collections.sort(expected);
        Collections.sort(actual);

        assertIterableEquals(expected, actual);
    }

    /*
     * Check that a currency that causes InvalidInputException
     * is not recognised by getCurrency
     */
    @Test
    void testGetEmptyCurrency() {
        String newCur = "";
        List<String> expected = Arrays.asList("AUD", "USD", "EURO", "INR", "RMB", "IDR");

        assertThrows(
                InvalidInputException.class,
                () -> databaseQuery.addCurrency(newCur)
        );

        List<String> actual = databaseQuery.getCurrencies();

        // Sort lists since we dont care about order
        Collections.sort(expected);
        Collections.sort(actual);

        assertIterableEquals(expected, actual);
    }

    /*
     * Check that a currency that causes CurrencyAlreadyExistsException
     * is not recognised by getCurrency
     */
    @Test
    void testGetRepeatCurrency() {
        String newCur = "AUD";
        List<String> expected = Arrays.asList("AUD", "USD", "EURO", "INR", "RMB", "IDR");

        assertThrows(
                CurrencyAlreadyExistsException.class,
                () -> databaseQuery.addCurrency(newCur)
        );

        List<String> actual = databaseQuery.getCurrencies();

        // Sort lists since we dont care about order
        Collections.sort(expected);
        Collections.sort(actual);

        assertIterableEquals(expected, actual);
    }
}
