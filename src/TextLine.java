import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * TextLine Class
 * Static text field used for Box Sections and Relation lines
 * - TextLine elements that are children of selected Boxes or Relations lose visibility when clicked 
 */
public class TextLine extends Text {

	Section parent;
	Relation relation;

	/**
	 * TextLine constructor (used for Sections)
	 * @param s - String that goes in this TextLine
	 * @param p - Parent Section
	 */
	public TextLine(String s, Section p) {
		super(s);
		
		parent = p;
		TextLine thisLine = this;
		
		//keeps box width from expanding
		setWrappingWidth(141);


		 //When clicked, create Input to replace this TextLine
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (parent.parent.controller.getSelectedBox() == parent.parent) {
					p.addInput(thisLine.getText(), thisLine);
				}
			}
		});

	}
	
	/**
	 * TextLine constructor (used for Relations)
	 * @param s - String that goes in this TextLine
	 * @param r - Parent Relation
	 */
	public TextLine(String s, Relation r) {
		super(s);
		
		parent = null;
		relation = r;
		
		TextLine thisLine = this;
		
		//When clicked, create Input to replace this TextLine
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (relation == relation.getController().getSelectedRelation()) {
					relation.addInput(thisLine.getText());
				}
				else {
					relation.getController().selectRelation(relation);
				}
				event.consume();
			}
		});
	}


}
