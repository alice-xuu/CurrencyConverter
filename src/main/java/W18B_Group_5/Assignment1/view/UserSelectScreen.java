package W18B_Group_5.Assignment1.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;

public class UserSelectScreen extends Screen {
    private Button normalUser;
    private Button admin;

    public UserSelectScreen() {
        super();
        normalUser = new Button("Normal User");
        admin = new Button("Admin");
        Text heading = new Text ("Are you...");

        normalUser.setLayoutX(150);
        normalUser.setLayoutY(140);
        admin.setLayoutX(260);
        admin.setLayoutY(140);
        heading.setLayoutX(170);
        heading.setLayoutY(100);

        this.addNode(normalUser);
        this.addNode(admin);
        this.addNode(heading);

        normalUser.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        admin.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        heading.setStyle("-fx-font-size: 30px;");
    }

    public void setNormalUserEvent(EventHandler<ActionEvent> eventHandler) {
        normalUser.setOnAction(eventHandler);
    }

    public void setAdminEvent(EventHandler<ActionEvent> eventHandler) {
        admin.setOnAction(eventHandler);
    }
}
