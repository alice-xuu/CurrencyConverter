package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


class DatabaseSetupTest extends DatabaseTest {

    @Test
    void testCurrenciesTableIsCreated() {
        String select_currencies = "SELECT * FROM " + CURRENCY_TABLE;
        assertNotEquals("", executeQuery(select_currencies));
    }

    @Test
    void testPopularTableIsCreated() {
        String select_currencies = "SELECT * FROM " + POPULAR_TABLE;
        assertNotEquals("", executeQuery(select_currencies));
    }

    @Test
    void testRatesTableIsCreated() {
        String select_currencies = "SELECT * FROM " + RATES_TABLE;
        assertNotEquals("", executeQuery(select_currencies));
    }

}