
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;

/**
 * Initial work for future iteration - not implemented
 */
public class ScrollBars extends ScrollPane{
	
	Controller controller;

	public ScrollBars(Controller c) {
		
		this.controller = c;
		getStyleClass().add("scroll-pane");
		setContent(c.workspace);
		
	}

}