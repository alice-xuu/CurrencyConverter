 package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.CurrencyAlreadyExistsException;
import W18B_Group_5.Assignment1.model.DatabaseQuery;
import W18B_Group_5.Assignment1.model.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class NewCurrencyScreen extends ScreenDB {
    private Button add;
    private Button back;
    private final TextField newCurrency;
    private final Label message;

    public NewCurrencyScreen(DatabaseQuery dq) {
        super(dq);

        add = new Button("Add Currency");
        back = new Button("Back");
        newCurrency = new TextField();
        newCurrency.setPromptText("Currency name");

        message = new Label("");


        EventHandler<ActionEvent> convertHandler = e -> {
            String new_cur = newCurrency.getText();
            try {
                dq.addCurrency(new_cur);
                message.setText("Currency added successfully");
                message.setTextFill(Color.rgb(0, 0, 0));
            }
            catch (CurrencyAlreadyExistsException er){
                message.setText("Error: Currency Already Exists");
                message.setTextFill(Color.rgb(210, 39, 30));
            } catch (InvalidInputException er){
                message.setText("Error: Please specify a new currency");
                message.setTextFill(Color.rgb(210, 39, 30));
            }
        };

        add.setOnAction(convertHandler);

        add.setLayoutX(280);
        add.setLayoutY(110);
        back.setLayoutX(5);
        back.setLayoutY(5);
        newCurrency.setLayoutX(100);
        newCurrency.setLayoutY(110);
        message.setLayoutX(110);
        message.setLayoutY(165);

        this.addNode(add);
        this.addNode(back);
        this.addNode(newCurrency);
        this.addNode(message);

        add.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
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
