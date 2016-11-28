import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

/**
 * Controller Class Communicates with most Panes and Nodes -selectedBox and
 * selectedRelation track the currently selected object -One or both of
 * selectedBox and selectedRelation must be null at all times -currentRelation
 * is a relation that has been started, but doesn't have an endpoint yet
 * -addingRelation is a boolean tracking if a user has begun the 'add relation'
 * process but has not yet clicked a 2nd box -relations is a set of all active
 * relations
 */
public class Controller {

	ContextMenu toolbar;
	FileMenu menu;
	WorkSpace workspace;
	ScrollBars scrollpane;
	BorderPane ui;
	private Box selectedBox;
	private Relation currentRelation;
	private boolean addingRelation;
	private Relation selectedRelation;
	private Set<Box> boxes;
	private Set<Relation> relations;

	/**
	 * Controller constructor Initializes controller with all UI elements
	 */
	public Controller() {
		toolbar = new ContextMenu(this);
		menu = new FileMenu(this);
		workspace = new WorkSpace(this);
		scrollpane = new ScrollBars(this);
		scrollpane.getStyleClass().add("scroll-pane");
		ui = new BorderPane();
		boxes = new HashSet<Box>();
		relations = new HashSet<Relation>();

		ui.setLeft(toolbar);
		ui.setTop(menu);
		ui.setCenter(scrollpane);
	}

	/**
	 * Selects a box
	 * 
	 * @param box
	 *            - the Box that will be selected
	 */
	public void selectBox(Box box) {

		deselectRelation();

		if (selectedBox != null && box != selectedBox) {
			selectedBox.getStyleClass().remove("box-shadow");
		}
		selectedBox = box;
		selectedBox.getStyleClass().add("box-shadow");
		selectedBox.select();

		// remove buttons and re-add (addRelation only if there are more than
		// one box)
		toolbar.hideDeleteButton();
		toolbar.hideAddRelationButton();
		if (boxes.size() > 1) {
			toolbar.showAddRelationButton();
		}
		toolbar.showDeleteButton();
	}

	/**
	 * Deletes the currently selected object (either selectedBox or
	 * selectedRelation)
	 */
	public void deleteSelected() {
		if (selectedBox != null) {
			workspace.getChildren().remove(selectedBox);
			boxes.remove(selectedBox);
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			// remove any relations attached to the box being removed
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
			toolbar.hideEditRelationButtons();
			toolbar.showAddBoxButton();
			selectedRelation = null;
		}
	}

	/**
	 * Deselects the currently selected box if there is one
	 */
	public void deselectBox() {
		if (selectedBox != null) {
			toolbar.hideDeleteButton();
			toolbar.hideAddRelationButton();
			selectedBox.deselect();
			selectedBox.getStyleClass().remove("box-shadow");
			selectedBox = null;
		}
	}

	/**
	 * Deselects the currently selected relation if there is one
	 */
	public void deselectRelation() {
		if (selectedRelation != null) {
			toolbar.hideDeleteButton();
			toolbar.hideEditRelationButtons();
			toolbar.showAddBoxButton();
			selectedRelation.setStroke(Paint.valueOf("#666666"));
			selectedRelation.hideText();
			selectedRelation = null;
		}
	}

	/**
	 * Hides the grid overlay from the workspace
	 */
	public void hideGrid() {
		workspace.getStyleClass().remove("noBG");
		workspace.getStyleClass().add("noGrid");
	}

	/**
	 * Displays the grid overlay on the workspace
	 */
	public void showGrid() {
		workspace.getStyleClass().remove("noGrid");
		workspace.getStyleClass().add("grid");
	}
	
	public void noBG() {
		workspace.getStyleClass().add("noBG");
	}

	/**
	 * Begins the process of adding a new Relation between two Boxes
	 */
	public void startNewRelation() {
		if (selectedBox != null) {
			addingRelation = true;
			currentRelation = new Relation(selectedBox, this);
			toolbar.setAddRelationShadow(true);
		}
	}

