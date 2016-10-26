
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class TextLine extends Text {

	Section parent;

	public TextLine(String s, Section p) {
		
		super(s);
		parent = p;
		final TextLine thisLine = this;

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				parent.addInput(thisLine.getText(), thisLine);
			}
		});

	}

}