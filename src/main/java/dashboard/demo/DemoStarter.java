package dashboard.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 *
 */
public class DemoStarter extends Application {

	@Override
	public void start(Stage primaryStage) {
        PresentationModel rootPM    = new PresentationModel();
        Region rootPanel = new DemoPane(rootPM);

        Scene scene = new Scene(rootPanel);

        primaryStage.titleProperty().bind(rootPM.locationProperty());
        primaryStage.setScene(scene);

        primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
