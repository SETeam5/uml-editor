import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Controller Class
 * Communicates with most Panes and Nodes
 * -selectedBox and selectedRelation track the currently selected object
 * -One or both of selectedBox and selectedRelation must be null at all times
 * -currentRelation is a relation that has been started, but doesn't have an endpoint yet
 * -addingRelation is a boolean tracking if a user has begun the 'add relation' process but has not yet clicked a 2nd box
 * -relations is a set of all active relations
 */
public class Controller {

	Model model;
	ContextMenu toolbar;
	FileMenu menu;
	WorkSpace workspace;
	BorderPane ui;
	private Box selectedBox = null;
	private Relation currentRelation = null;
	private boolean addingRelation = false;
	private Relation selectedRelation;
	Integer boxid = 0;
	Integer lineid = 0;
	ArrayList<String> boxtext = new ArrayList<String>();
	ArrayList<String> linetext = new ArrayList<String>();

	/**
	 * Controller constructor
	 * Initializes controller with all UI elements
	 */
	public Controller(Model model) {
		this.model = model;
	    toolbar = new ContextMenu(this, model);
		menu = new FileMenu(this, model);
		workspace = new WorkSpace(this);
		ScrollPane scrollpane = new ScrollPane(workspace);
		scrollpane.getStyleClass().add("scroll-pane");
		ui = new BorderPane();
		
		ui.setLeft(toolbar);
		ui.setTop(menu);
		ui.setCenter(scrollpane);
	}
	
	/**
	 * Selects a box
	 * @param box - the Box that will be selected
	 */
	public void selectBox(Box box) {
		
		deselectRelation();
		
		if (selectedBox == null) {
			selectedBox = box;
			toolbar.showAddRelationButton();
			toolbar.showDeleteButton();
			selectedBox.getStyleClass().add("box-shadow");
			selectedBox.select();
		} 
		else if (box != selectedBox) {
			selectedBox.getStyleClass().remove("box-shadow");
			selectedBox = box;
			selectedBox.getStyleClass().add("box-shadow");
			selectedBox.select();
		}
	}

	/**
	 * Deletes the currently selected object (either selectedBox or selectedRelation)
	 */
	public void deleteSelected() {
		if (selectedBox != null) {
			workspace.getChildren().remove(selectedBox);
			selectedBox.DeleteRectangleData(selectedBox.id);
			toolbar.boxid--;
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			//remove any relations attached to the box being removed
			for (int i = 1; i < model.reallinemap.size() + 1; i++) {
				System.out.println(model.reallinemap);
				Relation r = model.reallinemap.get(i);
				System.out.println(r);
				if (r.getEndingBox() == selectedBox || r.getStartingBox() == selectedBox) {
					r.remove();
					r.DeleteLineData(r.id);
					lineid--;
					r = null;
					i--;
				}
			}
			
			selectedBox = null;
		}
		if (selectedRelation != null) {
			selectedRelation.remove();
			selectedRelation.DeleteLineData(selectedRelation.id);
			lineid--;
			toolbar.hideDeleteButton();
			toolbar.showAddBoxButton();
			selectedRelation = null;
		}
	}
	
	/**
	 * Deselects the currently selected box if there is one
	 */
	public void deselectBox() {
		if (selectedBox != null){
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			selectedBox.deselect();
			selectedBox.getStyleClass().remove("box-shadow");
			selectedBox = null;
			cancelCurrentRelation();
		}
	}
	
	/**
	 * Deselects the currently selected relation if there is one
	 */
	public void deselectRelation() {
		
		if (selectedRelation != null){
			toolbar.hideDeleteButton();
			toolbar.showAddBoxButton();
			selectedRelation.setStroke(Color.GRAY);
			selectedRelation.hideText();
			selectedRelation = null;
		}
		
	}
	
	/**
	 * Hides the grid overlay from the workspace
	 */
	public void hideGrid() {
		workspace.getStyleClass().add("noGrid");
	}
	
	/**
	 * Displays the grid overlay on the workspace
	 */
	public void showGrid() {
		workspace.getStyleClass().remove("noGrid");
		workspace.getStyleClass().add("grid");
	}
	
	/**
	 * Begins the process of adding a new Relation between two Boxes
	 */
	public void startNewRelation() {
		if (selectedBox != null) {
			addingRelation = true;
			currentRelation = new Relation(selectedBox, this, model);
		}
	}
	
	/**
	 * Completes a new Relation line and stores line and linedata into the model
	 * @param b - the endpoint box for the line
	 */
	public void endCurrentRelation(Box b) {
		//only end relation if a box is selected
		//and the ending box and starting box are different
		if (b != null && !b.equals(currentRelation.getStartBox())) {
			currentRelation.setEndPoint(b);
			workspace.getChildren().add(currentRelation);
			currentRelation.toBack();
			lineid++;
			LineData linedata = new LineData(currentRelation.getStartingBox().id,currentRelation.getEndingBox().id,linetext,model,lineid);
			currentRelation.SetId(lineid);
			currentRelation = null;
			addingRelation = false;
		} else {
			//invalid ending box
			cancelCurrentRelation();
		}
	}
	
	/**
	 * Getter for addingRelation
	 * @return - boolean value of addingRelation
	 */
	public boolean isAddingRelation() {
		return addingRelation;
	}
	
	/**
	 * Cancels adding a new relation
	 * Called if the user doesn't select a valid endpoint for the Relation
	 */
	public void cancelCurrentRelation() {
		addingRelation = false;
		currentRelation = null;
	}

	/**
	 * Selects a Relation line
	 * @param relation - the Relation to be selected
	 */
	public void selectRelation(Relation relation) {
		
		deselectBox();
		
		if (selectedRelation == null) {
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
			toolbar.hideAddBoxButton();
			toolbar.hideAddRelationButton();
			toolbar.showDeleteButton();
			selectedRelation.showText();
		} 
		else if (selectedRelation != relation) {
			selectedRelation.setStroke(null);
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
		}
	}
	
	/**
	 * Iterates through all relations and runs the update method to adjust their locations
	 */
	public void updateRelations() {
		for (int i = 1; i < model.reallinemap.size() + 1; i++) {
			Relation r = model.reallinemap.get(i);
			r.update();
		}
	}
	
	/**
	 * Getter for selectedBox
	 * @return - the selected Box
	 */
	public Box getSelectedBox() {
		return selectedBox;
	}
	
	/**
	 * Getter for selectedRelation
	 * @return - the selected Relation
	 */
	public Relation getSelectedRelation() {
		return selectedRelation;
	}
	
}