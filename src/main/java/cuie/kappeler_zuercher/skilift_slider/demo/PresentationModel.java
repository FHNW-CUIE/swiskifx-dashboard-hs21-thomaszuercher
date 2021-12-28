package cuie.kappeler_zuercher.skilift_slider.demo;

import javafx.beans.property.*;

/**
 *
 */
public class PresentationModel {
    // all the properties waiting for being displayed
    private final StringProperty location = new SimpleStringProperty("Crans Montana");
    private final StringProperty canton = new SimpleStringProperty("Waadt und Wallis");
    private final IntegerProperty snowHeight = new SimpleIntegerProperty(0);
    private final IntegerProperty valueGondel = new SimpleIntegerProperty(10);
    private final IntegerProperty valueSchlepplift = new SimpleIntegerProperty(50);
    private final IntegerProperty valueSessellift = new SimpleIntegerProperty(90);
    private final IntegerProperty min = new SimpleIntegerProperty(0);
    private final IntegerProperty max = new SimpleIntegerProperty(100);


    // all getters and setters (generated via "Code -> Generate -> Getter and Setter)

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public int getValueGondel() {
        return valueGondel.get();
    }

    public IntegerProperty valueGondelProperty() {
        return valueGondel;
    }

    public void setValueGondel(int valueGondel) {
        this.valueGondel.set(valueGondel);
    }

    public int getMin() {
        return min.get();
    }

    public IntegerProperty minProperty() {
        return min;
    }

    public void setMin(int min) {
        this.min.set(min);
    }

    public int getMax() {
        return max.get();
    }

    public IntegerProperty maxProperty() {
        return max;
    }

    public void setMax(int max) {
        this.max.set(max);
    }

    public int getValueSchlepplift() {
        return valueSchlepplift.get();
    }

    public IntegerProperty valueSchleppliftProperty() {
        return valueSchlepplift;
    }

    public void setValueSchlepplift(int valueSchlepplift) {
        this.valueSchlepplift.set(valueSchlepplift);
    }

    public int getValueSessellift() {
        return valueSessellift.get();
    }

    public IntegerProperty valueSesselliftProperty() {
        return valueSessellift;
    }

    public void setValueSessellift(int valueSessellift) {
        this.valueSessellift.set(valueSessellift);
    }

    public String getCanton() {
        return canton.get();
    }

    public StringProperty cantonProperty() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton.set(canton);
    }

    public int getSnowHeight() {
        return snowHeight.get();
    }

    public IntegerProperty snowHeightProperty() {
        return snowHeight;
    }

    public void setSnowHeight(int snowHeight) {
        this.snowHeight.set(snowHeight);
    }
}
