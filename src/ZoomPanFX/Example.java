package ZoomPanFX;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by cjlovering on 12/27/15.
 */
public class Example extends Application {
    public void init() {}
    public static void main(String[] args) { LauncherImpl.launchApplication(Example.class, args);
    }

    @Override
    public void start(Stage s) throws  Exception {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: gray");


        Pane group = new Pane();
        Rectangle blue = new Rectangle(300, 200, Color.BLUE);
        Rectangle red = new Rectangle(100, 200, Color.RED);
        Rectangle green = new Rectangle(50, 310, Color.GREEN);
        Rectangle yellow = new Rectangle(75, 225, Color.YELLOW);
        group.getChildren().addAll(blue, red, green, yellow);

        ScrollPane zoom = new ZoomAndPan().createZoomPane(new Group(group));


        root.getChildren().addAll(zoom);
        Scene scene = new Scene(root, 550, 550);
        s.setScene(scene);
        s.show();
    }
}