package ZoomPanFX;

/**
 * Created by Josh on 12/1/2015.
 */

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


public class ZoomAndPan {

    private static final double MAX_SCALE = 15d;
    public double MIN_SCALE = 1d;
    private ScrollPane scrollPane = new ScrollPane();

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
    private final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);
    public PanAndZoomPane panAndZoomPane = new PanAndZoomPane();
    public double zoomOffsetX;
    public double zoomOffsetY;

    public ScrollPane createZoomPane(Group group) {

        //Never show scroll bars
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);


        zoomProperty.bind(panAndZoomPane.myScale);
        deltaY.bind(panAndZoomPane.deltaY);
        panAndZoomPane.getChildren().add(group);

        SceneGestures sceneGestures = new SceneGestures(panAndZoomPane);

        scrollPane.setContent(panAndZoomPane);
        panAndZoomPane.toBack();
        scrollPane.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scrollPane.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scrollPane.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        //TODO add listener for value
        scrollPane.viewportBoundsProperty().addListener(sceneGestures.getChangeListener());

        double zWidth = panAndZoomPane.getLayoutBounds().getWidth();
        double zHeight = panAndZoomPane.getLayoutBounds().getHeight();
        double vpH = scrollPane.getViewportBounds().getHeight();
        double vpW = scrollPane.getViewportBounds().getWidth();
        double initialScale = Math.min(vpH/zHeight, vpW/zWidth);
        double panXTrans = panAndZoomPane.getTranslateX();

        return scrollPane;
    }


      public void zoomToPoint(double x, double y){
          Bounds bounds = scrollPane.getViewportBounds();

          double transX = panAndZoomPane.getTranslateX()-(bounds.getWidth() - 550)/2; //TODO important values
          double transY = panAndZoomPane.getTranslateY()-(bounds.getHeight() - 550)/2; //TODO important values

          double scale = Math.min((bounds.getWidth()/550),(bounds.getHeight()/550));
          MIN_SCALE = scale;

          x -=((550/2)); //TODO if zooming is off alter these values
          y -=((550/2)); //TODO this too
          double scaleVal = 3.5;
          //System.out.println("out Scale" + scale);
          //if (bounds.getHeight()>530 || bounds.getWidth()>730 ||){
          zoomOffsetX = (transX+(x*scaleVal));
          zoomOffsetY = (transY+(y*scaleVal));
          panAndZoomPane.setPivot((transX+(x*scaleVal)),(transY+(y*scaleVal)),scaleVal);
    }

    /**
     * Mouse drag context used for scene and nodes.
     */
    class DragContext {

        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;

    }

    /**
     * Listeners for making the scene's canvas draggable and zoomable
     */
    public class SceneGestures {

        private DragContext sceneDragContext = new DragContext();

        PanAndZoomPane panAndZoomPane;

        public SceneGestures(PanAndZoomPane canvas) {
            this.panAndZoomPane = canvas;
        }

        public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
            return onMousePressedEventHandler;
        }

        public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
            return onMouseDraggedEventHandler;
        }

        public EventHandler<ScrollEvent> getOnScrollEventHandler() {
            return onScrollEventHandler;
        }

        public ChangeListener<Object> getChangeListener(){ return changeListener;}

        private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {

                sceneDragContext.mouseAnchorX = event.getX();
                sceneDragContext.mouseAnchorY = event.getY();

                sceneDragContext.translateAnchorX = panAndZoomPane.getTranslateX();
                sceneDragContext.translateAnchorY = panAndZoomPane.getTranslateY();
                //System.out.println("Screen Dragged");
            }

        };

        private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                Bounds bounds = scrollPane.getViewportBounds();
                double max_X_trans = ((bounds.getWidth() - 550)/2) + ((panAndZoomPane.getScale())*((bounds.getWidth()))-550);
                double min_X_trans = ((bounds.getWidth() - 550)/2) - ((panAndZoomPane.getScale())*((bounds.getWidth()))-550);
                double max_Y_trans = ((bounds.getHeight() - 550)/2) + ((panAndZoomPane.getScale())*((bounds.getHeight()))-550);
                double min_Y_trans = ((bounds.getHeight() - 550)/2) - ((panAndZoomPane.getScale())*((bounds.getHeight()))-550);

                double transX = sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX;

                if ((max_X_trans - panAndZoomPane.getTranslateX()) < 15 || (min_X_trans - panAndZoomPane.getTranslateX()) > -15){
                    transX = 0;
                }
                else if (((transX + panAndZoomPane.getTranslateX()) >= max_X_trans)){
                    transX =  max_X_trans - panAndZoomPane.getTranslateX();
                }
                else if ((transX + panAndZoomPane.getTranslateX()) <= min_X_trans){
                    transX = min_X_trans - panAndZoomPane.getTranslateX();
                }
                panAndZoomPane.setTranslateX(transX);

                double transY = sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY;

                if ((max_Y_trans - panAndZoomPane.getTranslateY()) < 15 || (min_Y_trans - panAndZoomPane.getTranslateY()) > -15){
                    transY = 0;
                }

                else if (((transY + panAndZoomPane.getTranslateY()) >= max_Y_trans)){
                    transY = max_Y_trans - panAndZoomPane.getTranslateY();
                }
                else if ((transY + panAndZoomPane.getTranslateY()) <= min_Y_trans){
                    transY = min_Y_trans - panAndZoomPane.getTranslateY();
                }

                panAndZoomPane.setTranslateY(transY);

                event.consume();
            }
        };
