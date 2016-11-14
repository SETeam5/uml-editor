import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/**
 * FileMenu Class
 * Currently just a visual placeholder - no functionality
 */
public class FileMenu extends MenuBar {
	
	Controller controller;
	
	public FileMenu(Controller c){
		controller = c;
		final Menu menuItem1 = new Menu("File");
		final Menu menuItem2 = new Menu("Edit");
		final Menu menuItem3 = new Menu("Preferences");
		final Menu menuItem4 = new Menu("Help");
		
		getMenus().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
		getStyleClass().add("menu");
	}

}
