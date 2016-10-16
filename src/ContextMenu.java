import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ContextMenu extends VBox {

	Controller controller;
	Button delete;
	Button addBox;
	Button addRelation;
	//private int p;

	public ContextMenu(Controller c) {
		controller = c;
		
		//space between buttons
		setSpacing(10);
		setPadding(new Insets(20, 10, 10, 10));
		//preferred width - need to unify button widths and this won't be an issue
		setPrefWidth(105);

		addBox = new Button("Create Box");
		addRelation = new Button("Add Relation");
		delete = new Button("Delete");
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

		delete.setOnAction(new EventHandler<ActionEvent>() {
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
		getChildren().add(delete);
	}
	
	public void hideDeleteButton() {
		getChildren().remove(delete);
	}
	
	public void showAddBoxButton() {
		getChildren().add(addBox);
	}
	
	public void hideAddBoxButton() {
		getChildren().remove(addBox);
	}

	public void showAddRelationButton() {
		getChildren().add(addRelation);
	}
	
	public void hideAddRelationButton() {
		getChildren().remove(addRelation);
	}

}