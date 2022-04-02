package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQueryGetPopularTest extends DatabaseTest{
    @Test
    void getInitialPopularCurrencies(){

        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("From/To", "AUD", "INR", "RMB", "IDR"));
        expected.add(Arrays.asList("AUD", "-",   "1.25", "1.25", "1.25"));
        expected.add(Arrays.asList("INR", "2.6",  "-",   "0.181818", "2.6"));
        expected.add(Arrays.asList("RMB", "0.8",  "0.8",   "-", "0.8"));
        expected.add(Arrays.asList("IDR", "0.2",  "0.384615", "0.2", "-"));

        List<List<String>> actual = databaseQuery.getPopular();
        assertEquals(5, actual.size());

        assertIterableEquals(expected, actual);
    }

    @Test
    void getModifiedPopularCurrencies(){
        List<String> expected = Arrays.asList("EURO", "RMB", "USD", "IDR");
        try {
            databaseQuery.setPopular(expected);
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }

        List<List<String>> retrieved = databaseQuery.getPopular();
        assertEquals(5, retrieved.size());
        List<String> actual = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            actual.add(retrieved.get(0).get(i+1));
        }
        assertIterableEquals(expected, actual);
    }

    @Test
    void addNewCurrenciesgetModifiedPopularCurrencies(){

        List<String> expected = Arrays.asList("A", "B", "C", "D");


        try {
            for(String currency: expected){
                databaseQuery.addCurrency(currency);
            }
            databaseQuery.setPopular(expected);
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        } catch (CurrencyAlreadyExistsException e) {
            fail("Currency already exists");
        }

        List<List<String>> retrieved = databaseQuery.getPopular();
        assertEquals(5, retrieved.size());
        List<String> actual = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            actual.add(retrieved.get(0).get(i+1));
        }

        assertIterableEquals(expected, actual);
    }

    @Test
    void UpdatedRatesgetModifiedPopularCurrencies(){
        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("From/To", "AUD", "INR", "RMB", "IDR"));
        expected.add(Arrays.asList("AUD", "-",   "10.0↑", "10.0↑", "1.25"));
        expected.add(Arrays.asList("INR", "1.0↓",  "-",   "0.181818", "2.6"));
        expected.add(Arrays.asList("RMB", "0.8",  "0.8",   "-", "0.8"));
        expected.add(Arrays.asList("IDR", "0.2",  "0.384615", "20.0↑", "-"));

        try {
            databaseQuery.updateRate("AUD", "INR", 10);
            databaseQuery.updateRate("AUD", "RMB", 10);
            databaseQuery.updateRate("INR", "AUD", 1);
            databaseQuery.updateRate("IDR", "RMB", 20);
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        } catch(DailyRateAddedException e){
            fail("rate already updated today");
        }

        List<List<String>> actual = databaseQuery.getPopular();
        assertEquals(5, actual.size());
        assertIterableEquals(expected, actual);
    }

}
