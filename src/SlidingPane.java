import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Note: Change VBox to VBox or HBox as needed
 */
public class SlidingPane extends AnchorPane {
    private Direction direction;  //Direction that the pane will slide - either left-right or up-down

    private double edge;      //amount always visible
    private double expanded;  //expanded value

    private javafx.scene.control.Button controlButton = new javafx.scene.control.Button();

    public javafx.scene.control.Button getButton() {
        return controlButton;
    }


    private Animation hideSidebar = new Transition() {
        {
            setCycleDuration(Duration.millis(350));
        }

        protected void interpolate(double t) {

            final double S1 = 25.0 / 9.0;
            final double S3 = 10.0 / 9.0;
            final double S4 = 1.0 / 9.0;
            t = ((t < 0.2) ? S1 * t * t : S3 * t - S4);
            t = (t < 0.0) ? 0.0 : (t > 1.0) ? 1.0 : t;

            final double curHeight;
            final double curWidth;

            switch (direction){//note that this changes depending on if you want it closed or opened first
                case LEFT_RIGHT://to the right visible first, slides left
                    curWidth = edge + expanded * (1.0 - t);
                    setPrefWidth(curWidth);
                    break;
                case UP_DOWN://down not visible, click down
                    curHeight = edge + expanded * (1.0 - t);
                    setPrefHeight(curHeight);
                    break;
            }

        }

    };

    private Animation showSidebar = new Transition() {
        {
            setCycleDuration(Duration.millis(350));
        }

        protected void interpolate(double t) {
            final double S1 = 25.0 / 9.0;
            final double S3 = 10.0 / 9.0;
            final double S4 = 1.0 / 9.0;
            t = ((t < 0.2) ? S1 * t * t : S3 * t - S4);
            t = (t < 0.0) ? 0.0 : (t > 1.0) ? 1.0 : t;

            final double curHeight;
            final double curWidth;

            switch (direction){//note that this changes depending on if you want it closed or opened first
                case LEFT_RIGHT://to the right visible first, slides left
                    curWidth = edge + expanded * t;
                    setPrefWidth(curWidth);
                    break;
                case UP_DOWN:
                    curHeight =  edge + expanded * t;
                    setPrefHeight(curHeight);
                    break;

            }
        }
    };

    /**
     * Use this function to toggle animation programmatically
     * @param OPEN [the property determining if its open or not]
     */
    public void animatePane(BooleanProperty OPEN) {
        if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            if (OPEN.getValue()) {
                hideSidebar.play();
            } else {
                showSidebar.play();
            }
        }
    }


    /**
     * Use these functions to programmatically play specific animations
     * @param prop [the property determining if its open or not]
     */
    public void playHidePane(BooleanProperty prop){
        if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED){
            this.
            hideSidebar.play();
            //hideSidebar.setOnFinished(e -> prop.setValue(!prop.getValue()));
        }
    }

    /**
     * Use these functions to programmatically play animations
     * @param prop [the property determining if its open or not]
     */
    public void playShowPane(BooleanProperty prop){
        if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            showSidebar.play();
            //showSidebar.setOnFinished(e -> prop.setValue(!prop.getValue()));
        }
        return;
    }


    /**
     * The constructor:
     * @param expanded   = the expanded size of the pane [this excludes the edge]
     * @param edge       = the minimum size of the pane  [thus, total = edge + expanded]
     * @param direction  = the direction of the slide
     * @param OPEN       = if the pane is open
     *                     note 1: Pass false to have sliding pane start closed
     *                     note 2: Add sampleNode.visibleProperty.bind(OPEN); to hide desired parts
     * @param buttonNode = Graphic/Text of button that controls the sliding
     *                     note: It is not automatically added to the pane
     * @param nodes      = optional nodes that will automatically be added to the pane
     *                     note: you can add nodes later
     */
    public SlidingPane(final double expanded, final double edge, Direction direction, BooleanProperty OPEN, Node buttonNode, Node... nodes) {
        this.direction = direction;
        this.edge = edge;
        this.expanded = expanded;

        /* default is for an open pane, to change this use overloaded constructor */
        switch (direction){
            case LEFT_RIGHT:
                if (OPEN.getValue()) {
                    this.setPrefWidth(expanded + edge);
                    this.setMinWidth(edge);
                } else {
                    this.setPrefWidth(edge);
                    this.setMinWidth(edge);
                }
                break;
            case UP_DOWN:
                if (OPEN.getValue()) {
                    this.setPrefHeight(expanded + edge);
                    this.setMinHeight(edge);
                } else {
                    this.setPrefHeight(edge);
                    this.setMinHeight(edge);
                }
        }

        hideSidebar.setOnFinished(e -> OPEN.setValue(!OPEN.getValue()));
        showSidebar.setOnFinished(e -> OPEN.setValue(!OPEN.getValue()));

        this.controlButton = new javafx.scene.control.Button();
        controlButton.setGraphic(buttonNode);
        controlButton.setAlignment(Pos.CENTER);

        this.getChildren().addAll(nodes); /* Note: nodes can be added after the creation of the slidingPane */

        controlButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {



                if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
                    if (OPEN.getValue()) {
                        hideSidebar.play();
                    } else {
                        showSidebar.play();
                    }
                }
            }
        });
    }
}
