package W18B_Group_5.Assignment1.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class AdminLoginAbstract {
    private Group group;
    private Scene scene = null;
    private final double SCREEN_WIDTH = 500;
    private final double SCREEN_HEIGHT = 300;

    public AdminLoginAbstract(Stage primaryStage, AdminFunctionScreen adminFunctionScreen) {
        group = new Group();
    }

    public void addNode(Node node) {
        group.getChildren().add(node);
    }

    public Scene getScene() {
        if (scene == null) {
            scene = new Scene(group, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        return scene;
    }
}
