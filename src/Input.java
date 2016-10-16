import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class Input extends TextField{
	
	final Section parent;
	
	public Input(Section p, String s) {
		
		parent = p;
		
		Input thisInput = this;
		
		//set input text to previous value unless it is the prompt text
		if (!s.equals(p.prompt)) {
			setText(s);
		}
		
		//hit enter in input, lose focus
		setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				thisInput.setFocused(false);
			}
		});
		
		//on lost focus, process input
		focusedProperty().addListener((observable, oldvalue, newvalue) -> {
			if (newvalue == false){
				parent.processInput(this);
			}
		});
		
	}

}
