

package assignment5;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application{
	public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		GridPane world = new GridPane();
		Critter.displayWorld(world);
		
	}
}