	/**
	 * Completes a new Relation line, or cancels if an invalid endpoint is
	 * passed
	 * 
	 * @param b
	 *            - the endpoint box for the line
	 */
	public void endCurrentRelation(Box b) {
		// only end relation if a box is selected
		// and the ending box and starting box are different
		if (b != null && !b.equals(currentRelation.getStartBox())) {
			currentRelation.setEndPoint(b);
			relations.add(currentRelation);
			if (!workspace.getChildren().contains(currentRelation)) {
				workspace.getChildren().add(currentRelation);
				currentRelation.toBack();
			}
			deselectBox();
			selectRelation(currentRelation);
			currentRelation = null;
			addingRelation = false;
			toolbar.setAddRelationShadow(false);
		} else {
			// invalid ending box
			displayInvalidRelationMessage();
		}
	}
	
	public void setCurrentRelationEndPosition(double x, double y) {
		if (currentRelation != null && x > 0 && x < workspace.getWidth() && y > 0 && y < workspace.getHeight()) {
			currentRelation.setTempEndPoint(x, y);
			currentRelation.toBack();
			if (!workspace.getChildren().contains(currentRelation)) {
				workspace.getChildren().add(currentRelation);
			}
		}
	}

	/**
	 * Getter for addingRelation
	 * 
	 * @return - boolean value of addingRelation
	 */
	public boolean isAddingRelation() {
		return addingRelation;
	}

	/**
	 * Cancels adding a new relation Called if the user doesn't select a valid
	 * endpoint for the Relation
	 */
	public void cancelCurrentRelation() {
		addingRelation = false;
		workspace.getChildren().remove(currentRelation);
		currentRelation = null;
		toolbar.setAddRelationShadow(false);
	}
	
	/**
	 * To be called when an invalid end point is selected for a relation
	 */
	public void displayInvalidRelationMessage() {
		Alert alert = new Alert(AlertType.WARNING);
        alert.setContentText("Select a Valid Class Box");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.show();
	}

	/**
	 * Selects a Relation line
	 * 
	 * @param relation
	 *            - the Relation to be selected
	 */
	public void selectRelation(Relation relation) {
		deselectBox();

		if (selectedRelation == null) {
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
			toolbar.hideAddBoxButton();
			toolbar.hideAddRelationButton();
			toolbar.showEditRelationButtons();
			toolbar.showDeleteButton();
			selectedRelation.showText();
		} else if (selectedRelation != relation) {
			selectedRelation.setStroke(Paint.valueOf("#666666"));
			selectedRelation.hideText();
			selectedRelation = relation;
			selectedRelation.setStroke(Color.WHITE);
			selectedRelation.showText();
		}
		
		toolbar.setArrowHeadTypeShadow(relation.getRelationType());
		toolbar.setLineTypeShadow(relation.isDotted());
		toolbar.setRelationEndingTypeShadow(relation.isSingleEnded());
	}

	public void flipCurrentRelation() {
		if (selectedRelation != null) {
			selectedRelation.flip();
		}
	}

	public void setCurrentRelationSingleEnded() {
		if (selectedRelation != null) {
			selectedRelation.setSingleEnded();
		}
	}

	public void setCurrentRelationDoubleEnded() {
		if (selectedRelation != null) {
			selectedRelation.setDoubleEnded();
		}
	}
	
	public void setCurrentRelationSolid() {
		if (selectedRelation != null) {
			selectedRelation.setSolid();
		}
	}
	
	public void setCurrentRelationDotted() {
		if (selectedRelation != null) {
			selectedRelation.setDotted();
		}
	}
	
    public void setRelationType(int type) {
        if(selectedRelation != null) {
            selectedRelation.setRelationType(type);
        }
    }

