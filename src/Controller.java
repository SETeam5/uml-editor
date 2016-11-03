import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

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

	public void deleteSelected() {
		if (selectedBox != null) {
			workspace.getChildren().remove(selectedBox);
			selectedBox.DeleteRectangleData(selectedBox.id);
			toolbar.boxid--;
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			for (int i = 1; i < model.reallinemap.size() + 1; i++) {
				Relation r = model.reallinemap.get(i);
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
	
	public void deselectRelation() {
		
		if (selectedRelation != null){
			toolbar.hideDeleteButton();
			toolbar.showAddBoxButton();
			selectedRelation.setStroke(Color.GRAY);
			selectedRelation.hideText();
			selectedRelation = null;
		}
		
	}
	
	public void hideGrid() {
		workspace.getStyleClass().add("noGrid");
	}
	
	public void showGrid() {
		workspace.getStyleClass().remove("noGrid");
		workspace.getStyleClass().add("grid");
	}
	
	public void startNewRelation() {
		if (selectedBox != null) {
			addingRelation = true;
			currentRelation = new Relation(selectedBox, this, model);
		}
	}
	
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
			selectedRelation.showText();
		} 
		else if (selectedRelation != relation) {
			selectedRelation.setStroke(null);
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
		}
	}
	
	public void updateRelations() {
		for (int i = 1; i < model.reallinemap.size() + 1; i++) {
			Relation r = model.reallinemap.get(i);
			r.update();
		}
	}
	
	public Box getSelectedBox() {
		return selectedBox;
	}
	
	public Relation getSelectedRelation() {
		return selectedRelation;
	}
}