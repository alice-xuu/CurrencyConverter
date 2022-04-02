package W18B_Group_5.Assignment1.model;

public class CurrencyDoesntExistException extends Exception {
    public CurrencyDoesntExistException(String msg) {
        super(msg);
    }
}
