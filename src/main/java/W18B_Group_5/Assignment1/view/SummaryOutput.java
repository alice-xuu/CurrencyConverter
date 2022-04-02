package W18B_Group_5.Assignment1.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;


public class SummaryOutput {

    private Stage summaryStage = new Stage();
    private Group group;
    private Scene scene = null;
    private final double SCREEN_WIDTH = 500;
    private final double SCREEN_HEIGHT = 300;

    public SummaryOutput(HBox hBoxHist, HBox hBoxAverage, HBox hBoxMedian, HBox hBoxStandev, HBox hBoxMin, HBox hBoxMax) {
        group = new Group();
        summaryStage.setTitle("Summary");

        Text statsHeading = new Text("Summary Statistics");
        statsHeading.setStyle("-fx-font-size: 15px");
        statsHeading.setFill(Color.PURPLE);
        statsHeading.setLayoutX(40);
        statsHeading.setLayoutY(25);

        hBoxAverage.setLayoutX(40);
        hBoxAverage.setLayoutY(40);
        hBoxMedian.setLayoutX(40);
        hBoxMedian.setLayoutY(55);
        hBoxStandev.setLayoutX(40);
        hBoxStandev.setLayoutY(70);
        hBoxMin.setLayoutX(40);
        hBoxMin.setLayoutY(85);
        hBoxMax.setLayoutX(40);
        hBoxMax.setLayoutY(100);

        Text historyHeading = new Text("Historical Rates");
        historyHeading.setStyle("-fx-font-size: 15px");
        historyHeading.setFill(Color.PURPLE);
        historyHeading.setLayoutX(250);
        historyHeading.setLayoutY(25);

        hBoxHist.setLayoutX(250);
        hBoxHist.setLayoutY(40);

        this.addNode(hBoxHist);
        this.addNode(hBoxAverage);
        this.addNode(hBoxMedian);
        this.addNode(hBoxStandev);
        this.addNode(hBoxMin);
        this.addNode(hBoxMax);
        this.addNode(statsHeading);
        this.addNode(historyHeading);
    }

    public void addNode(Node node) {
        group.getChildren().add(node);
    }

    public void showScene() {
        if (scene == null) {
            scene = new Scene(group, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        summaryStage.setScene(scene);
        summaryStage.show();
    }
}
