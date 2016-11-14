import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * WorkSpace Class
 * The WorkSpace holds the editable nodes where the user can interact
 * -Has a CSS overlay that shows/hides grid for placement
 */
public class WorkSpace extends Pane{
	
	Controller controller;
	
	/**
	 * WorkSpace constructor
	 * @param c - the Controller
	 */
	public WorkSpace(Controller c) {
		
		controller = c;
		WorkSpace workspace = this;
		
		getStyleClass().add("noGrid");
		
		//Deselects all objects
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
