
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Controller {

	Model model;
	ContextMenu toolbar;
	FileMenu menu;
	WorkSpace workspace;
	ScrollBar scrollbar;
	BorderPane ui;
	Box selectedBox = null;
	private Relation currentRelation = null;
	private boolean addingRelation = false;
	private Relation selectedRelation;

	public Controller(Model model) {
		this.model = model;
		toolbar = new ContextMenu(this, model);
		menu = new FileMenu(model);
		workspace = new WorkSpace(this);
		scrollbar = new VerticalScrollbar(this);
		ui = new BorderPane();
		
		workspace.setMinHeight(1742);
		
		ui.setLeft(toolbar);
		ui.setTop(menu);
		ui.setCenter(workspace);
		ui.setRight(scrollbar);
	}
	
	public void selectBox(Box box) {
		
		deselectRelation();
		
		if (selectedBox == null) {
			selectedBox = box;
			toolbar.hideAddBoxButton();
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

	public void deleteSelected() {
		if (selectedBox != null) {
			workspace.getChildren().remove(selectedBox);
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			toolbar.showAddBoxButton();
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
			toolbar.showAddBoxButton();
			selectedBox.deselect();
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
			currentRelation = new Relation(selectedBox, this, model);
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

