import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Section extends VBox{
	Box parent;
	
	public Section(Box p){
		parent = p;
		
		TextField input = new TextField();
		getChildren().add(input);
		setMinHeight(50);
	}

}
