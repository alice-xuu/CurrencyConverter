package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQueryAddCurrencyTest extends DatabaseTest{
    @Test
    void addNormally() {
        try {
            List<String> toAdd = new ArrayList<>();
            toAdd.add("Bitcoin");
            toAdd.add("MDR");
            toAdd.add("GHJ");
            toAdd.add("ABC");
            toAdd.add("JGK");
            toAdd.add("123");
            toAdd.add("^%$");
            toAdd.add("FAKE");

            for(String currency: toAdd){
                databaseQuery.addCurrency(currency);
            }

            List<String> currencies = databaseQuery.getCurrencies();
            boolean found = false;
            for(String added: toAdd){
                found = false;
                for(String currency: currencies){
                    if(added.equals(currency)){
                        found = true;
                    }
                }
                assertTrue(found, String.format("%s not found in database", added));
            }

        } catch (CurrencyAlreadyExistsException e) {
            fail("Currency already exists");
        } catch (InvalidInputException e) {
            fail("No currency specified");
        }
    }

    @Test
    void NullCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.addCurrency(null)
        );
    }

    @Test
    void ExistingCurrency() {
        assertThrows(
                CurrencyAlreadyExistsException.class,
                () -> databaseQuery.addCurrency("AUD")
        );
    }


}
