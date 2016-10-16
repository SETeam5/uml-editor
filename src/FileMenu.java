package seproject5;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class FileMenu extends MenuBar {
	
	public FileMenu(){
		
		final Menu menuItem1 = new Menu("File");
		final Menu menuItem2 = new Menu("Edit");
		final Menu menuItem3 = new Menu("Preferences");
		final Menu menuItem4 = new Menu("Help");
		
		getMenus().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
		getStyleClass().add("menu");
	}

}
