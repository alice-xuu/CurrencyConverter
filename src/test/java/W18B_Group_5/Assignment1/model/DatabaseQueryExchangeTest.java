package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseQueryExchangeTest extends DatabaseTest{
    @Test
    void convertAUDToUSDNormal() {
        try {
            String result = databaseQuery.exchange("AUD", "USD", 500);
            double converted = 500*1.7;
            String compare_with = String.format("USD %.2f", converted);
            String message = String.format("meant to output %s, got %s", compare_with, result);
            assertEquals(compare_with, result, message);
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (InvalidAmountSpecifiedException e) {
            fail("Amount specified is invalid, most likely negative value");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }

    @Test
    void SameFromToCurrency() {
        assertThrows(
                InvalidInputException.class,
                () -> databaseQuery.exchange("USD", "USD", 500)
        );
    }

    @Test
    void invalidFromCurrency() {
        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.exchange("pokeCoin", "USD", 500)
        );
    }

    @Test
    void invalidToCurrency() {
        assertThrows(
                CurrencyDoesntExistException.class,
                () -> databaseQuery.exchange("AUD", "Tralala", 100)
        );
    }

    @Test
    void NullFromCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.exchange(null, "USD", 500)
        );
    }

    @Test
    void NullToCurrency() {
        assertThrows(
                NullPointerException.class,
                () -> databaseQuery.exchange("AUD", null, 100)
        );
    }


    @Test
    void invalidAmount() {
        assertThrows(
                InvalidAmountSpecifiedException.class,
                () -> databaseQuery.exchange("AUD", "USD", -100)
        );
    }

    @Test
    void testUpdatedRateBetweenAUDandIDR() {
        String from = "AUD";
        String to = "IDR";

        List<Double> amounts = new ArrayList<>();
        amounts.add(1000.0);
        amounts.add(6323.532);
        amounts.add(100000000000000000000000000.0);
        amounts.add(92335925923.342342342);

        List<String> expectedOutputs = new ArrayList<>();
        expectedOutputs.add(String.format("IDR %.2f", 1.25*1000));
        expectedOutputs.add(String.format("IDR %.2f", 1.25*6323.532));
        expectedOutputs.add(String.format("IDR %.2f", 0.5*100000000000000000000000000.0));
        expectedOutputs.add(String.format("IDR %.2f", 0.5*92335925923.342342342));
        try {
            for(int i = 0; i < amounts.size(); i++){
                if(i == 2){
                    databaseQuery.updateRate(from, to, 0.5);
                }
                String result = databaseQuery.exchange(from, to, amounts.get(i));
                assertEquals(expectedOutputs.get(i), result);
            }
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (InvalidAmountSpecifiedException e) {
            fail("Amount specified is invalid, most likely negative value");
        } catch(DailyRateAddedException e){
            fail("today's rate has already been added");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }

    @Test
    void testnewCurrencyBetweenFAKEandIDR() {
        String from = "AUD";
        String to = "FAKE";

        List<Double> amounts = new ArrayList<>();
        amounts.add(1000.0);
        amounts.add(6323.532);
        amounts.add(100000000000000000000000000.0);
        amounts.add(92335925923.342342342);

        try{
            databaseQuery.addCurrency(to);
        }
        catch (CurrencyAlreadyExistsException e) {
            fail("Currency in setup is not detected");
        } catch (InvalidInputException e) {
            fail("No currency specified");
        }

        List<String> expectedOutputs = new ArrayList<>();
        expectedOutputs.add(String.format("FAKE %.2f", 1.0*1000));
        expectedOutputs.add(String.format("FAKE %.2f", 1.0*6323.532));
        expectedOutputs.add(String.format("FAKE %.2f", 0.5*100000000000000000000000000.0));
        expectedOutputs.add(String.format("FAKE %.2f", 0.5*92335925923.342342342));
        try {
            for(int i = 0; i < amounts.size(); i++){
                if(i == 2){
                    databaseQuery.updateRate(from, to, 0.5);
                }
                String result = databaseQuery.exchange(from, to, amounts.get(i));
                assertEquals(expectedOutputs.get(i), result);
            }
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (InvalidAmountSpecifiedException e) {
            fail("Amount specified is invalid, most likely negative value");
        } catch(DailyRateAddedException e){
            fail("today's rate has already been added");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }
}
