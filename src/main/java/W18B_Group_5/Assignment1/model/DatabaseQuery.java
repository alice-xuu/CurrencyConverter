
package W18B_Group_5.Assignment1.model;

import java.util.List;

public interface DatabaseQuery {

    // Returns a list of all the currencies in the database
    public List<String> getCurrencies();

/*----------------------------------------------------------------------------*/
    // used to update rates between two existing currencies
    /*
     * Update rates between two existing currencies and record date of update
     *
     * Throws CurrencyDoesntExistException if either the
     * "to" or "from" currency doesnt exist
     *
     * Throws DailyRateAddedException if
     * rate has already been updated between 'from' and 'to'
     * currencies today
     *
     * Throws InvalidInputException if
     * the "to" and "from" currencies are the same
     *
     * Throws NullPointerException if
     * currencies were not selected when the button is clicked
     */
    public void updateRate(String from, String to, double rate) throws CurrencyDoesntExistException, DailyRateAddedException, InvalidInputException;

/*----------------------------------------------------------------------------*/
    /*
     * Add a new currency.
     * Initialises the exchange rate to every other currency
     * to 1.
     *
     * Throws CurrencyAlreadyExistsException if the currency
     * already exists
     *
     * Throws InvalidInputException if
     * no currency were specified when the button is clicked
     */
    public void addCurrency(String cur) throws CurrencyAlreadyExistsException, InvalidInputException;

/*----------------------------------------------------------------------------*/
    /*
     * Return a list of the exchange rates between the "to"
     * and "from" currencies between the given start and end
     * dates.
     * Starts from most recent rate.
     *
     * Example: [2020-10-01 IDR AUD 0.4, 2020-09-30 IDR AUD 0.2]
     *
     * Throws DateOutOfRangeException if the start date is after the end date
     *
     * Throws CurrencyDoesntExistException if either "to" or "from" currencies
     * does not exist in the database.
     *
     * Throws InvalidInputException if
     * the "to" and "from" currencies are the same
     *
     * Throws NullPointerException if
     * currencies were not selected when the button is clicked
     */
    public List<String> history(String from, String to, String start, String end) throws DateOutOfRangeException, CurrencyDoesntExistException, InvalidInputException;


/*----------------------------------------------------------------------------*/
    /*
     * Return a list of statistical information of exchange rates
     * between the "to" and "from" currencies between the given
     * start and end dates.
     *
     * (average, median, standard deviation, minimum, maximum)
     * Example: [0.20, 0.20, 0.00, 0.20, 0.20]
     *
     * Throws DateOutOfRangeException if the start date is after the end date
     *
     * Throws CurrencyDoesntExistException if either "to" or "from" currencies
     * does not exist in the database.
     *
     * Throws InvalidInputException if
     * the "to" and "from" currencies are the same
     *
     * Throws NullPointerException if
     * currencies were not selected when the button is clicked
     */
    public List<String> summary(String from, String to, String start, String end) throws DateOutOfRangeException, CurrencyDoesntExistException, InvalidInputException;

/*----------------------------------------------------------------------------*/
    /*
     * Converts an amount of "to" currency to the "from" currency
     * Returns string of "currency" with the converted amount (2 decimal places)
     * Example: "IDR 200.00"
     *
     * Throws CurrencyDoesntExistException if either the
     * "to" or "from" currency doesnt exist
     *
     * Throws InvalidAmountSpecifiedException if the amount specied is negative
     *
     * Throws InvalidInputException if
     * the "to" and "from" currencies are the same
     *
     * Throws NullPointerException if
     * currencies were not selected when the button is clicked
     */
    public String exchange(String from, String to, double amount) throws CurrencyDoesntExistException, InvalidAmountSpecifiedException, InvalidInputException;

/*----------------------------------------------------------------------------*/
    /*
     * Returns a list of the 4 most popular currencies and their exchange rates.
     * The first list contains the names of the currencies. (header)
     * Then the next 4 lists are the exchange rates between the currencies
     * in the same order as they were specified in the header. i.e. the
     * first row of exchange rates contains the rate from the first currency
     * in the header to every other currency in the header
     */
    public List<List<String>> getPopular();

/*----------------------------------------------------------------------------*/
    /*
     * Sets the list of currencies as the popular currencies.
     * The number of currencies has to be 4.
     *
     * Throws CurrencyDoesntExistException any of the currencies
     * in the list don't exist
     *
     * Throws InvalidInputException if
     * the same currencies are chosen for different ranks
     *
     * Throws NullPointerException if
     * currencies were not selected when the button is clicked
     */
    public void setPopular(List<String> currencies) throws CurrencyDoesntExistException, InvalidInputException;

/*----------------------------------------------------------------------------*/

    /* --------------------------------------------------------
    prints out all entries of a table in other words it is like
    SELECT * FROM tableName

    List<String> arguments : list of column/attribute names
    of tableName
    ----------------------------------------------------------*/
    // public void seeTable(String tableName, List<String> arguments);

}
