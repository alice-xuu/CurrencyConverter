package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.CurrencyDoesntExistException;
import W18B_Group_5.Assignment1.model.DatabaseQuery;
import W18B_Group_5.Assignment1.model.InvalidAmountSpecifiedException;
import W18B_Group_5.Assignment1.model.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ExchangeScreen extends ScreenDB {
    private Button convert;
    private ComboBox<String> fromCurrency;
    private ComboBox<String> toCurrency;
    private Button back;
    private final Label message;
    private Text result;


    public ExchangeScreen(DatabaseQuery dq) {
        super(dq);

        convert = new Button("Convert");
        back = new Button("Back");

        message = new Label("");
        message.setTextFill(Color.rgb(210, 39, 30));

        TextField amount = new TextField();
        HBox hBoxAmount = new HBox(amount);

        result = new Text();
        HBox hBoxResult = new HBox(result);

        convert.setLayoutX(290);
        convert.setLayoutY(150);
        hBoxAmount.setLayoutX(110);
        hBoxAmount.setLayoutY(150);
        hBoxResult.setLayoutX(110);
        hBoxResult.setLayoutY(200);
        back.setLayoutX(5);
        back.setLayoutY(5);
        message.setLayoutX(110);
        message.setLayoutY(185);

        this.addNode(convert);

        this.addNode(hBoxAmount);
        this.addNode(hBoxResult);
        this.addNode(back);
        this.addNode(new Text(110, 70, "From"));
        this.addNode(new Text(290, 70, "To"));
        this.addNode(new Text(110, 140, "Value"));
        this.addNode(message);

        convert.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");

        EventHandler<ActionEvent> convertHandler = e -> {
            double fromAmount;
            message.setText("");
            result.setText("");
            try {
                fromAmount = Double.parseDouble(amount.getText());
            } catch (NumberFormatException er) {
                message.setText("Error: Invalid amount format");
                return;
            }
            String from = fromCurrency.getValue();
            String to = toCurrency.getValue();

            try {
                String toAmount = getDatabaseQuery().exchange(from, to, fromAmount);
                result.setText(toAmount);
            } catch (InvalidAmountSpecifiedException ex) {
                message.setText("Error: Invalid amount");

            } catch (CurrencyDoesntExistException ex) {
                message.setText("Error: Currency Doesn't Exist");

            } catch(InvalidInputException ex){
                message.setText("Error: Invalid selections");
            } catch(NullPointerException ex){
                message.setText("Error: Please make selections");
            }
            amount.setText("");
        };

        setConvertEvent(convertHandler);
    }

    public void setConvertEvent(EventHandler<ActionEvent> eventHandler) {
        convert.setOnAction(eventHandler);
    }

    public void setBackExchangeEvent(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    @Override
    public Scene getScene() {
        message.setText("");
        result.setText("");

        fromCurrency = new ComboBox<>();
        List<String> currencies = getDatabaseQuery().getCurrencies();
        for (String currency : currencies) {
            fromCurrency.getItems().add(currency);
        }
        HBox hBoxFrom = new HBox(fromCurrency);

        toCurrency = new ComboBox<>();
        for (String currency : currencies) {
            toCurrency.getItems().add(currency);
        }
        HBox hBoxTo = new HBox(toCurrency);

        fromCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        toCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");

        hBoxFrom.setLayoutX(110);
        hBoxFrom.setLayoutY(80);
        hBoxTo.setLayoutX(290);
        hBoxTo.setLayoutY(80);
        this.addNode(hBoxFrom);
        this.addNode(hBoxTo);

        return super.getScene();
    }
}
