import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class Input extends TextField {

	Section section;
	Relation relation;

	public Input(Section p, String s) {

		section = p;
		
		// set input text to previous value unless it is the prompt text
		if (!s.equals(p.prompt)) {
			setText(s);
		}

		loseFocus();

		// on lost focus, process input
		focusedProperty().addListener((observable, oldvalue, newvalue) -> {
			if (newvalue == false) {
				section.processInput(this);
			}
		});

	}

	public Input(Relation r) {
		relation = r;
		
		loseFocus();

		// on lost focus, process input
		focusedProperty().addListener((observable, oldvalue, newvalue) -> {
			if (newvalue == false) {
				relation.processInput();
			}
		});
	}
	
	// hit enter in input, lose focus
	public void loseFocus() {
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					setFocused(false);
				}
			});
	}

}
