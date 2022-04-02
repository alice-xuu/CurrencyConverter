
package W18B_Group_5.Assignment1.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public class DatabaseQueryImp implements DatabaseQuery {

    /*--------------------------------------
    Instantiate variable to check whether
    user is admin or not and SQL Database
    --------------------------------------*/
    private boolean isAdmin = false;
    private Connection connection;

    public DatabaseQueryImp(Connection connection){
        this.connection = connection;
    }

/*----------------------------------------------------------------------------*/

    public PreparedStatement prepStatement(String sqlText){
        try{
            PreparedStatement ps = connection.prepareStatement(sqlText);
            ps.setQueryTimeout(30);
            return ps;
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

/*----------------------------------------------------------------------------*/

    public List<String> getCurrencies(){
        try{
            List<String> cur_list = new ArrayList<String>();

            //get the list of popular currencies
            String getCur = "SELECT currency FROM Currency";
            PreparedStatement ps1 = prepStatement(getCur);
            ResultSet r1 = ps1.executeQuery();

            while (r1.next()) {
                cur_list.add(r1.getString("currency"));
            }
            return cur_list;
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    //checks if sql query returns anything, true = is empty, false = not empty
    public static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
        return (!rs.isBeforeFirst() && rs.getRow() == 0);
    }

/*----------------------------------------------------------------------------*/

    public void updateRate(String from, String to, double rate) throws CurrencyDoesntExistException, DailyRateAddedException, InvalidInputException{
        try{
            //check if from and to are different
            if (from.equals(to) || to.equals(from)){
                throw new InvalidInputException("Currencies 'to' and 'from' can't be the same");
            }


            //check if rate has been added today already for these two currencies
            String checkDate = "SELECT * FROM rates WHERE date = ? AND from_cur = ? AND to_cur = ? AND is_addcurrency = 0 AND is_initial = 0";
            PreparedStatement ps = prepStatement(checkDate);
            ps.setObject(1, LocalDate.now());
            ps.setString(2, from);
            ps.setString(3, to);

            ResultSet r = ps.executeQuery();
            if(!isResultSetEmpty(r)){
                throw new DailyRateAddedException("Today's rate has been added already");
            }

            //check if both input currencies exist already in 'currency'
            boolean fromNotExists;
            boolean toNotExists;

            ResultSet resultSet;
            String checkExists = "SELECT * FROM Currency WHERE currency IN (?)";

            PreparedStatement ps1 = prepStatement(checkExists);
            ps1.setString(1, from);
            resultSet = ps1.executeQuery();
            fromNotExists = isResultSetEmpty(resultSet);

            ps1.setString(1, to);
            resultSet = ps1.executeQuery();
            toNotExists = isResultSetEmpty(resultSet);

            if(fromNotExists || toNotExists){
                throw new CurrencyDoesntExistException("Currency doesn't exist");
            }

            //add new rate into 'rates'
            String SQL = "INSERT INTO Rates (date, from_cur, to_cur, rate, is_addcurrency, is_initial) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps2 = prepStatement(SQL);

            ps2.setObject(1, LocalDate.now());
            ps2.setString(2, from);
            ps2.setString(3, to);
            ps2.setDouble(4, rate);
            ps2.setInt(5, 0);
            ps2.setInt(6, 0);

            ps2.executeUpdate();
        }


        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

/*----------------------------------------------------------------------------*/

    public void addCurrency(String cur) throws CurrencyAlreadyExistsException, InvalidInputException{

        try{

            if(cur.length() == 0){
                throw new InvalidInputException("Please specify currency");
            }

            //check if cur exists already in 'currencies'
            String checkCur = "SELECT * FROM Currency WHERE currency IN (?)";
            PreparedStatement ps1 = prepStatement(checkCur);
            ps1.setObject(1, cur);
            ResultSet r1 = ps1.executeQuery();
            if(!isResultSetEmpty(r1)){
                throw new CurrencyAlreadyExistsException("Currency already exists");
            }

            //add new currency into 'currency'
            String SQL = "INSERT INTO Currency VALUES (?, ?)";
            PreparedStatement ps3 = prepStatement(SQL);
            ps3.setObject(1, LocalDate.now());
            ps3.setString(2, cur);
            ps3.executeUpdate();


            //add all the rates for the new currency to 'rates'
            String getCur = "SELECT currency FROM Currency";
            PreparedStatement ps2 = prepStatement(getCur);
            ResultSet r2 = ps2.executeQuery();

            String insertRates = "INSERT INTO Rates (date, from_cur, to_cur, rate, is_addcurrency, is_initial) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = prepStatement(insertRates);
            while (r2.next()) {
                String existingCur = r2.getString("currency");

                if (existingCur.equals(cur)){
                    continue;
                }

                ps.setObject(1, LocalDate.now());
                ps.setString(2, existingCur);
                ps.setString(3, cur);
                int rate = 1;
                ps.setDouble(4, rate);
                ps.setInt(5, 1);
                ps.setInt(6, 0);

                ps.executeUpdate();

                ps.setString(2, cur);
                ps.setString(3, existingCur);
                ps.executeUpdate();

            }

        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }

    }

/*----------------------------------------------------------------------------*/

    @Override
    public List<String> history(String from, String to, String start, String end) throws DateOutOfRangeException, CurrencyDoesntExistException, InvalidInputException{
        try{

            if (from.equals(to) || to.equals(from)){
                throw new InvalidInputException("Currencies 'to' and 'from' can't be the same");
            }

            //check if "to" and "from" currency exists in the database
            String checkExists = "SELECT * FROM Currency WHERE currency = ?";
            PreparedStatement ps1 = prepStatement(checkExists);
            ps1.setString(1, from);
            ResultSet r1 = ps1.executeQuery();
            PreparedStatement ps0 = prepStatement(checkExists);
            ps0.setString(1, to);
            ResultSet r2 = ps0.executeQuery();

            if(isResultSetEmpty(r1) || isResultSetEmpty(r2)){
                throw new CurrencyDoesntExistException("Currency doesn't exist");
            }

            //check that start date is not later than end date
            String compareDate = "SELECT 1 WHERE ? <= ?";
            PreparedStatement ps2 = prepStatement(compareDate);
            ps2.setString(1, start);
            ps2.setString(2, end);
            r1 = ps2.executeQuery();
            if(isResultSetEmpty(r1)){
                throw new DateOutOfRangeException("start date cannot be later end date");
            }

            //check that end date is not later than today
            String checkDate = "SELECT 1 FROM Rates WHERE ? <= DATE('now','localtime') AND ? >= (SELECT MIN(date) FROM Rates)";
            PreparedStatement ps3 = prepStatement(checkDate);
            ps3.setString(1, end);
            ps3.setString(2, end);
            r1 = ps3.executeQuery();

            if(isResultSetEmpty(r1)){
                throw new DateOutOfRangeException("end date is not a valid date");
            }


            //get list of all exchange rates between to currency and from currency
            //within specified dates
            String SQL = "SELECT date, from_cur, to_cur, rate FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ? ORDER BY date DESC";
            PreparedStatement ps4 = prepStatement(SQL);
            ps4.setString(1, start);
            ps4.setString(2, end);
            ps4.setString(3, from);
            ps4.setString(4, to);

            ResultSet rs = ps4.executeQuery();

            //instantiate the list of Strings that we will return
            List<String> to_return = new ArrayList<String>();

            //add rows of exchange history into list
            while(rs.next()){
                String row = rs.getString("date") + " " + rs.getString("from_cur") + " " + rs.getString("to_cur") + " " + rs.getDouble("rate");
                to_return.add(row);
            }


            //return list of Strings
            return to_return;
        }

        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    /*----------------------------------------------------------------------------*/

        @Override
        public List<String> summary(String from, String to, String start, String end) throws DateOutOfRangeException, CurrencyDoesntExistException, InvalidInputException{
            try{
                if (from.equals(to) || to.equals(from)){
                    throw new InvalidInputException("Currencies 'to' and 'from' can't be the same");
                }

                //check if "to" and "from" currency exists in the database
                String checkExists = "SELECT * FROM Currency WHERE currency = ?";
                PreparedStatement ps1 = prepStatement(checkExists);
                ps1.setString(1, from);
                ResultSet r1 = ps1.executeQuery();
                PreparedStatement ps0 = prepStatement(checkExists);
                ps0.setString(1, to);
                ResultSet r0 = ps0.executeQuery();

                if(isResultSetEmpty(r1) || isResultSetEmpty(r0)){
                    throw new CurrencyDoesntExistException("Currency doesn't exist");
                }

                //check that start date is not later than end date
                String compareDate = "SELECT 1 WHERE ? <= ?";
                PreparedStatement ps2 = prepStatement(compareDate);
                ps2.setString(1, start);
                ps2.setString(2, end);
                ResultSet r2 = ps2.executeQuery();
                if(isResultSetEmpty(r2)){
                    throw new DateOutOfRangeException("start date cannot be later end date");
                }

                //check that end date is not later than today
                String checkDate = "SELECT 1 FROM Rates WHERE ? <= DATE('now','localtime') AND ? >= (SELECT MIN(date) FROM Rates)";
                PreparedStatement ps3 = prepStatement(checkDate);
                ps3.setString(1, end);
                ps3.setString(2, end);
                ResultSet r3 = ps3.executeQuery();

                if(isResultSetEmpty(r3)){
                    throw new DateOutOfRangeException("end date is not a valid date");
                }


                //instantiate list that we will return
                List<String> to_return = new ArrayList<String>();

                //find average of exchange rates and add to list
                String SQLAverage = "SELECT AVG(rate) AS \"average\" FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ?";
                PreparedStatement ps4 = prepStatement(SQLAverage);
                ps4.setString(1, start);
                ps4.setString(2, end);
                ps4.setString(3, from);
                ps4.setString(4, to);

                ResultSet rs4 = ps4.executeQuery();
                Double average = 0.00;

                if(rs4.next()){
                    average = rs4.getDouble("average");
                    to_return.add(String.format("%.2f", average));
                }


                //find median of exchange rates and add to list
                String SQLMedian = "SELECT COUNT(rate) AS \"count\" FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ?";
                PreparedStatement ps5 = prepStatement(SQLMedian);
                ps5.setString(1, start);
                ps5.setString(2, end);
                ps5.setString(3, from);
                ps5.setString(4, to);

                ResultSet rs5 = ps5.executeQuery();

                int count = 0;
                double median = 0;

                if(rs5.next()){
                    count = rs5.getInt("count");
                    double n = Double.valueOf(count);
                    if(n % 2 != 0){
                        n += 1;
                    }
                    if(n != 0){
                        n /= 2.00;
                    }
                    String SQLMiddle= "SELECT rank, rate FROM (SELECT rank() OVER (ORDER BY rate DESC)rank, rate FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ?) WHERE rank = ?";
                    PreparedStatement ps6 = prepStatement(SQLMiddle);
                    ps6.setString(1, start);
                    ps6.setString(2, end);
                    ps6.setString(3, from);
                    ps6.setString(4, to);
                    ps6.setLong(5, Math.round(n));

                    ResultSet rs6 = ps6.executeQuery();
                    median = rs6.getDouble("rate");
                    if(count % 2 == 0 && count != 0){
                        String SQLMiddle2 = "SELECT rank, rate FROM (SELECT rank() OVER (ORDER BY rate DESC)rank, rate FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ?) WHERE rank = ?";
                        PreparedStatement ps7 = prepStatement(SQLMiddle2);
                        ps7.setString(1, start);
                        ps7.setString(2, end);
                        ps7.setString(3, from);
                        ps7.setString(4, to);
                        ps7.setLong(5, Math.round(n+1));

                        ResultSet rs7 = ps7.executeQuery();
                        median = (median + rs7.getDouble("rate"))/2.00;
                    }
                    to_return.add(String.format("%.2f", median));
                }

                //find standard deviation of exchange rates and add to list
                String SQLSTD = "SELECT rate FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ?";

                PreparedStatement ps8 = prepStatement(SQLSTD);
                ps8.setString(1, start);
                ps8.setString(2, end);
                ps8.setString(3, from);
                ps8.setString(4, to);

                ResultSet rs8 = ps8.executeQuery();
                double standardDev = 0.00;

                while(rs8.next()){
                    standardDev += Math.pow((rs8.getDouble("rate") - average), 2);
                }
                if(standardDev != 0){
                    standardDev /= (Double.valueOf(count)-1.00);
                    standardDev = Math.pow(standardDev, (1.00/2.00));
                }

                to_return.add(String.format("%.2f", standardDev));



                //find maximum and minimum of exchange rates and add to list
                String SQLMaxMin = "SELECT MAX(rate) AS \"maximum\", MIN(rate) AS \"minimum\" FROM Rates WHERE date >= ? AND date <= ? AND from_cur = ? AND to_cur = ?";

                PreparedStatement ps9 = prepStatement(SQLMaxMin);
                ps9.setString(1, start);
                ps9.setString(2, end);
                ps9.setString(3, from);
                ps9.setString(4, to);

                ResultSet rs9 = ps9.executeQuery();

                if(rs9.next()){
                    to_return.add(String.format("%.2f", rs9.getDouble("minimum")));
                    to_return.add(String.format("%.2f", rs9.getDouble("maximum")));
                }

                //return the list of statistical information
                return to_return;
            }

            catch(SQLException e){
                System.err.println(e.getMessage());
            }
            return null;
        }

/*----------------------------------------------------------------------------*/

    @Override
    public String exchange(String from, String to, double amount) throws CurrencyDoesntExistException, InvalidAmountSpecifiedException, InvalidInputException{
        try{
            if (from.equals(to) || to.equals(from)){
                throw new InvalidInputException("Currencies 'to' and 'from' can't be the same");
            }
            //if amount is negative we throw an exception
            if(amount < 0){
                throw new InvalidAmountSpecifiedException("amount specified is negative");
            }
            //variable to contain the value we will return
            double exchangedValue = 0.00;
            String to_return = new String();

            //check if "to" and "from" currency exists in the database
            String checkExists = "SELECT currency FROM Currency WHERE currency = ?";
            PreparedStatement ps1 = prepStatement(checkExists);
            ps1.setString(1, from);
            ResultSet r1 = ps1.executeQuery();
            PreparedStatement ps0 = prepStatement(checkExists);
            ps0.setString(1, to);
            ResultSet r2 = ps0.executeQuery();

            if(isResultSetEmpty(r1) || isResultSetEmpty(r2)){
                throw new CurrencyDoesntExistException("Currency doesn't exist");
            }

            //get rate between "to" and "from" currencies and calculate converted value
            String getRate = "SELECT rate FROM Rates WHERE from_cur=? AND to_cur=? AND is_initial = 0 AND is_addcurrency = 0 GROUP BY from_cur, to_cur HAVING MAX(date)";
            PreparedStatement ps2 = prepStatement(getRate);
            ps2.setString(1, from);
            ps2.setString(2, to);
            ResultSet rs = ps2.executeQuery();
            if(rs.next()){
                exchangedValue = amount * rs.getDouble("rate");
            }
            else{
                getRate = "SELECT rate FROM Rates WHERE from_cur=? AND to_cur=? AND is_addcurrency = 1 GROUP BY from_cur, to_cur HAVING MAX(date)";
                PreparedStatement ps3 = prepStatement(getRate);
                ps3.setString(1, from);
                ps3.setString(2, to);
                rs = ps3.executeQuery();
                if(rs.next()){
                    exchangedValue = amount * rs.getDouble("rate");
                }
                else{
                    getRate = "SELECT rate FROM Rates WHERE from_cur=? AND to_cur=? GROUP BY from_cur, to_cur HAVING MAX(date)";
                    PreparedStatement ps4 = prepStatement(getRate);
                    ps4.setString(1, from);
                    ps4.setString(2, to);
                    rs = ps4.executeQuery();
                    if(rs.next()){
                        exchangedValue = amount * rs.getDouble("rate");
                    }
                }
            }

            to_return = String.format("%s %.2f", to, exchangedValue);

            //return String containing amount of converted currency
            return to_return;
        }

        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

/*----------------------------------------------------------------------------*/

    enum Compare {
        INCREASE,
        DECREASE,
        NOCHANGE
    }

    public void insertToList(List<String> row2, List<String> row3, List<String> row4, List<String> row5, int i, String element){
        if (i == 0){
            row2.add(element);
        }
        if (i == 1){
            row3.add(element);
        }
        if (i == 2){
            row4.add(element);
        }
        if (i == 3){
            row5.add(element);
        }
    }

    @Override
    public List<List<String>> getPopular() {
        List<List<String>> pop_list = new ArrayList<List<String>>();

        try {

            List<String> row1 = new ArrayList<>();
            List<String> row2 = new ArrayList<>();
            List<String> row3 = new ArrayList<>();
            List<String> row4 = new ArrayList<>();
            List<String> row5 = new ArrayList<>();

            List<String> cur_list = new ArrayList<String>();

            //get the list of popular currencies
            String getCur = "SELECT currency FROM Popular";
            PreparedStatement ps1 = prepStatement(getCur);
            ResultSet r1 = ps1.executeQuery();

            while (r1.next()) {
                cur_list.add(r1.getString("currency"));
            }

            row1.add("From/To");
            for (int i = 0; i < cur_list.size(); i++) {
                row1.add(cur_list.get(i));
            }
            row2.add(cur_list.get(0));
            row3.add(cur_list.get(1));
            row4.add(cur_list.get(2));
            row5.add(cur_list.get(3));

            String getRate = "SELECT * FROM rates WHERE from_cur = ? AND to_cur = ? ORDER BY insert_order DESC LIMIT 2";
            PreparedStatement ps2 = prepStatement(getRate);
            for (int i = 0; i < cur_list.size(); i++) {
                for (int j = 0; j < cur_list.size(); j++) {
                    if (cur_list.get(i).equals(cur_list.get(j))){
                        insertToList(row2, row3, row4, row5, i, "-");
                        continue;
                    }
                    ps2.setString(1, cur_list.get(i));
                    ps2.setString(2, cur_list.get(j));
                    // System.out.println(cur_list.get(i));
                    // System.out.println(cur_list.get(j));

                    ResultSet r2 = ps2.executeQuery();

                    double newestRate = 0.0;
                    double previousRate = 0.0;
                    Compare comparison = Compare.NOCHANGE;
                    int index = 0;
                    while (r2.next()) {

                        String rateString = r2.getString("rate");
                        // System.out.println(rateString);


                        if (index == 1){
                            previousRate = Double.parseDouble(rateString);
                            if (newestRate > previousRate) {
                                comparison = Compare.INCREASE;
                            }
                            if (newestRate < previousRate) {
                                comparison = Compare.DECREASE;
                            }
                            break;
                        }
                        newestRate = Double.parseDouble(rateString);

                        index++;
                    }

                    if (comparison == Compare.NOCHANGE){
                        insertToList(row2, row3, row4, row5, i, String.valueOf(newestRate));
                    }

                    if (comparison == Compare.INCREASE){
                        String element = String.valueOf(newestRate) + "↑";
                        insertToList(row2, row3, row4, row5, i, element);
                    }
                    if (comparison == Compare.DECREASE){
                        String element = String.valueOf(newestRate) + "↓";
                        insertToList(row2, row3, row4, row5, i, element);
                    }
                }
            }

            pop_list.add(row1);
            pop_list.add(row2);
            pop_list.add(row3);
            pop_list.add(row4);
            pop_list.add(row5);

        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }


        return pop_list;
    }

/*----------------------------------------------------------------------------*/


    @Override
    public void setPopular(List<String> currencies) throws CurrencyDoesntExistException, InvalidInputException{
        try{

            //check if all currencies exist
            String checkCur = "SELECT * FROM Currency WHERE currency IN (?)";
            PreparedStatement ps1 = prepStatement(checkCur);
            for (int i = 0; i < currencies.size(); i++) {
                ps1.setObject(1, currencies.get(i));
                ResultSet r1 = ps1.executeQuery();
                if (isResultSetEmpty(r1)){
                    throw new CurrencyDoesntExistException("Currency doesn't exist");
                }
            }

            //check if all currencies are different
            for (int i = 0; i < currencies.size(); i++){
                for (int j = i + 1; j < currencies.size(); j++){
                    if (currencies.get(i).equals(currencies.get(j))){
                        throw new InvalidInputException("Currencies can't be the same");
                    }
                }
            }

            //delete all rows in 'popular' and add new currencies in
            String delete = "DELETE FROM Popular";
            PreparedStatement ps3 = prepStatement(delete);
            ps3.executeUpdate();

            String insertStmt = "INSERT INTO Popular VALUES (?, ?)";
            PreparedStatement ps2 = prepStatement(insertStmt);
            for (int i = 0; i < currencies.size(); i++){
                ps2.setInt(1, i+1);
                ps2.setString(2, currencies.get(i));
                ps2.executeUpdate();
            }

        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }

    }

/*----------------------------------------------------------------------------*/

    /*-------------------------------------------------------------
    this method accepts the table name we wish to query and a
    list of attributes of the table.

    Executing this method is like querying SELECT * FROM tableName
    ---------------------------------------------------------------*/
    // @Override
    // public void seeTable(String tableName, List<String> arguments){
    //     try{
    //         //connect to database.db
    //
    //         Statement statement = connection.createStatement();
    //         statement.setQueryTimeout(30);
    //
    //         ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
    //         for(int i = 0; i < arguments.size(); i++){
    //             System.out.print(arguments.get(i) + " | ");
    //         }
    //         System.out.println();
    //         while(rs.next()){
    //             for(int i = 0; i < arguments.size(); i++){
    //                 System.out.print(rs.getString(arguments.get(i)) + " | ");
    //             }
    //             System.out.println();
    //         }
    //         System.out.println();
    //
    //     }
    //     catch(SQLException e){
    //         System.err.println(e.getMessage());
    //     }
    // }

}
