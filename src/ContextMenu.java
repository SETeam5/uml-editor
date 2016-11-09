import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;

/**
 * ContextMenu Class
 * Toolbar with buttons that change according to the currently selected object
 * -Currently has three states - Box selected, Relation selected, nothing selected
 */
public class ContextMenu extends VBox {

	Controller controller;
	Button delete;
	Button addBox;
	Button addRelation;
	Integer boxid = 0;

	Image imageDelete = new Image(getClass().getResourceAsStream("/buttons/b_eras.png"));
	Image imageBox = new Image(getClass().getResourceAsStream("/buttons/b_cbox.png"));
	Image imageRelation = new Image(getClass().getResourceAsStream("/buttons/b_rela.png"));
	Image imageAggrigation = new Image(getClass().getResourceAsStream("/buttons/b_aggr.png"));
	Image imageComposion = new Image(getClass().getResourceAsStream("/buttons/b_comp.png"));
	Image imageDependency = new Image(getClass().getResourceAsStream("/buttons/b_depe.png"));
	Image imageGeneralization = new Image(getClass().getResourceAsStream("/buttons/b_gene.png"));
	Image imageLine = new Image(getClass().getResourceAsStream("/buttons/b_line.png"));

	/**
     * ContextMenu constructor
     * @param c - the Controller
     */
	public ContextMenu(Controller c, final Model model) {
		controller = c;

		// space between buttons
		setSpacing(10);
		setPadding(new Insets(20, 10, 10, 10));
		// preferred width - need to unify button widths and this won't be an issue
		setPrefWidth(105);

		addBox = new Button("Create Box");
		addRelation = new Button("Add Relation");
		delete = new Button("Delete");
		getChildren().add(addBox);
		getStyleClass().add("vbox");

		addBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.deselectBox();
				boxid++;
				RectangleData boxdata = new RectangleData(0,0,model,boxid);
				Box rect = new Box(controller, model, boxid, boxdata);				
				controller.workspace.getChildren().add(rect);
				controller.cancelCurrentRelation();
			}
		});

		addBox.setGraphic(new ImageView(imageBox));
		addBox.setTooltip(new Tooltip("New Class Box"));

		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.deleteSelected();
				controller.cancelCurrentRelation();
			}
		});

		delete.setGraphic(new ImageView(imageDelete));
		delete.setTooltip(new Tooltip("Delete"));

		// only available when a box is selected
		addRelation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.startNewRelation();
			}
		});

		addRelation.setGraphic(new ImageView(imageRelation));
		addRelation.setTooltip(new Tooltip("Add a new Realtion"));
	}

	/**
	 * Displays the delete button
	 */
	public void showDeleteButton() {
		getChildren().add(delete);
	}

	/**
	 * Hides the delete button
	 */
	public void hideDeleteButton() {
		getChildren().remove(delete);
	}

	/**
	 * Displays the add box button
	 */
	public void showAddBoxButton() {
		getChildren().add(addBox);
	}

	/**
	 * Hides the add box button
	 */
	public void hideAddBoxButton() {
		getChildren().remove(addBox);
	}

	/**
	 * Displays the add relation button
	 */
	public void showAddRelationButton() {
		getChildren().add(addRelation);
	}

	/**
	 * Hides the add relation button
	 */
	public void hideAddRelationButton() {
		getChildren().remove(addRelation);
	}

}
