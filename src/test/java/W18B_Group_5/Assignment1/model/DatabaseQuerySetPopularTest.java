package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQuerySetPopularTest extends DatabaseTest{

    @Test
    void NoneSelected() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.setPopular(null)
        );
    }

    @Test
    void DuplicateCurrency() {
        List<String> currency = new ArrayList<>();
        currency.add("USD");
        currency.add("USD");
        currency.add("USD");
        currency.add("USD");
        assertThrows(
                InvalidInputException.class,
                () -> databaseQuery.setPopular(currency)
        );
    }


    @Test
    void InvalidCurrency() {
        List<String> currency = new ArrayList<>();
        currency.add("USD");
        currency.add("Yo");
        currency.add("INR");
        currency.add("hello");
        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.setPopular(currency)
        );
    }

    @Test
    void SetPopularNormalFunctionality() {
        List<String> currencies = new ArrayList<>();
        currencies.add("AUD");
        currencies.add("INR");
        currencies.add("RMB");
        currencies.add("IDR");

        try {
            databaseQuery.setPopular(currencies);
            List<List<String>> popular = databaseQuery.getPopular();
            for(int i = 0; i < 4; i++){
                assertEquals(popular.get(0).get(i+1), currencies.get(i));
            }
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }

}
