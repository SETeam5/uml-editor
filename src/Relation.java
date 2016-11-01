
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;

public class Relation extends Line {

	Box startBox = null;
	Box endBox = null;
	int startboxid;
	int endboxid;
	Controller controller;
	Relation relation;
	Integer id;
	Model model;
	TextLine text;
	Input input;
	ArrayList<String> linetext = new ArrayList<String>();
	//relation types
	final int GENERALIZATION = 0;
	final int AGGREGATION = 1;
	//...
	ImageView arrowHead;

	public Relation(Box startBox, Controller c, Model model) {
		this.controller = c;
		this.model = model;
		this.startBox = startBox;
		startXProperty().bind(startBox.layoutXProperty().add(startBox.widthProperty().divide(2)));
		startYProperty().bind(startBox.layoutYProperty().add(startBox.heightProperty().divide(2)));
		final Relation relation = this;

		getStyleClass().add("relation");

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				controller.selectRelation(relation);
				// consume keeps event from interacting with elements below
				event.consume();
			}
		});
		
		text = new TextLine("add text here",this);
		arrowHead = new ImageView();
	}

	public void setEndPoint(Box endBoxin) {
		endBox = endBoxin;
		endboxid = endBox.id;
		startboxid = startBox.id;
		endXProperty().bind(endBox.layoutXProperty().add(endBox.widthProperty().divide(2)));
		endYProperty().bind(endBox.layoutYProperty().add(endBox.heightProperty().divide(2)));
		addText();
		setRelationType(GENERALIZATION);
		controller.workspace.getChildren().add(arrowHead);
		update();
	}

	public Box getStartingBox() {
		return startBox;
	}

	public Box getEndingBox() {
		return endBox;
	}
	
	public void addText() {
		controller.workspace.getChildren().add(text);
		text.layoutXProperty().bind(startXProperty().add(endXProperty()).divide(2));
		text.layoutYProperty().bind(startYProperty().add(endYProperty()).divide(2));
	}
	
	public void showText() {
		if (!controller.workspace.getChildren().contains(text)) {
			controller.workspace.getChildren().add(text);
		}
	}
	
	public void hideText() {
		if (text.getText().equals("add text here") || text.getText().trim().equals("")) {
			text.setText("add text here");
			controller.workspace.getChildren().remove(text);
		}
	}
	
	public void addInput(String s) {
		input.setText(s);
		controller.workspace.getChildren().remove(text);
		controller.workspace.getChildren().add(input);
		
		input.layoutXProperty().bind(startXProperty().add(endXProperty()).divide(2));
		input.layoutYProperty().bind(startYProperty().add(endYProperty().subtract(input.heightProperty().multiply(2))).divide(2));

		input.requestFocus();
	}
	
	public void processInput() {
		text.setText(input.getText());
		controller.workspace.getChildren().remove(input);
		showText();
		hideText();
	}
	
	public TextLine getText() {
		return text;
	}
	
	public ImageView getArrowHead() {
		return arrowHead;
	}
	
	//currently arrow heads can become mispositioned when boxes collapse and expand
	//currently dragging an attached box or clicking in the workspace, sets the arrow heads to the correct position
	public void update() {
		updateArrowRotation();
		updateArrowPosition();
	}
	
	public void updateArrowRotation() {
		arrowHead.setRotate(getLineAngle());
	}
	
	public void updateArrowPosition() {
		double angle = getLineAngle();
		double halfBoxWidth = endBox.getWidth() / 2;
		double halfBoxHeight = endBox.getHeight() / 2;
		
		//angle where line intersects corner of box
		double criticalAngle = Math.toDegrees(Math.atan(endBox.getHeight() / endBox.getWidth()));
		
		double xOffset = 0;
		double yOffset = 0;
		
		//calculate where line intersects outside of box
		if (angle >= 0 && angle < criticalAngle) {
			xOffset = -halfBoxWidth;
			yOffset = -Math.abs(Math.tan(Math.toRadians(angle))) * halfBoxWidth;
		} else if (angle >= criticalAngle && angle < 90) {
			xOffset = -halfBoxHeight / Math.abs(Math.tan(Math.toRadians(angle)));
			yOffset = -halfBoxHeight;
		} else if (angle >= 90 && angle < (180 - criticalAngle)) {
			xOffset = halfBoxHeight / Math.abs(Math.tan(Math.toRadians(angle)));
			yOffset = -halfBoxHeight; 
		} else if ((angle >= (180 - criticalAngle)) && angle <= 180) {
			xOffset = halfBoxWidth;
			yOffset = -Math.abs(Math.tan(Math.toRadians(angle))) * halfBoxWidth;
		} else if (angle >= -180 && angle < (-180 + criticalAngle)) {
			xOffset = halfBoxWidth;
			yOffset = Math.abs(Math.tan(Math.toRadians(angle))) * halfBoxWidth;
		} else if (angle >= (-180 + criticalAngle) && angle < -90) {
			xOffset = halfBoxHeight / Math.abs(Math.tan(Math.toRadians(angle)));
			yOffset = halfBoxHeight;
		} else if (angle >= -90 && angle < -criticalAngle) {
			xOffset = -halfBoxHeight / Math.abs(Math.tan(Math.toRadians(angle)));
			yOffset = halfBoxHeight;
		} else if (angle >= -criticalAngle && angle < 0) {
			xOffset = -halfBoxWidth;
			yOffset = Math.abs(Math.tan(Math.toRadians(angle))) * halfBoxWidth;
		}
		
		arrowHead.setX(getEndX() - (arrowHead.getImage().getWidth() / 2) + xOffset);
		arrowHead.setY(getEndY() - (arrowHead.getImage().getHeight() /2) + yOffset);
	}
	
	public void setRelationType(int relationType) {
		if (relationType == GENERALIZATION) {
			arrowHead.setImage(new Image("/generalization.png", false));
		}
		//...
	}
	
	public void remove() {
		controller.workspace.getChildren().remove(this);
		controller.workspace.getChildren().remove(text);
		controller.workspace.getChildren().remove(arrowHead);
	}
	
	//angle between line and x-axis in degrees
	//may seem upside down since (0,0) is in top left of pane
	private double getLineAngle() {
		//angle from startingBox to endingBox
		double dx = getEndX() - getStartX();
		double dy = getEndY() - getStartY();
		double angle = Math.toDegrees(Math.atan(dy / dx));
		
		//adjusting degrees to range from [-180, 180], instead of [-90, 90]
		if (dx < 0) {
			if (dy < 0) {
				angle -= 180;
			} else {
				angle += 180;
			}
		}
		return angle;
	}

	// everybox to the right of deleted box gets copied/moved to the left one
	// spot
	// and it's old spot deleted for both real and logical boxes
	public void DeleteLineData(Integer lineid) {
		model.getLineMap().remove(lineid);
		model.getRealLineMap().remove(lineid);
		for (int i = lineid + 1; i < model.getLineMap().size() + 2; i++) {
			LineData linei = model.getLineMap().get(i);
			linei.ResetLineData(linei.startboxid, linei.endboxid, linei.linetextdata, model,i - 1);
			model.getLineMap().remove(i);

			Relation reallinei = model.getRealLineMap().get(i);
			reallinei.ResetRealLineData(startboxid, endboxid, linetext, model, i - 1);
			model.getRealLineMap().remove(i);
		}
	}

	public void ResetRealLineData(int startboxidin, int endboxidin, ArrayList<String> linetextin, Model modelin, int idin) {
		this.startboxid = startboxidin;
		this.endboxid = endboxidin;
		this.linetext = linetextin;
		this.id = idin;
		model.getRealLineMap().put(id, this);
	}
	
	public void SetId(int idin) {
		this.id = idin;
		model.getRealLineMap().put(id, this);
	}
	
}
