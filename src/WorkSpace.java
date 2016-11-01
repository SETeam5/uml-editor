
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WorkSpace extends Pane{
	
	Controller controller;
	
	public WorkSpace(Controller c) {
		
		controller = c;
		WorkSpace workspace = this;
		getStyleClass().add("noGrid");
		setMinWidth(2000);
		setMinHeight(2000);
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
			    controller.deselectBox();
				controller.deselectRelation();
				workspace.requestFocus();
				//Temporary: clicking in workspace will fix all mispositioned arrowheads
				controller.updateRelations();
			}
		});
	}

}