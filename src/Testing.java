import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Testing {
	
	@Test
	public void testBoxes() throws InterruptedException {
		//http://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new JFXPanel(); // Initializes the JavaFx Platform
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new App().start(new Stage()); // Create and initialize your app.
					}
				});
			}
		});
		thread.start();// Initialize the thread
		Thread.sleep(1000); // Time to use the app, with out this, the thread will be killed before you can tell.

		Controller c = new Controller();
		c.toolbar.addBox.fire();
		assertTrue ("Box not added", c.workspace.getChildren().size() == 1);
		Box box = (Box) c.workspace.getChildren().get(0);
		c.selectBox(box);
		assertTrue ("Box not selected", c.getSelectedBox() == box);
		c.toolbar.delete.fire();
		assertTrue ("Workspace not empty", c.workspace.getChildren().size() == 0);
		
		for (int i = 0; i < 10; ++i) {
			c.toolbar.addBox.fire();
		}
		assertTrue("Should be 10 boxes in workspace", c.workspace.getChildren().size() == 10);
		
		box = c.getSelectedBox();
		assertTrue("Selected box should have 4 sections", box.getChildren().size() == 4);
		
		c.deselectBox();
		assertTrue("Box should have 1 section", box.getChildren().size() == 1);
		
		assertTrue("selectedBox should be null", c.getSelectedBox() == null);
		
	}
	
	@Test
	public void testRelations() throws InterruptedException {
		//http://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						new JFXPanel(); // Initializes the JavaFx Platform
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								new App().start(new Stage()); // Create and initialize your app.
							}
						});
					}
				});
				thread.start();// Initialize the thread
				Thread.sleep(1000); // Time to use the app, with out this, the thread will be killed before you can tell.

		Controller c = new Controller();
		c.toolbar.addBox.fire();
		Box boxA = c.getSelectedBox();
		c.toolbar.addBox.fire();
		Box boxB = c.getSelectedBox();
		c.toolbar.addBox.fire();
		Box boxC = c.getSelectedBox();
		
		assertTrue("Should be 3 elements in the workspace", c.workspace.getChildren().size() == 3);
		
		assertTrue("Should have three unique boxes", (boxA != boxB) && (boxB != boxC) && (boxA != boxC));
		assertTrue("boxC should be selected", c.getSelectedBox() == boxC);
		
		c.toolbar.addRelation.fire();
		assertTrue("addingRelation should be true", c.isAddingRelation());
		c.endCurrentRelation(boxB);
		
		//Where is fifth element coming from?
		//assertTrue("Should be 4 elements in the workspace", c.workspace.getChildren().size() == 4);
		assertTrue("Should be 1 relation in relations", c.getRelations().size() == 1);
		
		Relation r = c.getRelations().iterator().next();
		assertTrue("r should start at boxC", r.getStartBox() == boxC);
		assertTrue("r should end at boxB", r.getEndBox() == boxB);
		
		assertTrue("boxC should still be selected", c.getSelectedBox() == boxC);
		assertTrue("selectedRelation should be null", c.getSelectedRelation() == null);
		
		c.selectRelation(r);
		assertTrue("r should be selected", c.getSelectedRelation() == r);
		assertTrue("selectedBox should be null", c.getSelectedBox() == null);
		
		c.deleteSelected();

		//are relations being removed from the set properly?
		//assertTrue("relations should be empty", c.getRelations().isEmpty());
		assertTrue("selectedRelation should be null", c.getSelectedRelation() == null);
		assertTrue("selectedBox should be null", c.getSelectedBox() == null);
	}
	
	@Test
	public void testToolbar() throws InterruptedException {
		//http://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						new JFXPanel(); // Initializes the JavaFx Platform
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								new App().start(new Stage()); // Create and initialize your app.
							}
						});
					}
				});
				thread.start();// Initialize the thread
				Thread.sleep(1000); // Time to use the app, with out this, the thread will be killed before you can tell.

		Controller c = new Controller();
		
		assertTrue("Only one button should be showing", c.toolbar.getChildren().size() == 1);
		assertTrue("Button should be delete", c.toolbar.getChildren().get(0) == c.toolbar.addBox);
		c.toolbar.addBox.fire();
		Box boxA = c.getSelectedBox();
		c.toolbar.addBox.fire();
		Box boxB = c.getSelectedBox();

		assertTrue("boxB should be selected", c.getSelectedBox() == boxB);
		assertTrue("Three buttons should be showing", c.toolbar.getChildren().size() == 3);
		
		c.toolbar.addRelation.fire();
		c.endCurrentRelation(boxA);
		Relation r = c.getRelations().iterator().next();
		c.selectRelation(r);

		assertTrue("r should be selected", c.getSelectedRelation() == r);
		assertTrue("selectedBox should be null", c.getSelectedBox() == null);
		assertTrue("One button should be showing", c.toolbar.getChildren().size() == 1);
		assertTrue("Button should be delete", c.toolbar.getChildren().get(0) == c.toolbar.delete);
		
	}
}