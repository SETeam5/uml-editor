import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ContextMenu extends VBox {

	Controller controller;
	Button deleteBox;
	Button addBox;
	Button addRelation;

	public ContextMenu(Controller c) {
		super();
		
		controller = c;
		
		//space between buttons
		setSpacing(10);
		setPadding(new Insets(20, 10, 10, 10));

		addBox = new Button("  Create Box  ");
		deleteBox = new Button("  Delete Box  ");
		addRelation = new Button("Add Relation");
		getChildren().add(addBox);
		getStyleClass().add("vbox");

		//addBox button is always visible - should this go away when something is selected?
		addBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Box rect = new Box(controller);
				controller.workspace.getChildren().add(rect);
				controller.cancelCurrentRelation();
			}
		});

		//only available when a box is selected
		deleteBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.deleteSelected();
				controller.cancelCurrentRelation();
			}
		});

		//only available when a box is selected
		addRelation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.startNewRelation();
			}
		});
	}

	public void showDeleteButton() {
		getChildren().add(deleteBox);
	}
	
	public void hideDeleteButton() {
		getChildren().remove(deleteBox);
	}

	public void showAddRelationButton() {
		getChildren().add(addRelation);
	}
	
	public void hideAddRelationButton() {
		getChildren().remove(addRelation);
	}

}
