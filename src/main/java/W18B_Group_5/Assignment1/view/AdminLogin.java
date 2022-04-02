package W18B_Group_5.Assignment1.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminLogin extends AdminLoginAbstract {

    private Button back;
    private final PasswordField passwordField;
    private final Label message;

    public AdminLogin(Stage primaryStage, AdminFunctionScreen adminFunctionScreen){
        super(primaryStage, adminFunctionScreen);

        back = new Button("Back");
        passwordField = new PasswordField();
        message = new Label("");


        passwordField.setPromptText("Enter password");
        passwordField.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (!passwordField.getText().equals("Password")) {
                    message.setText("Incorrect password");
                    message.setTextFill(Color.rgb(210, 39, 30));
                } else {
                    /* Go to AdminFunctionScreen.java */
                    primaryStage.setScene(adminFunctionScreen.getScene());
                    primaryStage.show();
                    message.setText("");
                }
                passwordField.clear();
            }
        });

        passwordField.setLayoutX(180);
        passwordField.setLayoutY(130);
        message.setLayoutX(185);
        message.setLayoutY(165);
        back.setLayoutX(5);
        back.setLayoutY(5);

        this.addNode(back);
        this.addNode(passwordField);
        this.addNode(message);


        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
    }

    public void setBackUserSelectEvent(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    @Override
    public Scene getScene() {
        message.setText("");
        return super.getScene();
    }
}
