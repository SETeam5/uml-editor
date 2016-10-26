
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileMenu extends MenuBar {
	
	Model model;
	
	public FileMenu(Model passedmodel){
		
		this.model = passedmodel;
		
		getStyleClass().add("menu");
		
		final Menu menuItem1 = new Menu("File");
		final Menu menuItem2 = new Menu("Edit");
		final Menu menuItem3 = new Menu("Preferences");
		final Menu menuItem4 = new Menu("Help");
		
		getMenus().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
		
		MenuItem save = new MenuItem("Save");
		MenuItem open = new MenuItem("Open");
	
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				model.saveFile();
			}
		});
		
		open.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				model.openFile();
			}
		});
		
		menuItem1.getItems().addAll(save,open);
	}

}
