import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class TextLine extends Text {

	Section parent;
	Relation relation;

	public TextLine(String s, Section p) {
		super(s);
		
		parent = p;
		TextLine thisLine = this;
		
		//keeps box width from expanding
		setWrappingWidth(141);

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (parent.parent.controller.getSelectedBox() == parent.parent) {
					p.addInput(thisLine.getText(), thisLine);
				}
			}
		});

	}
	
	public TextLine(String s, Relation r) {
		super(s);
		
		parent = null;
		relation = r;
		
		TextLine thisLine = this;
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
					relation.addInput(thisLine.getText());
					event.consume();
			}
		});
	}


}
