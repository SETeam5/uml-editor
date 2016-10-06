import javafx.scene.shape.*;

public class Relation extends Line {

	private Box startBox = null;

	public Relation(Box startBox) {
		this.startBox = startBox;
		//double startX = startBox.getX() + (startBox.getWidth() / 2);
		//double startY = startBox.getY() + (startBox.getHeight() / 2);
		//setStartX(Math.floorDiv((int) startX, 20) * 20);
		//setStartY(Math.floorDiv((int) startY, 20) * 20);

		setId("relation");
	}

	public void setEndPoint(Box endBox) {
		//double endX = endBox.getX() + (endBox.getWidth() / 2);
		//double endY = endBox.getY() + (endBox.getHeight() / 2);
		//setEndX(Math.floorDiv((int) endX, 20) * 20);
		//setEndY(Math.floorDiv((int) endY, 20) * 20);
	}

	public Box getStartingBox() {
		return startBox;
	}
}
