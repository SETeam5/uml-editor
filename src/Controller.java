import java.util.HashSet;
import java.util.Set;
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

	ContextMenu toolbar;
	FileMenu menu;
	WorkSpace workspace;
	BorderPane ui;
	private Box selectedBox;
	private Relation currentRelation;
	private boolean addingRelation;
	private Relation selectedRelation;
	private Set<Relation> relations;

	/**
	 * Controller constructor
	 * Initializes controller with all UI elements
	 */
	public Controller() {
		toolbar = new ContextMenu(this);
		menu = new FileMenu(this);
		workspace = new WorkSpace(this);
		ui = new BorderPane();
		relations = new HashSet<Relation>();
		
		ui.setLeft(toolbar);
		ui.setTop(menu);
		ui.setCenter(workspace);
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
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			//remove any relations attached to the box being removed
			Set<Relation> relationsToRemove = new HashSet<Relation>();
			for (Relation r : relations) {
				if (r.getEndBox() == selectedBox || r.getStartBox() == selectedBox) {
					r.remove();
					relationsToRemove.add(r);
				}
			}
			relations.removeAll(relationsToRemove);
			
			selectedBox = null;
		}
		if (selectedRelation != null) {
			selectedRelation.remove();
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
			currentRelation = new Relation(selectedBox, this);
		}
	}
	
	/**
	 * Completes a new Relation line
	 * @param b - the endpoint box for the line
	 */
	public void endCurrentRelation(Box b) {
		//only end relation if a box is selected
		//and the ending box and starting box are different
		if (b != null && !b.equals(currentRelation.getStartBox())) {
			currentRelation.setEndPoint(b);
			workspace.getChildren().add(currentRelation);
			currentRelation.toBack();
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
	 * Adds a Relation to the set
	 * @param r - the Relation to be added
	 */
	public void addRelation(Relation r) {
		relations.add(r);
	}
	
	/**
	 * Iterates through all relations and runs the update method to adjust their locations
	 */
	public void updateRelations() {
		for (Relation r : relations) {
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
	
	/**
	 * Getter for all Relations
	 * @return - the set of Relations
	 */
	public Set<Relation> getRelations() {
		return relations;
	}
}
