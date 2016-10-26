
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("UML Editor - fiVe");
		
		Model model = new Model(primaryStage);
		Controller controller = new Controller(model);

		Scene scene = new Scene(controller.ui, 1200, 800);
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}