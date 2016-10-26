
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ContextMenu extends VBox {

	Controller controller;
	Button delete;
	Button addBox;
	Button addRelation;
    
    Image imageDelete = new Image(getClass().getResourceAsStream("/src/b_eras.png"));
    Image imageBox = new Image(getClass().getResourceAsStream("/src/b_cbox.png"));
    Image imageRelation = new Image(getClass().getResourceAsStream("/src/b_rela.png"));
    Image imageAggrigation = new Image(getClass().getResourceAsStream("/src/b_aggr.png"));
    Image imageComposion = new Image(getClass().getResourceAsStream("/src/b_comp.png"));
    Image imageDependency = new Image(getClass().getResourceAsStream("/src/b_depe.png"));
    Image imageGeneralization = new Image(getClass().getResourceAsStream("/src/b_gene.png"));
    Image imageLine = new Image(getClass().getResourceAsStream("/src/b_line.png"));
	
	public ContextMenu(Controller c, final Model model) {
		controller = c;
		
		//space between buttons
		setSpacing(10);
		setPadding(new Insets(20, 10, 10, 10));
		//preferred width - need to unify button widths and this won't be an issue
		setPrefWidth(105);

		addBox = new Button("Create Box");
        addBox.setGraphic(new ImageView(imageBox));
                
		addRelation = new Button("Add Relation");
        addRelation.setGraphic(new ImageView(imageRelation));
                
		delete = new Button("Delete");
        delete.setGraphic(new ImageView(imageDelete));
                
		getChildren().add(addBox);
		getStyleClass().add("vbox");

		//addBox button is always visible - should this go away when something is selected?
		addBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Box rect = new Box(controller, model);
				rect.setLayoutY(20);
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