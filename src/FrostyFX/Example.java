package FrostyFX;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
        StackPane root = new StackPane();
        HBox content = new HBox();
        content.setStyle("-fx-background-color: gray");

        Rectangle blue = new Rectangle(300, 200, Color.BLUE);
        Rectangle red = new Rectangle(100, 200, Color.RED);
        Rectangle green = new Rectangle(50, 310, Color.GREEN);
        Rectangle yellow = new Rectangle(75, 225, Color.YELLOW);

        Button getFrosty = new Button("Get Frosty");
        getFrosty.setOnAction(e -> {
            StackPane frosty = Frosty.freeze(content, s);
            root.getChildren().add(frosty);
            frosty.setOnMouseClicked(z -> root.getChildren().remove(frosty));

        });

        content.getChildren().addAll(blue, red, green, yellow, getFrosty);


        root.getChildren().addAll(content);
        Scene scene = new Scene(root, 550, 550);
        s.setScene(scene);
        s.show();
    }
}