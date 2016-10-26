
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Box extends VBox {
	Controller controller;
	Section[] sections = new Section[4];
	Double coordX;
	Double coordY;
	Integer previousx = 0;
	Integer previousy = 0;
	Integer id;

	public Box(Controller c, Model model) {
		controller = c;
		
		getStyleClass().add("box");
		final Box thisBox = this;
		setPrefWidth(141);
		setPrefHeight(241);
		
		//RectangleData boxdata = new RectangleData(141,241,0,20,,model,id);
		
		sections[0] = new Section(this, "add class name");
		sections[1] = new Section(this, "add attribute");
		sections[2] = new Section(this, "add operation");
		sections[3] = new Section(this, "add miscellaneous");
		
		getChildren().addAll(sections[0], sections[1], sections[2], sections[3]);
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//css style to show grid while rectangle is being dragged - should be call to controller like below on setOnMouseReleased
				controller.workspace.getStyleClass().remove("noGrid");
				controller.workspace.getStyleClass().add("grid");
				double x = event.getSceneX() - coordX;
				double y = event.getSceneY() - coordY;
				if((x < 0) || (y < 20)){
					x = previousx;
					y = previousy;
				}
				if(((x + thisBox.getWidth()) > controller.workspace.getWidth()) || ((y + thisBox.getHeight()) > controller.workspace.getHeight())){
					x = previousx;
					y = previousy;
				}
				previousx = Math.floorDiv((int) x, 20) * 20;
				previousy = Math.floorDiv((int) y, 20) * 20;
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
	
	public void deselect() {
		boolean okayToHide = true;
		for (int i = 3; i > -1; --i){
			sections[i].deselect();
			if (okayToHide && sections[i].isEmpty()) {
				getChildren().remove(sections[i]);
			}
			else {
				okayToHide = false;
			}
		}
	}
	
	public void select() {
		for (Section s : sections){
			s.select();
			if (getChildren().indexOf(s) == -1) {
				getChildren().add(s);
			}
		}
	}
	
}
