import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;

public class Relation extends Line {

	private Box startBox = null;
	Controller controller;
	Relation relation;

	public Relation(Box startBox, Controller c) {
		this.controller = c;
		this.startBox = startBox;
		double startX = startBox.getLayoutX() + (startBox.getWidth() / 2);
		double startY = startBox.getLayoutY() + (startBox.getHeight() / 2);
		setStartX(((int)(startX / 20)) * 20);
		setStartY(((int)(startY / 20)) * 20);
		final Relation relation = this;
		
		getStyleClass().add("relation");
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				controller.selectRelation(relation);
				//consume keeps event from interacting with elements below
				event.consume();
			}
		});
	}

	public void setEndPoint(Box endBox) {
		double endX = endBox.getLayoutX() + (endBox.getWidth() / 2);
		double endY = endBox.getLayoutY() + (endBox.getHeight() / 2);
		setEndX(((int)(endX / 20)) * 20);
		setEndY(((int)(endY / 20)) * 20);
	}

	public Box getStartingBox() {
		return startBox;
	}
}
