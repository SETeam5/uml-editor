import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Box extends VBox {
	Controller controller;
	Section name;
	Section attrib;
	Section ops;
	Section extra;

	public Box(Controller c) {
		super();
		
		controller = c;
		
		//css ID, should be changed to class
		getStyleClass().add("rect");
		Box thisBox = this;
		setPrefHeight(241);
		setPrefWidth(141);
		
		name = new Section(this, "Class name");
		attrib = new Section(this, "Attributes");
		ops = new Section(this, "Operations");
		extra = new Section(this, "Miscellaneous");
		
		getChildren().addAll(name, attrib, ops, extra);
		
		//box corner jumps to cursor on drag, need to fix that
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//css style to show grid while rectangle is being dragged - should be call to controller like below on setOnMouseReleased
				controller.workspace.getStyleClass().remove("noGrid");
				controller.workspace.getStyleClass().add("grid");
				double x = event.getX();
				double y = event.getY();
				//round to nearest 20 px
				setTranslateX(Math.floorDiv((int) x, 20) * 20);
				setTranslateY(Math.floorDiv((int) y, 20) * 20);
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
