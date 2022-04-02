package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.DateOutOfRangeException;
import W18B_Group_5.Assignment1.model.CurrencyDoesntExistException;
import W18B_Group_5.Assignment1.model.DatabaseQuery;
import W18B_Group_5.Assignment1.model.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.DatePicker;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import W18B_Group_5.Assignment1.view.SummaryOutput;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class SummaryScreen extends ScreenDB {

    private Button process;
    private ComboBox<String> fromCurrency;
    private ComboBox<String> toCurrency;
    private Button back;
    private final Label message;

    public SummaryScreen(DatabaseQuery dq) {
        super(dq);

        process = new Button("Process");
        back = new Button("Back");

        DatePicker datePickerFrom = new DatePicker();
        HBox hBox_datePickerFrom = new HBox(datePickerFrom);
        DatePicker datePickerTo = new DatePicker();
        HBox hBox_datePickerTo = new HBox(datePickerTo);

        message = new Label("");



        process.setLayoutX(290);
        process.setLayoutY(200);
        hBox_datePickerTo.setLayoutX(290);
        hBox_datePickerTo.setLayoutY(150);
        hBox_datePickerFrom.setLayoutX(60);
        hBox_datePickerFrom.setLayoutY(150);
        back.setLayoutX(5);
        back.setLayoutY(5);
        message.setLayoutX(90);
        message.setLayoutY(190);

        this.addNode(process);

        this.addNode(hBox_datePickerFrom);
        this.addNode(hBox_datePickerTo);
        this.addNode(back);
        this.addNode(new Text(60, 70, "From"));
        this.addNode(new Text(290, 70, "To"));
        this.addNode(message);
//        this.addNode(new Text(110, 140, "Value"));


        process.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");
        back.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");

        Text ratesHist = new Text();
        HBox hBoxHist = new HBox(ratesHist);

        Text averageText = new Text();
        HBox hBoxAverage = new HBox(averageText);

        Text medianText = new Text();
        HBox hBoxMedian = new HBox(medianText);

        Text standevText = new Text();
        HBox hBoxStandev = new HBox(standevText);

        Text minText = new Text();
        HBox hBoxMin = new HBox(minText);

        Text maxText = new Text();
        HBox hBoxMax = new HBox(maxText);

        EventHandler<ActionEvent> processHandler = e -> {
            String from = fromCurrency.getValue();
            String to = toCurrency.getValue();
            try {
                List<String> ratesHistory = getDatabaseQuery().history(from, to, datePickerFrom.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), datePickerTo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                String formatted = "";
                for(int i = 0; i < ratesHistory.size(); i++){
                    formatted += ratesHistory.get(i) + "\n";
                }
                ratesHist.setText(formatted);
                List<String> ratesStats = getDatabaseQuery().summary(from, to, datePickerFrom.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), datePickerTo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                averageText.setText("average: " + ratesStats.get(0));
                medianText.setText("median: " + ratesStats.get(1));
                standevText.setText("standard deviation: " + ratesStats.get(2));
                minText.setText("minimum: " + ratesStats.get(3));
                maxText.setText("maximum: " + ratesStats.get(4));

                SummaryOutput summaryOutput = new SummaryOutput(hBoxHist, hBoxAverage, hBoxMedian, hBoxStandev, hBoxMin, hBoxMax);
                summaryOutput.showScene();

            } catch (CurrencyDoesntExistException ex) {
                message.setText("Error: Currency Doesn't Exist");
                message.setTextFill(Color.rgb(210, 39, 30));
            }  catch (DateOutOfRangeException ex) {
                message.setText("Error: Invalid date specified");
                message.setTextFill(Color.rgb(210, 39, 30));
            }  catch(InvalidInputException ex){
                message.setText("Error: Invalid selections");
                message.setTextFill(Color.rgb(210, 39, 30));
            } catch(NullPointerException ex){
                message.setText("Error: Please make selections");
                message.setTextFill(Color.rgb(210, 39, 30));
            }

        };

        setProcessEvent(processHandler);
    }

    public void setProcessEvent(EventHandler<ActionEvent> eventHandler) {
        process.setOnAction(eventHandler);
    }

    public void setBackExchangeEvent(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    @Override
    public Scene getScene() {
        message.setText("");

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

        hBoxFrom.setLayoutX(60);
        hBoxFrom.setLayoutY(80);
        hBoxTo.setLayoutX(290);
        hBoxTo.setLayoutY(80);
        this.addNode(hBoxFrom);
        this.addNode(hBoxTo);
        fromCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");
        toCurrency.setStyle("-fx-background-color: #c9c0eb; -fx-text-fill: white;");

        return super.getScene();
    }
}
