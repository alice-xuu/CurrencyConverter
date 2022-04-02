package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.CurrencyDoesntExistException;
import W18B_Group_5.Assignment1.model.InvalidInputException;
import W18B_Group_5.Assignment1.model.DatabaseQuery;
import W18B_Group_5.Assignment1.model.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class AdminChangePopular extends ScreenDB {
    private Button update;
    private Button back;
    private ComboBox<String> firstCurrency;
    private ComboBox<String> secondCurrency;
    private ComboBox<String> thirdCurrency;
    private ComboBox<String> fourthCurrency;
    private final Label message;

    public AdminChangePopular(DatabaseQuery dq) {
        super(dq);

        update = new Button("Update");
        back = new Button("Back");

        message = new Label("");

        EventHandler<ActionEvent> updateHandler = e -> {
            try {
                List<String> popular = new ArrayList<String>();
                popular.add(firstCurrency.getValue());
                popular.add(secondCurrency.getValue());
                popular.add(thirdCurrency.getValue());
                popular.add(fourthCurrency.getValue());
                dq.setPopular(popular);
                message.setText("Popular currencies updated successfully");
                message.setTextFill(Color.rgb(0, 0, 0));
            }
            catch (CurrencyDoesntExistException er){
                message.setText("Error: Currency Doesnt Exist");
                message.setTextFill(Color.rgb(210, 39, 30));
            } catch (InvalidInputException er){
                message.setText("Error: Duplicate of currencies in popular rankings");
                message.setTextFill(Color.rgb(210, 39, 30));
            }
        };

        update.setOnAction(updateHandler);

        update.setLayoutX(280);
        update.setLayoutY(190);
        back.setLayoutX(5);
        back.setLayoutY(5);

        message.setLayoutX(110);
        message.setLayoutY(220);

        this.addNode(update);
        this.addNode(back);

        this.addNode(message);

        update.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
    }

    public void setBackAdminFunction(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    @Override
    public Scene getScene() {
        message.setText("");

        Text one = new Text("1");
        firstCurrency = new ComboBox<>();
        List<String> currencies = getDatabaseQuery().getCurrencies();
        for (String currency : currencies) {
            firstCurrency.getItems().add(currency);
        }

        Text two = new Text("2");
        secondCurrency = new ComboBox<>();
        for (String currency : currencies) {
            secondCurrency.getItems().add(currency);
        }

        Text three = new Text("3");
        thirdCurrency = new ComboBox<>();
        for (String currency : currencies) {
            thirdCurrency.getItems().add(currency);
        }

        Text four = new Text("4");
        fourthCurrency = new ComboBox<>();
        for (String currency : currencies) {
            fourthCurrency.getItems().add(currency);
        }

        one.setLayoutX(87);
        one.setLayoutY(87);
        firstCurrency.setLayoutX(100);
        firstCurrency.setLayoutY(70);

        two.setLayoutX(87);
        two.setLayoutY(127);
        secondCurrency.setLayoutX(100);
        secondCurrency.setLayoutY(110);

        three.setLayoutX(87);
        three.setLayoutY(167);
        thirdCurrency.setLayoutX(100);
        thirdCurrency.setLayoutY(150);

        four.setLayoutX(87);
        four.setLayoutY(207);
        fourthCurrency.setLayoutX(100);
        fourthCurrency.setLayoutY(190);

        firstCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        secondCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        thirdCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        fourthCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");

        one.setFill(Color.PURPLE);
        two.setFill(Color.PURPLE);
        three.setFill(Color.PURPLE);
        four.setFill(Color.PURPLE);

        this.addNode(firstCurrency);
        this.addNode(secondCurrency);
        this.addNode(thirdCurrency);
        this.addNode(fourthCurrency);

        this.addNode(one);
        this.addNode(two);
        this.addNode(three);
        this.addNode(four);

        return super.getScene();
    }
}
