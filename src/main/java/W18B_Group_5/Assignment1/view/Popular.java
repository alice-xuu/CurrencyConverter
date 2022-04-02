package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.DatabaseQuery;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Popular extends ScreenDB {
    private Button back;
    private final double ROW_HEIGHT = 40;
    private final double COLUMN_WIDTH = 75;
    private final double HEADING_HEIGHT = 30;

    TableView<List<StringProperty>> popularTable;

    public Popular(DatabaseQuery dq) {
        super(dq);

        back = addButton("Back", 5, 5);
    }

    public void setBackExchangeEvent(EventHandler<ActionEvent> eventHandler) {
        back.setOnAction(eventHandler);
    }

    private Button addButton(String text, double x, double y) {
        Button button = new Button(text);

        button.setLayoutX(x);
        button.setLayoutY(y);

        this.addNode(button);

        button.setStyle("-fx-background-color: #4a34a3; -fx-text-fill: white;");

        return button;
    }

    private ObservableList<List<StringProperty>> toObservableList(List<List<String>> rows) {

        List<List<StringProperty>> observableRows = new ArrayList<>();
        List<StringProperty> observableRow;
        for (List<String> row: rows) {
            observableRow = new ArrayList<>();
            for (String str: row) {
                observableRow.add(new SimpleStringProperty(str));
            }
            observableRows.add(observableRow);
        }

        return FXCollections.observableArrayList(observableRows);
    }

    @Override
    public Scene getScene() {
        Scene scene = super.getScene();

        List<List<String>> popularRates = getDatabaseQuery().getPopular();
        List<String> headings = popularRates.remove(0);

        ObservableList<List<StringProperty>> observableRates = toObservableList(popularRates);
        // Set the items of the table
        popularTable = new TableView<>();
        popularTable.setItems(observableRates);

        // Specify how to get the value of each column in a row
        int numCols = popularRates.get(0).size();
        TableColumn<List<StringProperty>, String> column;
        for (int colIndex = 0; colIndex < numCols; ++colIndex) {
            final int finalColIndex = colIndex;

            column = new TableColumn<>(headings.get(colIndex));
            column.setCellValueFactory(row -> row.getValue().get(finalColIndex));
            column.setPrefWidth(COLUMN_WIDTH);

            popularTable.getColumns().add(column);
        }


        // Set table dimensions
        popularTable.setFixedCellSize(ROW_HEIGHT);

        HBox tableContainer = new HBox(popularTable);
        tableContainer.setLayoutX(50);
        tableContainer.setLayoutY(50);
        tableContainer.setPrefSize(COLUMN_WIDTH * 5 + 30, (ROW_HEIGHT * 4) + HEADING_HEIGHT);
        this.addNode(tableContainer);

        return scene;
    }
}

