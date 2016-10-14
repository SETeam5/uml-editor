import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Section extends VBox{
	Box parent;
	TextField input;
	String prompt;
	
	public Section(Box b, String s){
		parent = b;
		prompt = s;
		
		Section thisSection = this;
		TextLine placeholder = new TextLine(prompt, thisSection);
		
		input = new TextField();
		input.setPromptText(prompt);
		
		getChildren().add(placeholder);
		setMinHeight(50);
		
		input.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				thisSection.getChildren().remove(input);
				
			}
		});
		
		input.focusedProperty().addListener((observable, oldvalue, newvalue) -> {
			if (newvalue == false){
				String str = input.getText().trim().equals("") ? prompt : input.getText();
				System.out.println(input.getText());
				TextLine text = new TextLine(str, thisSection);
				thisSection.getChildren().add(text);
				thisSection.getChildren().remove(input);
			}
		});
		
	}
	
	public void addInput(String s, TextLine t) {
		input.setPromptText(s);
		getChildren().remove(t);
		getChildren().add(input);
		input.requestFocus();
	}

}
