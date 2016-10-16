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
	private Relation selectedRelation;

	public Controller() {
		toolbar = new ContextMenu(this);
		menu = new FileMenu();
		workspace = new WorkSpace(this);
		ui = new BorderPane();
		
		ui.setLeft(toolbar);
		ui.setTop(menu);
		ui.setCenter(workspace);
	}
	
	public void selectBox(Box box) {
		
		deselectRelation();
		
		if (selectedBox == null) {
			selectedBox = box;
			toolbar.showAddRelationButton();
			toolbar.showDeleteButton();
		} 
		else if (box != selectedBox) {
			selectedBox.getStyleClass().remove("box-shadow");
			selectedBox = box;
		}
		box.getStyleClass().add("box-shadow");
	}

	public void deleteSelected() {
		if (selectedBox != null) {
			workspace.getChildren().remove(selectedBox);
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			selectedBox = null;
		}
		if (selectedRelation != null) {
			workspace.getChildren().remove(selectedRelation);
			toolbar.hideDeleteButton();
			toolbar.showAddBoxButton();
			selectedRelation = null;
		}
	}
	
	public void deselectBox() {

		if (selectedBox != null){
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			selectedBox.getStyleClass().remove("box-shadow");
			selectedBox = null;
			cancelCurrentRelation();
		}
		
	}
	
	public void deselectRelation() {
		
		if (selectedRelation != null){
			toolbar.hideDeleteButton();
			toolbar.showAddBoxButton();
			selectedRelation.setStroke(Color.GRAY);
			selectedRelation = null;
		}
		
	}
	
	public void showGrid() {
		workspace.getStyleClass().add("noGrid");
	}
	
	public void startNewRelation() {
		if (selectedBox != null) {
			addingRelation = true;
			currentRelation = new Relation(selectedBox, this);
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

	public void selectRelation(Relation relation) {
		
		deselectBox();
		
		if (selectedRelation == null) {
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
			toolbar.hideAddBoxButton();
			toolbar.hideAddRelationButton();
			toolbar.showDeleteButton();
		} 
		else if (selectedRelation != relation) {
			selectedRelation.setStroke(null);
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
		}
	}

}
