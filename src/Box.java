import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Box extends VBox {
	Controller controller;
	private Section[] sections = { new Section(this, "add class name", true), new Section(this, "add attribute", false),new Section(this, "add operation", false), new Section(this, "add miscellaneous", false) };
	private Double offsetX;
	private Double offsetY;
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
		Box thisBox = this;
		setPrefWidth(141);

		getChildren().addAll(sections[0], sections[1], sections[2], sections[3]);

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
				myboxdata.ResetRectangleData(141, 241, previousx, previousy, boxtext, model, id);
				controller.updateRelations();
			}
		});

		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				offsetX = event.getSceneX() - getLayoutX();
				offsetY = event.getSceneY() - getLayoutY();
			}
		});

		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				controller.hideGrid();
			}
		});

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

	public void deselect() {
		boolean okayToHide = true;
		for (int i = 3; i >= 1; --i) {
			sections[i].deselect();
			if (okayToHide && sections[i].isEmpty()) {
				getChildren().remove(sections[i]);
			} else {
				okayToHide = false;
			}
		}
	}

	public void select() {
		requestFocus();
		for (Section s : sections) {
			s.select();
			if (getChildren().indexOf(s) == -1) {
				getChildren().add(s);
			}
		}
	}

	// everybox to the right of deleted box gets copied/moved to the left one spot
	// and it's old spot deleted for both real and logical boxes. for this method and
	// elsewhere in other areas of the model code I like to always keep a 1, 2,3
	// etc. ordering of the boxes,lines,etc. in the map in their creation order.
	// if there are 5 boxes on the screen they will always be 1, 2, 3, 4, 5
	public void DeleteRectangleData(Integer boxid) {
		model.getBoxMap().remove(boxid);
		model.getRealBoxMap().remove(boxid);
		for (int i = boxid + 1; i < model.getBoxMap().size() + 2; i++) {

			RectangleData boxi = model.getBoxMap().get(i);
			boxi.ResetRectangleData(boxi.width, boxi.height, boxi.xposition, boxi.yposition, boxi.boxtextdata, model,i - 1);
			model.getBoxMap().remove(i);

			Box realboxi = model.getRealBoxMap().get(i);
			realboxi.ResetRealRectangleData(previousx, previousy, boxtext, model, i - 1);
			model.getRealBoxMap().remove(i);
		}

	}

	public void ResetRealRectangleData(int previousxin, int previousyin, ArrayList<String> boxtextin, Model modelin,
			int idin) {
		this.previousx = previousxin;
		this.previousy = previousyin;
		this.boxtext = boxtextin;
		this.id = idin;
		model.getRealBoxMap().put(idin, this);
	}
}