package cuie.kappeler_zuercher.dashboard;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

import java.util.HashMap;

/**
 * Das Dashboard dient dazu, dass man die Anzahl an verschiedenen Skiliften per Slider einstellen kann.
 * Bei den Slider sind die min und max Werte gesetzt, welche nicht überschritten werden können.
 *
 */
public class DashboardControl extends Region {
    private static final double ARTBOARD_WIDTH  = 800;
    private static final double ARTBOARD_HEIGHT = 400;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 100;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 1000;


    private GaugeChartControl gaugeChartControl;
    private SkiliftControl skiliftControl;
    private ImageView image;
    private Rectangle background;

    private final HashMap<String, Image> imageHashMap = new HashMap<>();

    private final IntegerProperty gondelValue = new SimpleIntegerProperty();
    private final IntegerProperty schleppliftValue = new SimpleIntegerProperty();
    private final IntegerProperty sesselliftValue = new SimpleIntegerProperty();
    private final IntegerProperty openLiftValue = new SimpleIntegerProperty();


    private final StringProperty locationValue = new SimpleStringProperty();
    private final StringProperty cantonValue = new SimpleStringProperty();
    private final StringProperty snowheightValue = new SimpleStringProperty();
    private final StringProperty imageUrlValue = new SimpleStringProperty();

    // needed for resizing
    private Pane drawingPane;

    public DashboardControl() {
        initializeSelf();
        initializeParts();
        initializeDrawingPane();
        layoutParts();
        setupEventHandlers();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeSelf() {
        loadFonts("/fonts/Roboto/Roboto-Regular.ttf");
        loadFonts("/fonts/Roboto/Roboto-Bold.ttf");
        addStylesheetFiles("style.css");

        getStyleClass().add("dashboard-control");
    }

    private void initializeParts() {
        gaugeChartControl = new GaugeChartControl();
        skiliftControl = new SkiliftControl();
        image = new ImageView();
        image.maxHeight(400);
        image.setFitWidth(800);
        image.setPreserveRatio(true);

        background = new Rectangle(800,400);
        background.getStyleClass().add("background");
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setClip(new Rectangle(800,400));
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        drawingPane.getChildren().addAll(image, background, gaugeChartControl,skiliftControl);
        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {

    }

    private void setupValueChangeListeners() {
        imageUrlValueProperty().addListener((x,t,y) -> {
            Image img;

            //cach image for faster loading
            if(imageHashMap.containsKey(imageUrlValueProperty().getValue())) {
                img = imageHashMap.get(imageUrlValueProperty().getValue());
            } else {
                if(imageUrlValueProperty().getValue() != null && !imageUrlValueProperty().getValue().isEmpty()) {
                    img = new Image(imageUrlValueProperty().getValue(), true);
                    imageHashMap.put(imageUrlValueProperty().getValue(), img);
                } else{
                    img = null;
                }
            }

            image.setImage(img);
        });
    }

    private void setupBindings() {
        gaugeChartControl.gondelValueProperty().bind(gondelValueProperty());
        gaugeChartControl.openLiftValueProperty().bind(openLiftValueProperty());
        gaugeChartControl.sesselliftValueProperty().bind(sesselliftValueProperty());
        gaugeChartControl.schleppliftValueProperty().bind(schleppliftValueProperty());

        skiliftControl.cantonValueProperty().bind(cantonValueProperty());
        skiliftControl.sesselliftValueProperty().bindBidirectional(sesselliftValueProperty());
        skiliftControl.gondelValueProperty().bindBidirectional(gondelValueProperty());
        skiliftControl.schleppliftValueProperty().bindBidirectional(schleppliftValueProperty());
        skiliftControl.locationValueProperty().bind(locationValueProperty());
        skiliftControl.snowheightValueProperty().bind(snowheightValueProperty());
    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    //resize by scaling
    private void resize() {
        Insets padding         = getPadding();
        double availableWidth  = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    // some handy functions

    private void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    private void addStylesheetFiles(String... stylesheetFile){
        for(String file : stylesheetFile){
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }


    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // alle getter und setter  (generiert via "Code -> Generate... -> Getter and Setter)
    public int getGondelValue() {
        return gondelValue.get();
    }

    public IntegerProperty gondelValueProperty() {
        return gondelValue;
    }

    public void setGondelValue(int gondelValue) {
        this.gondelValue.set(gondelValue);
    }

    public String getLocationValue() {
        return locationValue.get();
    }

    public StringProperty locationValueProperty() {
        return locationValue;
    }

    public void setLocationValue(String locationValue) {
        this.locationValue.set(locationValue);
    }

    public String getCantonValue() {
        return cantonValue.get();
    }

    public StringProperty cantonValueProperty() {
        return cantonValue;
    }

    public void setCantonValue(String cantonValue) {
        this.cantonValue.set(cantonValue);
    }

    public String getSnowheightValue() {
        return snowheightValue.get();
    }

    public StringProperty snowheightValueProperty() {
        return snowheightValue;
    }

    public void setSnowheightValue(String snowheightValue) {
        this.snowheightValue.set(snowheightValue);
    }

    public String getImageUrlValue() {
        return imageUrlValue.get();
    }

    public StringProperty imageUrlValueProperty() {
        return imageUrlValue;
    }

    public void setImageUrlValue(String imageUrlValue) {
        this.imageUrlValue.set(imageUrlValue);
    }

    public int getSchleppliftValue() {
        return schleppliftValue.get();
    }

    public IntegerProperty schleppliftValueProperty() {
        return schleppliftValue;
    }

    public void setSchleppliftValue(int schleppliftValue) {
        this.schleppliftValue.set(schleppliftValue);
    }

    public int getSesselliftValue() {
        return sesselliftValue.get();
    }

    public IntegerProperty openLiftValueProperty(){
        return openLiftValue;
    }

    public int getOpenLiftValueProperty(){
        return openLiftValue.get();
    }

    public void setOpenLiftValueProperty(int openLiftValue){
        this.openLiftValue.set(openLiftValue);
    }

    public IntegerProperty sesselliftValueProperty() {
        return sesselliftValue;
    }

    public void setSesselliftValue(int sesselliftValue) {
        this.sesselliftValue.set(sesselliftValue);
    }


}

