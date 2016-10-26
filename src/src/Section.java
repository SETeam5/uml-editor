
import javafx.scene.layout.VBox;

public class Section extends VBox {
	Box parent;
	String prompt;

	public Section(Box b, String s) {
		parent = b;
		prompt = s;
		
		setMinHeight(60);
		getStyleClass().add("section");
	}

	//add an editable input box where the textline was
	public void addInput(String s, TextLine t) {
		Input input = new Input(this, s);

		if (t == null) {
			getChildren().add(input);
		} 
		else {
			int index = getChildren().indexOf(t);
			getChildren().set(index, input);
			getChildren().remove(t);
		}
		input.requestFocus();
	}

	public void processInput(Input inputBox) {
		// if input is blank, use prompt for textline
		String str = inputBox.getText().trim().equals("") ? prompt : inputBox.getText();
		TextLine text = new TextLine(str, this);
		int index = getChildren().indexOf(inputBox);
		getChildren().set(index, text);
		getChildren().remove(inputBox);
		
		//add another input if prev input wasn't blank and we're at the end of the list
		if (!str.equals(prompt) && index == getChildren().size() - 1) {
			addInput(prompt, null);
		}

	}
	
	public void deselect() {
		getChildren().remove(getChildren().size() - 1);
	}
	
	public boolean isEmpty() {
		return (getChildren().size() == 0);
	}
	
	public void select() {
		TextLine placeholder = new TextLine(prompt, this);
		getChildren().add(placeholder);
	}

}

