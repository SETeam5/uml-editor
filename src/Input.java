import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * Input Class
 * Editable TextFields used for adding text to Box Sections and to Relations
 * -Input boxes are not visible upon losing focus
 * -Input boxes transfer their contents to a TextLine upon losing focus
 */
public class Input extends TextField {

	Section section;
	Relation relation;

	/**
	 * Input constructor (used in Sections)
	 * @param p - Parent Section
	 * @param s - Placeholder string for input; this could be the Section prompt or previous input from a user
	 */
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

	/**
	 * Input constructor (used for Relations)
	 * @param r - Parent Relation 
	 */
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
	
	/**
	 * Event handler for when the 'enter' key is pressed inside the Input
	 * Focus is lost (set to false)
	 */
	public void loseFocus() {
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					setFocused(false);
				}
			});
	}

}
