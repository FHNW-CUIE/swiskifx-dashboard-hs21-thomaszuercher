package cuie.kappeler_zuercher.dashboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Das Dashboard dient dazu, dass man die Anzahl an verschiedenen Skiliften per Slider einstellen kann.
 * Bei den Slider sind die min und max Werte gesetzt, welche nicht überschritten werden können.
 */
public class SkiliftControl extends Region {
    private static final double ARTBOARD_WIDTH = 400;
    private static final double ARTBOARD_HEIGHT = 400;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH = 100;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 1000;

    private static final Image GONDEL_IMG = new Image("/images/Gondel.png");
    private static final Image SCHLEPPLIFT_IMG = new Image("/images/Schlepplift.png");
    private static final Image SCHLEPPLIFT_SEIL_IMG = new Image("/images/SchleppliftSeil.png");
    private static final Image SESSELLIFT_IMG = new Image("/images/Sessellift.png");
    private static final Image SESSELLIFT_SEIL_IMG = new Image("/images/SesselliftSeil.png");

    private static final Integer MIN = 0;
    private static final Integer MAX = 100;

    private static final Color COLOR_GONDEL = Color.rgb(81, 108, 151);
    private static final Color COLOR_SCHLEPPLIFT = Color.rgb(17, 146, 242);
    private static final Color COLOR_SESSELLIFT = Color.rgb(63, 82, 255);

    private Text displayLocation;
    private Text displayCanton;
    private Text minLifte;
    private Text maxLifte;
    private Rectangle line;
    private Rectangle minLine;
    private Rectangle maxLine;
    private Rectangle gondel;
    private Rectangle schlepplift;
    private Rectangle schleppliftSeil;
    private Rectangle sessellift;
    private Rectangle sesselliftSeil;
    private Circle gondelPoint;
    private Circle schleppliftPoint;
    private Circle sesselliftPoint;

    private final IntegerProperty gondelValue = new SimpleIntegerProperty();
    private final IntegerProperty schleppliftValue = new SimpleIntegerProperty();
    private final IntegerProperty sesselliftValue = new SimpleIntegerProperty();
    private final StringProperty locationValue = new SimpleStringProperty();
    private final StringProperty cantonValue = new SimpleStringProperty();
    private final StringProperty snowheightValue = new SimpleStringProperty();

    // needed for resizing
    private Pane drawingPane;

    public SkiliftControl() {
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

        getStyleClass().add("skilift-control");
    }

