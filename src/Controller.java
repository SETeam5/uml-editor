import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Controller {

	ContextMenu toolbar;
	FileMenu menu;
	WorkSpace workspace;
	BorderPane ui;
	Box selectedBox = null;
	private Relation currentRelation = null;
	private boolean addingRelation = false;

	public Controller() {
		toolbar = new ContextMenu(this);
		menu = new FileMenu();
		workspace = new WorkSpace(this);
		ui = new BorderPane();
		
		ui.setLeft(toolbar);
		ui.setTop(menu);
		ui.setCenter(workspace);
	}
	
	//rather than use setStroke(), we might want to use CSS here
	public void selectBox(Box box) {
		if (selectedBox == null) {
			selectedBox = box;
			//box.setStroke(Color.WHITE);
		    toolbar.showDeleteButton();
		    toolbar.showAddRelationButton();
		} 
		else if (box != selectedBox) {
			//selectedBox.setStroke(null);
			selectedBox = box;
			//box.setStroke(Color.WHITE);
		}
	}

	public void deleteSelected() {
		if (selectedBox != null) {
			workspace.getChildren().remove(selectedBox);
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			selectedBox = null;
		}
	}
	
	public void deselect() {
		if (selectedBox != null){
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			//selectedBox.setStroke(null);
			selectedBox = null;
		}
		
	}

	public void showGrid() {
		workspace.getStyleClass().add("noGrid");
	}
	
	public void startNewRelation() {
		if (selectedBox != null) {
			addingRelation = true;
			currentRelation = new Relation(selectedBox);
		}
	}
	
	public void endCurrentRelation() {
		//only end relation if a box is selected
		//and the ending box and starting box are different
		if (selectedBox != null && !selectedBox.equals(currentRelation.getStartingBox())) {
			currentRelation.setEndPoint(selectedBox);
			workspace.getChildren().add(currentRelation);
			currentRelation.toBack();
			currentRelation = null;
			addingRelation = false;
		} else {
			//invalid ending box
			cancelCurrentRelation();
		}
	}
	
	public boolean isAddingRelation() {
		return addingRelation;
	}
	
	public void cancelCurrentRelation() {
		addingRelation = false;
		currentRelation = null;
	}

}
