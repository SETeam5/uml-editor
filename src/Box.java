import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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
	Model model;
	Section[] sections = new Section[4]; //this needs to stay this way
	//due to a bug with the model being null with previous declaration
	private Double offsetX;
	private Double offsetY;
	int previousx = 0;
	int previousy = 0;
	Integer id;
	RectangleData myboxdata;
	CopyOnWriteArrayList<RectangleTextData> boxtext = new CopyOnWriteArrayList<RectangleTextData>();

	/**
	 * Box constructor
	 * Boxes are initialized with event handlers for mousedowns, click-and-drags, and clicks
	 * @param c - the Controller
	 */
	public Box(Controller c, Model modelin, Integer boxid, RectangleData myboxdatain) {
		controller = c;
		id = boxid;
		model = modelin;
		myboxdata = myboxdatain;
		sections[0] = new Section(this, "add class name", true, model, 0);
		sections[1] = new Section(this, "add attribute", false, model, 1);
		sections[2] = new Section(this, "add operation", false, model, 2);
		sections[3] = new Section(this, "add miscellaneous", false, model, 3);

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
				if ((x < 0) || (y < 0)) {
					x = previousx;
					y = previousy;
				}
				if (((x + thisBox.getWidth()) > controller.workspace.getWidth())
						|| ((y + thisBox.getHeight()) > controller.workspace.getHeight())) {
					x = previousx;
					y = previousy;
				}
				previousx = Math.floorDiv((int) x, 20) * 20;
				previousy = Math.floorDiv((int) y, 20) * 20;
				// round to nearest 20 px
				relocate(Math.floorDiv((int) x, 20) * 20, Math.floorDiv((int) y, 20) * 20);
				myboxdata.ResetRectangleData(previousx, previousy, model, id, myboxdata.boxtextdata);
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
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				controller.hideGrid();
			}
		});
		
		//Handles selecting the box or adding relation lines on click
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (controller.isAddingRelation()) {
					controller.endCurrentRelation(thisBox);
				} else if (thisBox != controller.getSelectedBox()) {
					controller.deselectBox();
					controller.selectBox(thisBox);
				}
				// consume keeps event from interacting with elements below
				event.consume();
			}
		});

		// created box starts selected
		controller.selectBox(this);
		model.getRealBoxMap().put(id, this);
		
	}
	
	/**
	 * Called when the box is deselected
	 * Handles setting the state of each section
	 */
	public void deselect() {
		for (int i = 3; i >= 1; --i) {
			sections[i].deselect();
			if (sections[i].isEmpty()) {
				getChildren().remove(sections[i]);
			} 
		}
	}
	
	/**
	 * Called when the box is selected
	 * Handles setting the state of each section
	 */
	public void select() {
		requestFocus();
		for (Section s : sections) {
			s.select();
			if (getChildren().indexOf(s) == -1) {
				getChildren().add(s);
			}
		}
	}

	/** everybox to the right of deleted box gets copied/moved to the left one spot
	* and it's old spot deleted for both real and logical boxes. for this method and
	* elsewhere in other areas of the model code there is always a 1, 2, 3 ...
	* ordering of the boxes,lines,etc. in the map in their creation order.
	* if there are 5 boxes on the screen they will always be 1, 2, 3, 4, 5
	* @ param boxid - the real and logical box's id
	*/
	public void DeleteRectangleData(Integer boxid) {
		model.getBoxMap().remove(boxid);
		model.getRealBoxMap().remove(boxid);
		System.out.println(model.getBoxMap());
		System.out.println(model.getRealBoxMap());
		for (int i = boxid + 1; i < model.getBoxMap().size() + 2; i++) {

			RectangleData boxi = model.getBoxMap().get(i);
			boxi.ResetRectangleData(boxi.xposition, boxi.yposition, model, i - 1, myboxdata.boxtextdata);
			model.getBoxMap().remove(i);

			Box realboxi = model.getRealBoxMap().get(i);
			realboxi.ResetRealRectangleData(previousx, previousy, boxtext, model, i - 1);
			model.getRealBoxMap().remove(i);
		}

	}
	/**
	 * called whenever the real rectangle data needs to be reset, called mostly for
	 * deletion of boxes and subsequent id shifting
	 * @param previousxin
	 * @param previousyin
	 * @param boxtextin
	 * @param modelin
	 * @param idin
	 */
	public void ResetRealRectangleData(int previousxin, int previousyin, 
			CopyOnWriteArrayList<RectangleTextData> boxtext2, Model modelin, int idin) {
		this.previousx = previousxin;
		this.previousy = previousyin;
		this.boxtext = boxtext2;
		this.id = idin;
		model.getRealBoxMap().put(idin, this);
	}
}