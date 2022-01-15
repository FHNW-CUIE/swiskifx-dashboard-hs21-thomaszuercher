package cuie.kappeler_zuercher.dashboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
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

/**
 * Das Dashboard dient dazu, dass man die Anzahl an verschiedenen Skiliften per Slider einstellen kann.
 * Bei den Slider sind die min und max Werte gesetzt, welche nicht überschritten werden können.
 *
 */
public class GaugeChartControl extends Region {
    private static final double ARTBOARD_WIDTH  = 800;
    private static final double ARTBOARD_HEIGHT = 400;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 100;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 1000;

    private Text title;
    private Text gondelVal;
    private Text schleppliftVal;
    private Text sesselliftVal;
    private Arc  gondelArc;
    private Arc  schleppliftArc;
    private Arc  sesselliftArc;
    private Rectangle gondelRect;
    private Rectangle schleppliftRect;
    private Rectangle sesselliftRect;
    private Text gondelDesc;
    private Text schleppliftDesc;
    private Text sesselliftDesc;

    private final IntegerProperty gondelValue = new SimpleIntegerProperty();
    private final IntegerProperty schleppliftValue = new SimpleIntegerProperty();
    private final IntegerProperty sesselliftValue = new SimpleIntegerProperty();
    private final IntegerProperty openLiftValue = new SimpleIntegerProperty();

    // needed for resizing
    private Pane drawingPane;

    public GaugeChartControl() {
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

        getStyleClass().add("gauge-control");
    }

    private void initializeParts() {
        title = new Text(424, 23,"0 von 30 Life geöffnet");
        title.getStyleClass().add("title-text");
        setCenteredText(title, 600, 50);
        gondelVal = new Text("0");
        gondelVal.getStyleClass().add("chart-text");
        sesselliftVal = new Text("0");
        sesselliftVal.getStyleClass().add("chart-text");
        schleppliftVal = new Text("0");
        schleppliftVal.getStyleClass().add("chart-text");

        gondelArc = new Arc(600, 200, 100, 100,0, 150);
        gondelArc.getStyleClass().add("gondel-arc");
        schleppliftArc = new Arc(600, 200, 100, 100,150, 300);
        schleppliftArc.getStyleClass().add("schlepplift-arc");
        sesselliftArc = new Arc(600, 200, 100, 100,300, 360);
        sesselliftArc.getStyleClass().add("sessellift-arc");

        gondelArc.setType(ArcType.ROUND);
        sesselliftArc.setType(ArcType.ROUND);
        schleppliftArc.setType(ArcType.ROUND);


        gondelRect = new Rectangle(470,350,20, 20);
        gondelRect.getStyleClass().add("gondel-rect");
        gondelDesc = new Text(495, 365, "Gondeln");


        schleppliftRect = new Rectangle(570,350,20, 20);
        schleppliftRect.getStyleClass().add("schlepplift-rect");
        schleppliftDesc = new Text(595, 365, "Schlepplifte");

        sesselliftRect = new Rectangle(670,350,20, 20);
        sesselliftRect.getStyleClass().add("sessellift-rect");
        sesselliftDesc = new Text(695, 365, "Sessellifte");
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        drawingPane.getChildren().addAll(gondelArc, sesselliftArc, schleppliftArc, title, gondelVal, sesselliftVal,
                schleppliftVal, sesselliftRect, gondelRect, schleppliftRect, gondelDesc, schleppliftDesc, sesselliftDesc);
        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {

    }

    private void setupValueChangeListeners() {
        gondelValueProperty().addListener((observable, oldValue, newValue) -> update());
        schleppliftValueProperty().addListener((observable, oldValue, newValue) -> update());
        sesselliftValueProperty().addListener((observable, oldValue, newValue) -> update());
    }

    private void setupBindings() {
        gondelVal.textProperty().bind(gondelValueProperty().asString());
        schleppliftVal.textProperty().bind(schleppliftValueProperty().asString());
        sesselliftVal.textProperty().bind(sesselliftValueProperty().asString());
    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void update(){
        updateTitle();
        updateChart();
    }

    private void updateTitle(){
        title.textProperty().set(openLiftValue.get() + " von " + getNrOfLifts() + " Lifte geöffnet");
        setCenteredText(title, 600, 50);
    }

    private void updateChart(){
        double angleGondel = valueToAngle(getGondelValue(), 0, getNrOfLifts());
        double angleSchlepplift = valueToAngle(getSchleppliftValue(), 0, getNrOfLifts());
        double angleSessellift = valueToAngle(getSesselliftValue(), 0, getNrOfLifts());

        gondelArc.setStartAngle(0);
        gondelArc.setLength(angleGondel);
        schleppliftArc.setStartAngle(angleGondel);
        schleppliftArc.setLength(angleSchlepplift);
        sesselliftArc.setStartAngle(angleGondel + angleSchlepplift);
        sesselliftArc.setLength(angleSessellift);

        Point2D gondelTextPoint = getPointInMiddle(0, angleGondel, 50);
        Point2D schleppliftTextPoint = getPointInMiddle(angleGondel, angleSchlepplift, 50);
        Point2D sesselliftTextPoint = getPointInMiddle( angleGondel + angleSchlepplift, angleSessellift, 50);

        setCenteredText(gondelVal, gondelTextPoint.getX(), gondelTextPoint.getY());
        setCenteredText(schleppliftVal, schleppliftTextPoint.getX(), schleppliftTextPoint.getY());
        setCenteredText(sesselliftVal, sesselliftTextPoint.getX(), sesselliftTextPoint.getY());
    }

    private Point2D getPointInMiddle(double startAngle, double lengthAngle, int distance){
        double radAngle = (((lengthAngle + 180) / 2) + startAngle) * Math.PI / 180;
        double nX = 600 + distance * Math.sin(radAngle);
        double nY = 200 + distance * Math.cos(radAngle);
        return new Point2D(nX,nY);
    }


    private int getNrOfLifts(){
        return getGondelValue() + getSchleppliftValue() + getSesselliftValue();
    }

    private void setCenteredText(Text text, double cx, double cy) {
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
        text.setWrappingWidth(width);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setY(cy);
        text.setX(cx - (width / 2.0));
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


    private double valueToAngle(double value, double minValue, double maxValue) {
        return 360.00 * ((value - minValue) / (maxValue - minValue));
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
