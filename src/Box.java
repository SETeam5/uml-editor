import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Box extends VBox {
	Controller controller;
	Section name;
	Section attrib;
	Section ops;
	Section extra;
	Double coordX;
	Double coordY;

	public Box(Controller c) {
		controller = c;
		
		getStyleClass().add("box");
		Box thisBox = this;
		setPrefHeight(241);
		setPrefWidth(141);
		
		name = new Section(this, "add class name");
		attrib = new Section(this, "add attribute");
		ops = new Section(this, "add operation");
		extra = new Section(this, "add miscellaneous");
		getChildren().addAll(name, attrib, ops, extra);
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//css style to show grid while rectangle is being dragged - should be call to controller like below on setOnMouseReleased
				controller.workspace.getStyleClass().remove("noGrid");
				controller.workspace.getStyleClass().add("grid");
				double x = event.getSceneX() - coordX;
				double y = event.getSceneY() - coordY;
				//round to nearest 20 px
				relocate(Math.floorDiv((int) x, 20) * 20, Math.floorDiv((int) y, 20) * 20);
			}
		});
		
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				coordX = event.getSceneX() - getLayoutX();
				coordY = event.getSceneY() - getLayoutY();
			}
		});
		
		setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				controller.showGrid();			
			}
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {				
				controller.selectBox(thisBox);
				if (controller.isAddingRelation()) {
					controller.endCurrentRelation();
				}
				//consume keeps event from interacting with elements below
				event.consume();
			}
		});
	}	
	
}
