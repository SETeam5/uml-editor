package seproject5;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class Box extends Rectangle {
	Controller controller;
	//need these four to fix bug
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private int currentx;
	private int currenty;
	
	public Box(int x, int y, int w, int h, Controller c) {
		super(x, y, w, h);
		
		currentx = x;
		currenty = y;
		
		controller = c;
		
		setFill(null);
		//css ID, should be changed to class
		setId("rect");
		final Box thisBox = this;
		
		setOnMousePressed(new EventHandler<MouseEvent>() {
	       	 
            @Override
            public void handle(MouseEvent event) {
            	 x1 = event.getSceneX();
            	 y1 = event.getSceneY();
            	 x2 = ((Box)(event.getSource())).getX();
            	 y2 = ((Box)(event.getSource())).getY();
            	 //two lines above are basically just the box's origin point
            	 //but have to be written this way otherwise bugs arise
            }
        });
		
		//box corner jumps to cursor on drag, need to fix that
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//css style to show grid while rectangle is being dragged - should be call to controller like below on setOnMouseReleased
				controller.workspace.getStyleClass().remove("noGrid");
				controller.workspace.getStyleClass().add("grid");
				double xoffset = event.getSceneX() - x1;
                double yoffset = event.getSceneY() - y1;
				double x = x2 + xoffset;
				double y = y2 + yoffset;
				//round to nearest 20 px
				((Box)(event.getSource())).setX(((int)(x / 20)) * 20);
                ((Box)(event.getSource())).setY(((int)(y / 20)) * 20);
                currentx = (int)thisBox.getX();
                currenty = (int)thisBox.getY();
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
	
	public int getCurrentX(){
		return currentx;
	}
	
	public int getCurrentY(){
		return currenty;
	}

}
