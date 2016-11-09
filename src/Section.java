import java.util.ArrayList;

import javafx.scene.layout.VBox;

/**
 * Section Class
 * Every class box is composed of 4 sections, each holds a series of TextLines
 * -When the parent box is selected, the section is expanded and there is a placeholder for new text
 * -When the parent box is deselected, the section only contains input from the user, no placeholders
 * -Title sections behave differently - only one TextLine is ever available, whether the parent box is selected or not
 */
public class Section extends VBox {
	Box parent;
	String prompt;
	boolean isTitle;
	Model model;
	int sectionnumber;

	/**
	* Section constructor
	* @param b - Parent box
	* @param s - Placeholder prompt that goes in an empty TextLine for this section
	* @param t - Boolean value denoting if this section is a Title section
	*/
	public Section(Box b, String s, boolean t, Model modelin, int sectionnumberin) {
		parent = b;
		prompt = s;
		isTitle = t;
		model = modelin;
		sectionnumber = sectionnumberin;
		
		setMinHeight(30);
		getStyleClass().add("section");
	}

	/**
	 * Adds editable Input that replaces TextLine
	 * After Input creation, the input has focus and the user can add/edit text
	 * @param s - String that goes into Input
	 * @param t - Textline being replaced
	 */
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

	/**
	 * Creates TextLine from Input value after editing is complete
	 * @param inputBox - the Input that was being edited
	 */
	public void processInput(Input inputBox) {
		// if input is blank, use prompt for textline
		if (getChildren().contains(inputBox)) {
			String str = inputBox.getText().trim().equals("") ? prompt : inputBox.getText();
			TextLine text = new TextLine(str, this);
			int index = getChildren().indexOf(inputBox);
			getChildren().set(index, text);
			RectangleTextData rtd = new RectangleTextData(str,index,sectionnumber,parent.myboxdata);
			parent.boxtext.add(rtd);
			parent.ResetRealRectangleData(parent.previousx, parent.previousy, parent.boxtext, model, parent.id);
			model.getBoxMap().get(parent.id).ResetRectangleData(parent.previousx, parent.previousy, model, parent.id, parent.boxtext);
			getChildren().remove(inputBox);
			
			//add another input if prev input wasn't blank and we're at the end of the list
			if (!str.equals(prompt) && index == getChildren().size() - 1 && !isTitle) {
				addInput(prompt, null);
			}
		}
	}
	
	/**
	 * Removes the placeholder TextLines
	 */
	public void deselect() {
		getChildren().remove(getChildren().size() - 1);
	}
	
	/**
	 * Checks if the section has no children elements
	 * @return boolean value whether the section is empty or not
	 */
	public boolean isEmpty() {
		return (getChildren().size() == 0);
	}
	
	/**
	 * Adds the placeholder TextLines
	 */
	public void select() {
		if (!isTitle || (isTitle && isEmpty()) ) {
			TextLine placeholder = new TextLine(prompt, this);
			getChildren().add(placeholder);
		}
	}
		
}