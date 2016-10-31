import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WorkSpace extends Pane{
	
	Controller controller;
	
	public WorkSpace(Controller c) {
		
		controller = c;
		WorkSpace workspace = this;
		
		getStyleClass().add("noGrid");
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				workspace.requestFocus();
				controller.deselectRelation();
				controller.deselectBox();
				//Temporary: clicking in workspace will fix all mispositioned arrowheads
				controller.updateRelations();
			}
		});
	}

}
