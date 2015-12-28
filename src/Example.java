import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by cjlovering on 12/27/15.
 */
public class Example extends Application{
    public void init() {}
    public static void main(String[] args) { LauncherImpl.launchApplication(Example.class, args);
    }

    @Override
    public void start(Stage s) throws  Exception {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: gray");

        /* pane that slides right */
        BooleanProperty rightOpen = new SimpleBooleanProperty(true);
        rightOpen.setValue(true);
        SlidingPane right = new SlidingPane(200, 75, Direction.LEFT_RIGHT, rightOpen, new Text("Slide Right"));
        right.setStyle("-fx-background-color: red");
        Rectangle rightRect = new Rectangle(15, 15, Color.BLUE);
        AnchorPane.setRightAnchor(rightRect, 5.0);
        AnchorPane.setTopAnchor(rightRect,  5.0);
        rightRect.visibleProperty().bind(rightOpen);
        right.getChildren().addAll(right.getButton(), rightRect);

        AnchorPane.setTopAnchor(right, 550/2.0);
        AnchorPane.setLeftAnchor(right, 0.0);


        /* pane that slides left */
        BooleanProperty leftOpen = new SimpleBooleanProperty(true);
        leftOpen.setValue(true);
        SlidingPane left = new SlidingPane(200, 75, Direction.LEFT_RIGHT, leftOpen, new Text("Slide Left"));
        left.setStyle("-fx-background-color: green");
        Rectangle leftRect = new Rectangle(15, 15, Color.BLUE);
        AnchorPane.setLeftAnchor(leftRect,  5.0);
        AnchorPane.setTopAnchor(leftRect,   5.0);

        AnchorPane.setRightAnchor(left.getButton(), 0.0);
        left.getChildren().addAll(left.getButton(), leftRect);
        leftRect.visibleProperty().bind(leftOpen);


        AnchorPane.setTopAnchor(left, 550/2.0);
        AnchorPane.setRightAnchor(left, 0.0);


        /* pane that slides up */
        BooleanProperty upOpen = new SimpleBooleanProperty(true);
        SlidingPane up = new SlidingPane(200, 25, Direction.UP_DOWN, upOpen, new Text("Slide Up"));
        up.setStyle("-fx-background-color: orange");

        Rectangle upRect = new Rectangle(55, 15, Color.BLUE);
        AnchorPane.setLeftAnchor(upRect,  7.0);
        AnchorPane.setTopAnchor(upRect,   5.0);

        AnchorPane.setBottomAnchor(up.getButton(), 0.0);
        up.getChildren().addAll(up.getButton(), upRect);
        upRect.visibleProperty().bind(upOpen);


        AnchorPane.setBottomAnchor(up, 0.0);
        AnchorPane.setRightAnchor(up, 550/2.0);


        /* pane that slides down */
        BooleanProperty downOpen = new SimpleBooleanProperty(true);
        SlidingPane down = new SlidingPane(200, 25, Direction.UP_DOWN, downOpen, new Text("Slide Down"));
        down.setStyle("-fx-background-color: purple");


        Rectangle downRect = new Rectangle(75, 15, Color.BLUE);
        AnchorPane.setLeftAnchor(downRect,  7.0);
        AnchorPane.setBottomAnchor(downRect,   5.0);

        AnchorPane.setTopAnchor(down.getButton(), 0.0);
        down.getChildren().addAll(down.getButton(), downRect);
        downRect.visibleProperty().bind(downOpen);


        AnchorPane.setTopAnchor(down, 0.0);
        AnchorPane.setRightAnchor(down, 550/2.0);

        Button allButton = new Button("ALL");
        allButton.setOnAction(e -> {
            left.animatePane(leftOpen);
            right.animatePane(rightOpen);
            up.animatePane(upOpen);
            down.animatePane(downOpen);
        });

        Button closeButton = new Button("Close All");
        closeButton.setOnAction(event -> {
            left.playHidePane(leftOpen);
            right.playHidePane(rightOpen);
            up.playHidePane(upOpen);
            down.playHidePane(downOpen);
        });

        Button openButton = new Button("Open All");
        openButton.setOnAction(event -> {
            left.playShowPane(leftOpen);
            right.playShowPane(rightOpen);
            up.playShowPane(upOpen);
            down.playShowPane(downOpen);
        });

        AnchorPane.setTopAnchor(closeButton, 27.0);
        AnchorPane.setTopAnchor(openButton, 54.0);


        root.getChildren().addAll(left, right, up, down, allButton, openButton, closeButton);
        Scene scene = new Scene(root, 550, 550);
        s.setScene(scene);
        s.show();
    }
}
