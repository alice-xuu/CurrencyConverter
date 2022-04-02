package W18B_Group_5.Assignment1.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class DatabaseTest {
    /*
     * Constants
     */
    protected static final String DB_NAME = "database.db";
    protected static final String CURRENCY_TABLE = "Currency";
    protected static final String POPULAR_TABLE = "Popular";
    protected static final String RATES_TABLE = "Rates";
    protected static final String URL = "jdbc:sqlite:" + DB_NAME;

    protected static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /*
     * Test setup
     */
    protected Connection connection;
    protected DatabaseQuery databaseQuery;

    @BeforeEach
    protected void setUp() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Could not connect to database");
            e.printStackTrace();
        }

        new DatabaseSetup(connection);
        databaseQuery = new DatabaseQueryImp(connection);
    }
    @AfterEach
    protected void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not close database");
        }
        File dbFile = new File(DB_NAME);
        dbFile.delete();
    }

    /*
     * Utility test methods
     */
    protected Statement getStatement() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not create database statement");
        }
        return statement;
    }

    /*
     * Executes any sql query and returns result as a string
     */
    protected String executeQuery(String query) {
        final String colSep = "\t\t";
        StringBuilder resultString = new StringBuilder();
        try {
            ResultSet resultSet = getStatement().executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            final int columnCount = resultSetMetaData.getColumnCount();

            for(int i = 1; i <= columnCount; ++i) {
                resultString.append(resultSetMetaData.getColumnName(i));
                resultString.append(colSep);
            }
            resultString.append('\n');

            while(resultSet.next()) {
                for(int i = 1; i <= columnCount; ++i) {
                    resultString.append(resultSet.getString(i));
                    resultString.append(colSep);
                }
                resultString.append('\n');
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not query " + DB_NAME);
        }

        return resultString.toString();
    }

    /*
     * Wrap updateRate in try catch block
     */
    protected void updateRateWrapper(String from, String to, double rate) {
        try {
            databaseQuery.updateRate(from, to, rate);
        } catch (CurrencyDoesntExistException e) {
            fail("Unexpected expection: " + e.getMessage());
        } catch(DailyRateAddedException e){
            fail("today's rate has already been added");
        } catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }
    }

    /*
     * Check that summary is equivalent to expected outputs and
     * wraps call to summary in try catch block
     */
    protected void assertSummaryEquals(List<String> expectedOutputs, String from, String to, String start, String end) {
        try {
            List<String> result = databaseQuery.summary(from, to, start, end);
            assertIterableEquals(expectedOutputs, result);
        } catch (CurrencyDoesntExistException e) {
            fail("Currency in setup is not detected");
        } catch (DateOutOfRangeException e) {
            fail("Valid date is seen as out of range");
        }catch(InvalidInputException e){
            fail("currencies 'to' and 'from' cannot be the same");
        }

    }
}
