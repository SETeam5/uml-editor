
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class FileMenu extends MenuBar {
	
	Model model;
	Controller controller;
	
	public FileMenu(Model passedmodel, Controller c){
		
		this.model = passedmodel;
		this.controller = c;
		
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
				model.openFile(controller);
			}
		});
		
		menuItem1.getItems().addAll(save,open);
	}

}
