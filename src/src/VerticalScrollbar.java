
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;

public class VerticalScrollbar extends ScrollBar{
	
	Controller controller;

	public VerticalScrollbar(Controller c) {
		
		this.controller = c;
		this.setOrientation(Orientation.VERTICAL);
		getStyleClass().add("scroll-bar");
		
		valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	controller.workspace.toBack();
            	controller.workspace.setLayoutY(-new_val.doubleValue()*10);
            }
        });
		
	}

}