    private void initializeParts() {
        displayLocation = new Text(10, 25, "");
        displayLocation.getStyleClass().add("location-title");
        displayCanton = new Text(10, 60, "");
        displayCanton.getStyleClass().add("canton");
        minLifte = new Text(MIN.toString());
        minLifte.getStyleClass().add("min-lifte");
        minLifte.setX(29);
        minLifte.setY(153);
        maxLifte = new Text(MAX.toString());
        maxLifte.setX(355);
        maxLifte.setY(153);
        maxLifte.getStyleClass().add("max-lifte");
        line = new Rectangle(0, 177, 400, 3);
        minLine = new Rectangle(31, 167, 3, 24);
        maxLine = new Rectangle(365, 167, 3, 24);

        gondel = new Rectangle(minLine.getX() - 40, 177, 70, 58);
        schlepplift = new Rectangle(minLine.getX() - 40, 261, 48, 45);
        schleppliftSeil = new Rectangle(minLine.getX() - 40, 177, 41, 109);
        sessellift = new Rectangle(minLine.getX() - 40, 331, 59, 59);
        sesselliftSeil = new Rectangle(minLine.getX() - 40, 177, 2, 184);
        ImagePattern patternGondel = new ImagePattern(GONDEL_IMG);
        ImagePattern patternSchlepplift = new ImagePattern(SCHLEPPLIFT_IMG);
        ImagePattern patternSchleppliftSeil = new ImagePattern(SCHLEPPLIFT_SEIL_IMG);
        ImagePattern patternSessellift = new ImagePattern(SESSELLIFT_IMG);
        ImagePattern patternSesselliftSeil = new ImagePattern(SESSELLIFT_SEIL_IMG);
        gondel.setFill(patternGondel);
        schlepplift.setFill(patternSchlepplift);
        schleppliftSeil.setFill(patternSchleppliftSeil);
        sessellift.setFill(patternSessellift);
        sesselliftSeil.setFill(patternSesselliftSeil);
        gondelPoint = new Circle(158, 178, 10);
        gondelPoint.setFill(COLOR_GONDEL);
        schleppliftPoint = new Circle(168, 178, 10);
        schleppliftPoint.setFill(COLOR_SCHLEPPLIFT);
        sesselliftPoint = new Circle(178, 178, 10);
        sesselliftPoint.setFill(COLOR_SESSELLIFT);
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        drawingPane.getChildren()
                .addAll(displayLocation, displayCanton, minLifte, maxLifte, line, minLine, maxLine, gondel,
                        schleppliftSeil, schlepplift, sesselliftSeil, sessellift, gondelPoint, schleppliftPoint,
                        sesselliftPoint);

        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {
        gondel.setOnMouseDragged(event -> {
            double per = valueToPercentage(event.getX() - 5, minLine.getX(), maxLine.getX());
            int newValue = (int) percentageToValue(per, MIN, MAX);
            setGondelValue(MIN >= newValue ? 0 : MAX <= newValue ? 100 : newValue);
        });
        schlepplift.setOnMouseDragged(event -> {
            double per = valueToPercentage(event.getX() + 50, minLine.getX(), maxLine.getX());
            int newValue = (int) percentageToValue(per, MIN, MAX);
            setSchleppliftValue(MIN >= newValue ? 0 : MAX <= newValue ? 100 : newValue);
        });
        sessellift.setOnMouseDragged(event -> {
            double per = valueToPercentage(event.getX(), minLine.getX(), maxLine.getX());
            int newValue = (int) percentageToValue(per, MIN, MAX);
            setSesselliftValue(MIN >= newValue ? 0 : MAX <= newValue ? 100 : newValue);
        });
    }

    private void setupValueChangeListeners() {
        gondelValueProperty().addListener((observable, oldValue, newValue) -> replaceGondel(newValue.intValue()));
        schleppliftValueProperty().addListener(
                (observable, oldValue, newValue) -> replaceSchlepplift(newValue.intValue()));
        sesselliftValueProperty().addListener(
                (observable, oldValue, newValue) -> replaceSessellift(newValue.intValue()));
    }

    private void setupBindings() {
        displayLocation.textProperty().bind(locationValueProperty());
        displayCanton.textProperty()
                .bind(cantonValueProperty().concat(" (").concat(snowheightValueProperty()).concat(" cm Schnee)"));
    }

    @Override protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void replaceGondel(int value) {
        var per = valueToPercentage(value, MIN, MAX);
        var x = (maxLine.getX() - minLine.getX()) * per;
        gondel.setX(x + 3);
        gondelPoint.setCenterX(x + 30);
    }

    private void replaceSchlepplift(int value) {
        var per = valueToPercentage(value, MIN, MAX);
        var x = (maxLine.getX() - minLine.getX()) * per;
        schlepplift.setX(x - 32);
        schleppliftSeil.setX(x - 7);
        schleppliftPoint.setCenterX(x + 30);
    }

    private void replaceSessellift(int value) {
        var per = valueToPercentage(value, MIN, MAX);
        var x = (maxLine.getX() - minLine.getX()) * per;
        sessellift.setX(x + 4);
        sesselliftSeil.setX(x + 30);
        sesselliftPoint.setCenterX(x + 30);
    }

    //resize by scaling
    private void resize() {
        Insets padding = getPadding();
        double availableWidth = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH),
                MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    // some handy functions

    private void loadFonts(String... font) {
        for (String f : font) {
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    private void addStylesheetFiles(String... stylesheetFile) {
        for (String file : stylesheetFile) {
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    private double percentageToValue(double percentage, double minValue, double maxValue) {
        return ((maxValue - minValue) * percentage) + minValue;
    }

    private double valueToPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue);
    }

    // compute sizes

    @Override protected double computeMinWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override protected double computeMinHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override protected double computePrefWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override protected double computePrefHeight(double width) {
        Insets padding = getPadding();
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

    public IntegerProperty sesselliftValueProperty() {
        return sesselliftValue;
    }

    public void setSesselliftValue(int sesselliftValue) {
        this.sesselliftValue.set(sesselliftValue);
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
}
