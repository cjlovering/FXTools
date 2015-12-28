package FrostyFX;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by Xenocidist on 12/28/15.
 */



public class Frosty {

    private static final double BLUR_AMOUNT = 60;

    private static final Effect frostEffect =
            new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);


    /**
     * Creates the freeze image of the background
     * @param background
     * @return
     */
    public static StackPane freeze(Node background, Stage stage) {
        Image frostImage = background.snapshot(
                new SnapshotParameters(),
                null
        );

        ImageView frost = new ImageView(frostImage);

        Rectangle filler = new Rectangle(0, 0, stage.getWidth(), stage.getHeight());
        filler.widthProperty().bind(stage.widthProperty());
        filler.heightProperty().bind(stage.heightProperty());
        filler.setFill(Color.AZURE);

        Pane frostPane = new Pane(frost);
        frostPane.setEffect(frostEffect);

        StackPane frostView = new StackPane(
                filler,
                frostPane
        );

        Rectangle clipShape = new Rectangle(0, 0, stage.getWidth(), stage.getHeight());
        clipShape.heightProperty().bind(stage.heightProperty());
        clipShape.widthProperty().bind(stage.widthProperty());
        frostView.setClip(clipShape);

        return frostView;
    }
}
