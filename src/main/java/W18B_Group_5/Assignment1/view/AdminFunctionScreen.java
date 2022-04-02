package W18B_Group_5.Assignment1.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AdminFunctionScreen extends Screen {

    private Button exchange;
    private Button summary;
    private Button popular;

    private Button updateRate;
    private Button addCurrency;
    private Button changePopular;

    private Button back;

    public AdminFunctionScreen() throws FileNotFoundException {
        super();
        exchange = new Button("Exchange");
        summary = new Button("Summary");
        popular = new Button("Popular");
        back = new Button("Back");

        updateRate = new Button("Update an Exchange Rate");
        addCurrency = new Button("Add a new Currency");
        changePopular = new Button("Change popular currencies");


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
        vbox1.setLayoutY(55);

        VBox vbox2 = new VBox(imageView2, summary);
        vbox2.setLayoutX(210);
        vbox2.setLayoutY(55);

        VBox vbox3 = new VBox(imageView3, popular);
        vbox3.setLayoutX(325);
        vbox3.setLayoutY(55);


        exchange.setLayoutX(80);
        exchange.setLayoutY(140);
        summary.setLayoutX(210);
        summary.setLayoutY(140);
        popular.setLayoutX(330);
        popular.setLayoutY(140);

        updateRate.setLayoutX(80);
        updateRate.setLayoutY(190);
        addCurrency.setLayoutX(262);
        addCurrency.setLayoutY(190);
        changePopular.setLayoutX(155);
        changePopular.setLayoutY(230);

        back.setLayoutX(5);
        back.setLayoutY(5);


        exchange.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        summary.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        popular.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");

        updateRate.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        addCurrency.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        changePopular.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");

        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        vbox1.setStyle("-fx-width: 100px;");


        this.addNode(exchange);
        this.addNode(summary);
        this.addNode(popular);

        this.addNode(updateRate);
        this.addNode(addCurrency);
        this.addNode(changePopular);

        this.addNode(back);
        this.addNode(vbox1);
        this.addNode(vbox2);
        this.addNode(vbox3);
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

    public void setAddCurrencyEvent(EventHandler<ActionEvent> eventHandler) {
        addCurrency.setOnAction(eventHandler);
    }

    public void setUpdateRateEvent(EventHandler<ActionEvent> eventHandler) {
        updateRate.setOnAction(eventHandler);
    }

    public void setBackAdminLogin(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    public void setChangePopularEvent(EventHandler<ActionEvent> eventHandler) {
        changePopular.setOnAction(eventHandler);
    }

}