//TODO change this function to dynamically resize the image
        private ChangeListener<Object> changeListener = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                Bounds bounds = scrollPane.getViewportBounds();
                //System.out.println("viewport bounds: " + bounds.getWidth()+ " width, " + bounds.getHeight() +" height");
                double transX = panAndZoomPane.getTranslateX()-(bounds.getWidth() - 550)/2; //TODO important values
                double transY = panAndZoomPane.getTranslateY()-(bounds.getHeight() - 550)/2; //TODO important values

                double scale = Math.min((bounds.getWidth()/550),(bounds.getHeight()/550));
                MIN_SCALE = scale;
                //System.out.println("out Scale" + scale);
                //if (bounds.getHeight()>530 || bounds.getWidth()>730 ||){
                panAndZoomPane.setPivot(transX,transY,scale);
                //}

//            int left = -1 * (int) bounds.getMinX();
//            int right = left + (int) bounds.getWidth();
                //System.out.println("height!!!!!!!!!!!!!:" + scrollPane.getViewportBounds().getHeight());
                //panAndZoomPane.setPivot(0,0, panAndZoomPane.getScale()*1.05);
            }
        };

        /**
         * Mouse wheel handler: zoom to pivot point
         */
        private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {

                double delta = PanAndZoomPane.DEFAULT_DELTA;

                double scale = panAndZoomPane.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;

                panAndZoomPane.setDeltaY(event.getDeltaY());
                if (panAndZoomPane.deltaY.get() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }

                double newScale = clamp(scale,MIN_SCALE, MAX_SCALE);
                //System.out.println("Scale ");

                double f = (scale / oldScale)-1;

                double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth()/2 + panAndZoomPane.getBoundsInParent().getMinX()));
                double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight()/2 + panAndZoomPane.getBoundsInParent().getMinY()));

                if (newScale == scale){
                    panAndZoomPane.setPivot(f*dx, f*dy, newScale);
                }

                if (newScale == MIN_SCALE){
//                    panAndZoomPane.setPivot(0,0,newScale);
                    Bounds bounds = scrollPane.getViewportBounds();
                    //System.out.println("viewport bounds: " + bounds.getWidth()+ " width, " + bounds.getHeight() +" height");
                    double transX = panAndZoomPane.getTranslateX()-(bounds.getWidth() - 550)/2; //TODO important values
                    double transY = panAndZoomPane.getTranslateY()-(bounds.getHeight() - 550)/2; //TODO important values

                    //System.out.println("out Scale" + scale);
                    //if (bounds.getHeight()>530 || bounds.getWidth()>730 ||){
                    panAndZoomPane.setPivot(transX,transY,MIN_SCALE);
                }
                //System.out.println("scale" + panAndZoomPane.getScale());
                event.consume();

            }
        };

    }

    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }


}