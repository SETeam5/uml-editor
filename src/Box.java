import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Box Class
 * Contains four Sections, can be connected by Relations
 * -Has two states, selected and unselected, though this is tracked by controller, not individual boxes
 */
public class Box extends VBox {
	Controller controller;
	private Section[] sections = {new Section(this, "add class name", true), new Section(this, "add attribute", false), new Section(this, "add operation", false), new Section(this, "add miscellaneous", false)};
	private Double offsetX;
	private Double offsetY;

	/**
	 * Box constructor
	 * Boxes are initialized with event handlers for mousedowns, click-and-drags, and clicks
	 * @param c - the Controller
	 */
	public Box(Controller c) {
		controller = c;
		
		getStyleClass().add("box");
		Box thisBox = this;
		setPrefWidth(141);
		
		getChildren().addAll(sections[0], sections[1], sections[2], sections[3]);
		
		//Enables click and drag of the boxes for movement
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				controller.showGrid();
				double x = event.getSceneX() - offsetX;
				double y = event.getSceneY() - offsetY;
				//round to nearest 20 px
				relocate(Math.floorDiv((int) x, 20) * 20, Math.floorDiv((int) y, 20) * 20);
				controller.updateRelations();
			}
		});
		
		//Tracks the position of the mouse with relation to the box
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				offsetX = event.getSceneX() - getLayoutX();
				offsetY = event.getSceneY() - getLayoutY();
			}
		});
		
		//Hides the grid when the box is not being moved
		setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				controller.hideGrid();
			}
		});
		
		//Handles selectingthe box or adding relation lines on click
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {				
				
				if (controller.isAddingRelation()) {
					controller.endCurrentRelation(thisBox);
				}
				else if (thisBox != controller.getSelectedBox()) {
					controller.deselectBox();
					controller.selectBox(thisBox);
				}
				//consume keeps event from interacting with elements below
				event.consume();
			}
		});
		
		//created box starts selected
		controller.selectBox(this);
	}
	
	/**
	 * Called when the box is deselected
	 * Handles setting the state of each section
	 */
	public void deselect() {
		boolean okayToHide = true;
		for (int i = 3; i >= 1; --i){
			sections[i].deselect();
			if (okayToHide && sections[i].isEmpty()) {
				getChildren().remove(sections[i]);
			}
			else {
				okayToHide = false;
			}
		}
	}
	
	/**
	 * Called when the box is selected
	 * Handles setting the state of each section
	 */
	public void select() {
		requestFocus();
		for (Section s : sections){
			s.select();
			if (getChildren().indexOf(s) == -1) {
				getChildren().add(s);
			}
		}
	}
}
