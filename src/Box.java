
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Box extends VBox {
	Controller controller;
	Section[] sections = new Section[4];
	Double coordX;
	Double coordY;
	int previousx = 0;
	int previousy = 0;
	Integer id;
	RectangleData myboxdata;
	Model model;
	ArrayList<String> boxtext = new ArrayList<String>();

	public Box(Controller c, Model modelin, Integer boxid, RectangleData myboxdatain) {
		controller = c;
		id = boxid;
		model = modelin;
		myboxdata = myboxdatain;
		
		getStyleClass().add("box");
		final Box thisBox = this;
		setPrefWidth(141);
		setPrefHeight(241);
		
		sections[0] = new Section(this, "add class name", true);
		sections[1] = new Section(this, "add attribute", false);
		sections[2] = new Section(this, "add operation", false);
		sections[3] = new Section(this, "add miscellaneous", false);
		
		getChildren().addAll(sections[0], sections[1], sections[2], sections[3]);
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//css style to show grid while rectangle is being dragged - should be call to controller like below on setOnMouseReleased
				controller.workspace.getStyleClass().remove("noGrid");
				controller.workspace.getStyleClass().add("grid");
				double x = event.getSceneX() - coordX;
				double y = event.getSceneY() - coordY;
				if((x < 0) || (y < 0)){
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
				myboxdata.ResetRectangleData(141, 241, previousx, previousy, boxtext, model, id);
			    controller.updateRelations();
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
		model.getRealBoxMap().put(id, this);
		
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
		requestFocus();
		for (Section s : sections){
			s.select();
			if (getChildren().indexOf(s) == -1) {
				getChildren().add(s);
			}
		}
	}

	//everybox to the right of deleted box gets copied/moved to the left one spot 
	//and it's old spot deleted for both real and logical boxes
	public void DeleteRectangleData(Integer boxid) {
		model.getBoxMap().remove(boxid);
		model.getRealBoxMap().remove(boxid);
		for(int i = boxid + 1; i < model.getBoxMap().size() + 2; i++){
			RectangleData boxi = model.getBoxMap().get(i);
			boxi.ResetRectangleData(boxi.width, boxi.height, boxi.xposition, boxi.yposition, boxi.boxtextdata, model, i - 1);
			System.out.println(model.getBoxMap().get(i));
			model.getBoxMap().remove(i);
			
			Box realboxi = model.getRealBoxMap().get(i);
			realboxi.ResetRealRectangleData(previousx,previousy,boxtext,model,i-1);
			model.getRealBoxMap().remove(i);
		}
		
		System.out.println(model.getBoxMap());
	}
	
	public void ResetRealRectangleData(int previousxin, int previousyin, ArrayList<String> boxtextin, Model modelin, int idin) {
		this.previousx = previousxin;
		this.previousy = previousyin;
		this.boxtext = boxtextin;
		this.id = idin;
		model.getRealBoxMap().put(idin, this);
	}
	
}
