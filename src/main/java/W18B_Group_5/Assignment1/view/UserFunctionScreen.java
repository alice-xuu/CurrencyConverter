package W18B_Group_5.Assignment1.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class UserFunctionScreen extends Screen {
    private Button exchange;
    private Button summary;
    private Button popular;
    private Button back;

    public UserFunctionScreen() throws FileNotFoundException {
        super();
        exchange = new Button("Exchange");
        summary = new Button("Summary");
        popular = new Button("Popular");
        back = new Button("Back");
        Text paragraph1 = new Text ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        Text paragraph2 = new Text ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        Text paragraph3 = new Text ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");


        ImageView imageView1 = new ImageView(new Image(new FileInputStream("assets/image1.png")));
        imageView1.setFitHeight(75);
        imageView1.setFitWidth(75);

        ImageView imageView2 = new ImageView(new Image(new FileInputStream("assets/image2.png")));
        imageView2.setFitHeight(75);
        imageView2.setFitWidth(75);

        ImageView imageView3 = new ImageView(new Image(new FileInputStream("assets/image3.png")));
        imageView3.setFitHeight(75);
        imageView3.setFitWidth(75);

        VBox vbox1 = new VBox(imageView1, exchange);
        vbox1.setMaxWidth(100);
        vbox1.setFillWidth(true);

        vbox1.setLayoutX(75);
        vbox1.setLayoutY(75);

        VBox vbox2 = new VBox(imageView2, summary);
        vbox2.setLayoutX(200);
        vbox2.setLayoutY(75);

        VBox vbox3 = new VBox(imageView3, popular);
        vbox3.setLayoutX(325);
        vbox3.setLayoutY(75);

        this.addNode(back);
        this.addNode(vbox1);
        this.addNode(vbox2);
        this.addNode(vbox3);

        exchange.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        summary.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        popular.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white; -fx-border-radius:200px;");
        vbox1.setStyle("-fx-width: 100px;");

        back.setLayoutX(5);
        back.setLayoutY(5);
//        exchange.setLayoutX(150);
//        exchange.setLayoutY(110);
        summary.setLayoutX(260);
        summary.setLayoutY(110);
        popular.setLayoutX(210);
        popular.setLayoutY(160);
    }

    public void setExchangeEvent(EventHandler<ActionEvent> eventHandler) {
        exchange.setOnAction(eventHandler);
    }

    public void setSummaryEvent(EventHandler<ActionEvent> eventHandler) {
        summary.setOnAction(eventHandler);
    }

    public void setPopularEvent(EventHandler<ActionEvent> eventHandler) {
        popular.setOnAction(eventHandler);
    }

    public void setBackUserEvent(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }


}
