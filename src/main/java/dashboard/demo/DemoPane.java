package dashboard.demo;

import dashboard.DashboardControl;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.util.converter.NumberStringConverter;

/**
 *
 */
public class DemoPane extends BorderPane {

    private final dashboard.demo.PresentationModel pm;

    // declare the custom control
    private DashboardControl dashboardControl;

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

        dashboardControl = new DashboardControl();

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

        setCenter(dashboardControl);
        setRight(controlPane);
    }

    private void setupBindings() {
        //bindings for the "demo controls"
        valueGondelLabel.textProperty().bindBidirectional(pm.valueGondelProperty(), new NumberStringConverter());
        valueSchleppliftLabel.textProperty().bindBidirectional(pm.valueSchleppliftProperty(), new NumberStringConverter());
        valueSesselliftLabel.textProperty().bindBidirectional(pm.valueSesselliftProperty(), new NumberStringConverter());

        //bindings for the Custom Control
        dashboardControl.gondelValueProperty().bindBidirectional(pm.valueGondelProperty());
        dashboardControl.schleppliftValueProperty().bindBidirectional(pm.valueSchleppliftProperty());
        dashboardControl.sesselliftValueProperty().bindBidirectional(pm.valueSesselliftProperty());
        dashboardControl.locationValueProperty().bindBidirectional(pm.locationProperty());
        dashboardControl.cantonValueProperty().bindBidirectional(pm.cantonProperty());
        dashboardControl.snowheightValueProperty().bindBidirectional(pm.snowHeightProperty(), new NumberStringConverter());
        dashboardControl.openLiftValueProperty().bind(pm.openLiftsProperty());
        dashboardControl.imageUrlValueProperty().bind(pm.imageUrlProperty());

    }

}
