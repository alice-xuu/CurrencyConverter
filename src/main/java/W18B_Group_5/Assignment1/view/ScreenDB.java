package W18B_Group_5.Assignment1.view;

import W18B_Group_5.Assignment1.model.DatabaseQuery;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

public abstract class ScreenDB {
    private Group group;
    private Scene scene = null;
    private final double SCREEN_WIDTH = 500;
    private final double SCREEN_HEIGHT = 300;
    private DatabaseQuery dq;


    public ScreenDB(DatabaseQuery dq) {
        group = new Group();
        this.dq = dq;
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
    protected DatabaseQuery getDatabaseQuery() {
        return dq;
    }
}
