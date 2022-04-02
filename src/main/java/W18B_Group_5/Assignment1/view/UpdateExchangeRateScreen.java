package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class UpdateExchangeRateScreen extends ScreenDB {

    private ComboBox<String> fromCurrency;
    private ComboBox<String> toCurrency;
    private Button createRate;

    private Button back;
    private final Label message;

    public UpdateExchangeRateScreen(DatabaseQuery dq) {
        super(dq);

        createRate = new Button("Create Rate");
        fromCurrency = new ComboBox<>();
        toCurrency = new ComboBox<>();
        message = new Label("");


        List<String> cur_list = new ArrayList<String>();
        cur_list = dq.getCurrencies();
        for (int i = 0; i < cur_list.size(); i++) {
            fromCurrency.getItems().add(cur_list.get(i));
            toCurrency.getItems().add(cur_list.get(i));
        }

        HBox hBoxFrom = new HBox(fromCurrency);
        HBox hBoxTo = new HBox(toCurrency);

        TextField rate = new TextField();
        rate.setPromptText("1 left equals x right");
        HBox hBoxAmount = new HBox(rate);

        back = new Button("Back");

        EventHandler<ActionEvent> convertHandler = e -> {
            try {
                String from = (String) fromCurrency.getValue();
                String to = (String) toCurrency.getValue();
                //check if inputs are null
                if (from == null || to == null){
                    throw new InvalidInputException("Invalid Inputs");
                }

                String new_rate = rate.getText();
                double double_new_rate = Double.parseDouble(new_rate);
                try {
                    dq.updateRate(from, to, double_new_rate);
                    message.setText("Rate updated successfully");
                    message.setTextFill(Color.rgb(0, 0, 0));
                }
                catch (CurrencyDoesntExistException er){
                    message.setText("Error: Currency Doesn't Exist");
                    message.setTextFill(Color.rgb(210, 39, 30));
                }
                catch (DailyRateAddedException er){
                    message.setText("Error: Daily rate added already");
                    message.setTextFill(Color.rgb(210, 39, 30));
                }
                catch(InvalidInputException er){
                    message.setText("Error: Invalid selections");
                    message.setTextFill(Color.rgb(210, 39, 30));
                } catch(NullPointerException ex){
                    message.setText("Error: Please make selections");
                    message.setTextFill(Color.rgb(210, 39, 30));
                }
            }

            catch (NumberFormatException er){
                message.setText("Error: Invalid Rate");
                message.setTextFill(Color.rgb(210, 39, 30));
            }
            catch (InvalidInputException er){
                message.setText("Error: Invalid Inputs");
                message.setTextFill(Color.rgb(210, 39, 30));
            }

        };

        createRate.setOnAction(convertHandler);

        hBoxFrom.setLayoutX(110);
        hBoxFrom.setLayoutY(80);
        hBoxTo.setLayoutX(290);
        hBoxTo.setLayoutY(80);
        createRate.setLayoutX(290);
        createRate.setLayoutY(120);
        hBoxAmount.setLayoutX(110);
        hBoxAmount.setLayoutY(120);
        back.setLayoutX(5);
        back.setLayoutY(5);
        message.setLayoutX(110);
        message.setLayoutY(165);

        this.addNode(createRate);
        this.addNode(hBoxFrom);
        this.addNode(hBoxTo);
        this.addNode(hBoxAmount);
        this.addNode(back);
        this.addNode(new Text(110, 70, "From"));
        this.addNode(new Text(290, 70, "To"));
        this.addNode(message);

        fromCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        toCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        createRate.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
    }

    public void setBackAdminFunction(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    @Override
    public Scene getScene() {
        message.setText("");
        return super.getScene();
    }
}
