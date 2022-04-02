
//documentation in https://github.com/xerial/sqlite-jdbc

/*
Compile with:
    javac Database

Run with:
    java -classpath ".:sqlite-jdbc-3.32.3.2.jar" DatabaseImp

*/

package W18B_Group_5.Assignment1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSetup {

    /*--------------------------------------
    Instantiate Connection and Statement
    --------------------------------------*/
    private Connection connection;
    private Statement statement;

/*----------------------------------------------------------------------------
|XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|
----------------------------------------------------------------------------*/

    /*---------------------------------------------------
    insertPopular

    inserts the currency we have chosen as one of the
    4 most popular ones into Popular table.

    Order of inserting determines rank

    First inserted -> highest rank
    ----------------------------------------------------*/
    private void insertPopular(String currency) throws SQLException {
        String insertStmt = String.format("INSERT INTO Popular (currency) VALUES ('%s')", currency);

        statement.executeUpdate(insertStmt);
    }

    /*---------------------------------------------------
    insertCurrency

    admin inserts new currency into table that contains
    all the distinct currencies in the application on
    date
    ----------------------------------------------------*/
    private void insertCurrency(String currency) throws SQLException {
        String insertStmt = String.format("INSERT INTO Currency VALUES (DATE('now','localtime'),'%s')", currency);

        statement.executeUpdate(insertStmt);
    }

    /*---------------------------------------------------
    insertRates

    inserts new rate between two currencies
    on current date
    ----------------------------------------------------*/
    private void insertRates(String from_cur, String to_cur, double rate) throws SQLException {
        String insertStmt = String.format("INSERT INTO Rates (date, from_cur, to_cur, rate, is_addcurrency, is_initial) VALUES (DATE('now','localtime'),'%s','%s',%f,0,1)", from_cur, to_cur, rate);

        statement.executeUpdate(insertStmt);
    }

/*----------------------------------------------------------------------------
|XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|
----------------------------------------------------------------------------*/

    public DatabaseSetup(Connection connection){
        try{

            //create database connection with db file
            this.connection = connection;
            // System.out.println("connected");

            //used to execute queries
            statement = this.connection.createStatement();

            /*---------------------------------------------------
            set query timeout to 30 seconds in case of any
            queries that take too long to execute
            ---------------------------------------------------*/
            statement.setQueryTimeout(30);

/*----------------------------------------------------------------------------*/


            /*---------------------------------------------------
            Currency Table

            contains distinct currencies in the application
            with its corresponding symbols
            ---------------------------------------------------*/
            statement.executeUpdate("DROP TABLE IF EXISTS Currency");

            statement.executeUpdate("CREATE TABLE Currency (date DATE,currency STRING PRIMARY KEY)");

            //insert initial 6 currencies
            insertCurrency("AUD");
            insertCurrency("USD");
            insertCurrency("EURO");
            insertCurrency("INR");
            insertCurrency("RMB");
            insertCurrency("IDR");

            //list that contains attribute names of Currency Table
            List<String> currencyArgs = new ArrayList<>();
            currencyArgs.add("date");
            currencyArgs.add("currency");

            //the following method call is equivalent to
            //SELECT * FROM Currency;
            // seeTable("Currency", currencyArgs);


/*----------------------------------------------------------------------------*/


            /*---------------------------------------------------
            Popular Table

            contains rank with its corresponding currencies
            ---------------------------------------------------*/
            statement.executeUpdate("DROP TABLE IF EXISTS Popular");

            statement.executeUpdate("CREATE TABLE Popular (rank INTEGER PRIMARY KEY AUTOINCREMENT, currency STRING)");

            //insert default top 4 most popular currencies
            insertPopular("AUD");
            insertPopular("INR");
            insertPopular("RMB");
            insertPopular("IDR");

            //list that contains attribute names of Currency Table
            List<String> popularArgs = new ArrayList<>();
            popularArgs.add("rank");
            popularArgs.add("currency");

            //the following method call is equivalent to
            //SELECT * FROM Popular;
            // seeTable("Popular", popularArgs);


/*----------------------------------------------------------------------------*/


            /*---------------------------------------------------
            Rates Table

            contains rates between each currency with all other
            currencies and the date when rate between two currencies
            was added.
            ---------------------------------------------------*/

            statement.executeUpdate("DROP TABLE IF EXISTS Rates");

            //is_addcurrency/is_initial: SQLite doesn't have BOOLEAN so is INTEGER - 0 = false and 1 = true
            statement.executeUpdate("CREATE TABLE Rates (insert_order INTEGER PRIMARY KEY AUTOINCREMENT, date DATE,from_cur STRING,to_cur STRING,rate NUMERIC, is_addcurrency INTEGER, is_initial INTEGER)");

            //insert rates of initial 6 currencies
            insertRates("AUD", "USD", 1.7);
            insertRates("AUD", "EURO", 1.25);
            insertRates("AUD", "INR", 1.25);
            insertRates("AUD", "RMB", 1.25);
            insertRates("AUD", "IDR", 1.25);

            insertRates("USD", "AUD", 0.588235);
            insertRates("USD", "EURO", 1.5);
            insertRates("USD", "INR", 5);
            insertRates("USD", "RMB", 5);
            insertRates("USD", "IDR", 5);

            insertRates("EURO", "AUD", 0.666667);
            insertRates("EURO", "USD", 0.666667);
            insertRates("EURO", "INR", 5.5);
            insertRates("EURO", "RMB", 0.666667);
            insertRates("EURO", "IDR", 0.666667);

            insertRates("INR", "AUD", 2.6);
            insertRates("INR", "USD", 2.6);
            insertRates("INR", "EURO", 0.181818);
            insertRates("INR", "RMB", 0.181818);
            insertRates("INR", "IDR", 2.6);

            insertRates("RMB", "AUD", 0.8);
            insertRates("RMB", "USD", 0.8);
            insertRates("RMB", "EURO", 0.8);
            insertRates("RMB", "INR", 0.8);
            insertRates("RMB", "IDR", 0.8);

            insertRates("IDR", "AUD", 0.2);
            insertRates("IDR", "USD", 0.2);
            insertRates("IDR", "EURO", 0.2);
            insertRates("IDR", "INR", 0.384615);
            insertRates("IDR", "RMB", 0.2);



            //list that contains attribute names of Rates Table
            List<String> rateArgs = new ArrayList<>();
            rateArgs.add("insert_order");
            rateArgs.add("date");
            rateArgs.add("from_cur");
            rateArgs.add("to_cur");
            rateArgs.add("rate");

            //the following method call is equivalent to
            //SELECT * FROM Rates;
            // seeTable("Rates", rateArgs);


/*----------------------------------------------------------------------------*/

        }
        catch(SQLException e){

            //if the error message is "out of memory"
            //it probably means no database file is found
            e.printStackTrace();
        }
    }

}