	/**
	 * Adds a box to the set and workspace
	 * 
	 * @param b-
	 *            the box to be added
	 */
	public void addBox(Box b) {
		boxes.add(b);
		workspace.getChildren().add(b);
	}

	/**
	 * Iterates through all relations and runs the update method to adjust their
	 * locations
	 */
	public void updateRelations() {
		for (Relation r : relations) {
			r.update();
		}
	}

	/**
	 * Getter for selectedBox
	 * 
	 * @return - the selected Box
	 */
	public Box getSelectedBox() {
		return selectedBox;
	}

	/**
	 * Getter for selectedRelation
	 * 
	 * @return - the selected Relation
	 */
	public Relation getSelectedRelation() {
		return selectedRelation;
	}

	/**
	 * Getter for all Relations
	 * 
	 * @return - the set of Relations
	 */
	public Set<Relation> getRelations() {
		return relations;
	}

	public void clear() {
		workspace.getChildren().clear();
	}
	
	public void print() {
		PrinterJob job = PrinterJob.createPrinterJob();
		job.showPageSetupDialog(null);
		noBG();
		job.printPage(workspace);
		hideGrid();
		job.endJob();
	}

	public void save() throws IOException {
		FileChooser fc = new FileChooser();
		fc.setSelectedExtensionFilter(null);
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("UML document", "*.uml"));
		File f = fc.showSaveDialog(null);
		if (f == null) return;
		FileWriter fw = new FileWriter(f);

		int count = 0;
		for (Node n : workspace.getChildren()) {
			if (Box.class.isInstance(n)) {
				Box b = (Box) n;
				fw.append(b.serialize(count));
				++count;
			}
		}
		
		fw.append("__startRelations\n");
		
		for (Node n : workspace.getChildren()) {
			if (Relation.class.isInstance(n)) {
				Relation r = (Relation) n;
				fw.append(r.serialize());
			}
		}
		fw.close();
	}

	public void open() throws IOException {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("UML document", "*.uml"));
		File f = fc.showOpenDialog(null);
		if (f == null) return;
		Scanner s = new Scanner(f);
		workspace.getChildren().clear();
		boolean pastBoxes = false;
		
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.equals("__startRelations")) {
				pastBoxes = true;
				line = s.nextLine();
			}
			if (!pastBoxes){
				Box box = new Box(this);
				deselectBox();
				box.setLayoutX(Double.parseDouble(line.trim()));
				box.setLayoutY(Double.parseDouble(s.nextLine().trim()));
				int sec = 0;
				while (sec < 4) {
					while (true) {
						line = s.nextLine();
						if (line.equals("__section"))
							break;
						else if (sec == 0)
							((TextLine) box.getSections()[0].getChildren().get(0)).setText(line);
						else
							box.getSections()[sec].addLine(line);
					}
					++sec;
				}
				box.setID(Integer.parseInt(s.nextLine()));
				System.out.println(s.nextLine());
				selectBox(box);
				deselectBox();
			}
			else {
				selectBox(getBoxByID(Integer.parseInt(line)));
				startNewRelation();
				Relation r = currentRelation;
				endCurrentRelation(getBoxByID(Integer.parseInt(s.nextLine())));
				deselectBox();
				String text = s.nextLine();
				if (!text.equals("add text here")) {
					r.setText(text);
					r.showText();
				}
				r.setRelationType(Integer.parseInt(s.nextLine()));
				if (s.nextLine().equals("second")) {
					r.setDoubleEnded();
				}
				if (s.nextLine().equals("dotted")) {
					r.setDotted();
				}
				s.nextLine();
				updateRelations();
			}
		}
		deselectBox();
		deselectRelation();
		s.close();
	}
	
	public Box getBoxByID(int id) {
		for (Node n : workspace.getChildren()) {
			if (Box.class.isInstance(n)) {
				Box b = (Box) n;
				if (b.getID() == id) {
					return b;
				}
			}
		}
		return null;
	}

}
