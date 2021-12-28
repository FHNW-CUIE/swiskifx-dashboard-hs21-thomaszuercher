package cuie.kappeler_zuercher.skilift_slider.demo;

import cuie.kappeler_zuercher.skilift_slider.SkiliftControl;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.util.converter.NumberStringConverter;

/**
 *
 */
public class DemoPane extends BorderPane {

    private final PresentationModel pm;

    // declare the custom control
    private SkiliftControl skiliftControl;

    // all controls you need to show the features of the custom control
    private Label titleLabel;
    private Label valueGondelTitleLabel;
    private Label valueSchleppliftTitleLabel;
    private Label valueSesselliftTitleLabel;
    private Label valueGondelLabel;
    private Label valueSchleppliftLabel;
    private Label valueSesselliftLabel;

    public DemoPane(PresentationModel pm) {
        this.pm = pm;
        initializeControls();
        layoutControls();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        skiliftControl = new SkiliftControl();

        titleLabel = new Label("Dashboard Properties");
        valueGondelTitleLabel = new Label("Anzahl Gondeln: ");
        valueGondelLabel = new Label();
        valueSchleppliftTitleLabel = new Label("Anzahl Schlepplifte: ");
        valueSchleppliftLabel = new Label();
        valueSesselliftTitleLabel = new Label("Anzahl Sessellifte: ");
        valueSesselliftLabel = new Label();
    }

    private void layoutControls() {
        VBox controlPane = new VBox(titleLabel, valueGondelTitleLabel, valueGondelLabel, valueSchleppliftTitleLabel,
                valueSchleppliftLabel, valueSesselliftTitleLabel, valueSesselliftLabel);
        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(skiliftControl);
        setRight(controlPane);
    }

    private void setupBindings() {
        //bindings for the "demo controls"
        valueGondelLabel.textProperty().bindBidirectional(pm.valueGondelProperty(), new NumberStringConverter());
        valueSchleppliftLabel.textProperty().bindBidirectional(pm.valueSchleppliftProperty(), new NumberStringConverter());
        valueSesselliftLabel.textProperty().bindBidirectional(pm.valueSesselliftProperty(), new NumberStringConverter());

        //bindings for the Custom Control
        skiliftControl.gondelValueProperty().bindBidirectional(pm.valueGondelProperty());
        skiliftControl.schleppliftValueProperty().bindBidirectional(pm.valueSchleppliftProperty());
        skiliftControl.sesselliftValueProperty().bindBidirectional(pm.valueSesselliftProperty());
        skiliftControl.locationValueProperty().bindBidirectional(pm.locationProperty());
        skiliftControl.cantonValueProperty().bindBidirectional(pm.cantonProperty());
        skiliftControl.snowheightValueProperty().bindBidirectional(pm.snowHeightProperty(), new NumberStringConverter());
    }

}
