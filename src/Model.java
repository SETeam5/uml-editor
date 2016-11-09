import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * Model Class
 * This is the serializable and deserializable object which stores all information
 * about the real and logical boxes and lines on screen.  the stage, realboxmap, and
 * reallinemap fields are marked transient because we want these in the model but do
 * not want to serialize them
 */
@SuppressWarnings("serial")
public class Model implements Serializable{

	transient Stage stage;
	
	Map<Integer,RectangleData> boxmap = new HashMap<Integer,RectangleData>();
	Map<Integer,LineData> linemap = new HashMap<Integer,LineData>();
	transient Map<Integer,Box> realboxmap = new HashMap<Integer,Box>();
	transient Map<Integer,Relation> reallinemap = new HashMap<Integer,Relation>();
	
	/**
	 * sets the stage field
	 * @param primaryStage
	 */
	public Model() {
		
	}
	
	/**
	 * takes in the controller passed by FileMenu, sets up the file chooser and when
	 * the user has chosen their file we get the absolute path and read the bytes of
	 * the file and cast them as a local model object which is then passed off to 
	 * reinstantiate the objects
	 * @param controller
	 */
	public void openFile(Controller controller){
		//File Chooser Code
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SER", "*.SER")
            );
		File file = fileChooser.showOpenDialog(stage);
        
		if(file != null) {
		//Deserialize Code
			
			Model model;
			
			try{
				FileInputStream accessfile = new FileInputStream(file.getAbsolutePath());
				ObjectInputStream in = new ObjectInputStream(accessfile);
				model = (Model) in.readObject();
				in.close();
				accessfile.close();
				reinstantiateobjects(model, controller);
			}
			catch(FileNotFoundException c) {
				System.out.println("File not found!");
				c.printStackTrace();
				
			}
			catch(IOException i) {
				i.printStackTrace();
			}
			catch(ClassNotFoundException c) {
				System.out.println("File not found!");
				c.printStackTrace();
				
			}
		
		
		}
		
		
	}
	
	/**
	 * Sets up the file chooser, gets the users filename and chosen directory to 
	 * save into, and writes out the bytes of model to a .ser file.
	 */
	public void saveFile(){
		//File Chooser Code
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SER", "*.SER")
            );
		File file = fileChooser.showSaveDialog(stage);
		
		if(file != null) {
		//Serialize Code
			try	 {
				FileOutputStream savedfile = new FileOutputStream(file.getAbsolutePath());
				ObjectOutputStream out = new ObjectOutputStream(savedfile);
				out.writeObject(this);
				out.close();
				savedfile.close();
			}
			catch(IOException i) {
				i.printStackTrace();
			}
		}
	}
	
	/**
	 * takes in the model acquired from .ser file and uses it's critical data maps
	 * to reinstantiate the real objects on screen. second inner for loop handles
	 * the text creation.
	 * @param model
	 * @param controller
	 */
	public void reinstantiateobjects(Model model, Controller controller){
		
		Map<Integer,RectangleData> myboxmap = model.getBoxMap();
		boxmap = myboxmap;
		for(int id = 1; id < myboxmap.size() + 1; id++){
			RectangleData boxdata = boxmap.get(id);
			Box rect = new Box(controller, this, id, boxdata);
			rect.setLayoutX(boxdata.xposition);
			rect.setLayoutY(boxdata.yposition);
			CopyOnWriteArrayList<RectangleTextData> textdata = boxdata.boxtextdata;
			for(RectangleTextData rtd: textdata){
				TextLine text = new TextLine(rtd.text, rect.sections[rtd.sectionnumber]);
				if(rtd.sectionindex != rtd.sectionsize - 1){
					System.out.println(rtd.text);
					rect.sections[rtd.sectionnumber].getChildren().add(rtd.sectionindex, text);
				}
				else{
					System.out.println("HI");
					rect.sections[rtd.sectionnumber].getChildren().set(rtd.sectionindex, text);
				}	
				
			}
			controller.deselectBox();
			controller.workspace.getChildren().add(rect);
		}
		
		Map<Integer,LineData> mylinemap = model.getLineMap();
		linemap = mylinemap;
		for(int id = 1; id < mylinemap.size() + 1; id++){
			LineData linedata = linemap.get(id);
			Box startbox = realboxmap.get(linedata.startboxid);
			Box endbox = realboxmap.get(linedata.endboxid);
			Relation line = new Relation(startbox,controller, this);
			line.setEndPoint(endbox);
			line.SetId(id);
			controller.workspace.getChildren().add(line);
			line.toBack();
		}

	}
	
	/**
	 * setter for the stage that filechooser uses
	 * @param primarystage
	 */
	public void setStage(Stage primarystage) {
		this.stage = primarystage;
	}
	
	/**
	 * getter for logical boxmap
	 * @return
	 */
	public Map<Integer,RectangleData> getBoxMap() {
		return boxmap;
	}
	
	/**
	 * getter for logical linemap
	 * @return
	 */
	public Map<Integer,LineData> getLineMap() {
		return linemap;
	}
	
	/**
	 * getter for real boxmap
	 * @return
	 */
	public Map<Integer,Box> getRealBoxMap() {
		return realboxmap;
	}
	
	/**
	 * getter for real linemap
	 * @return
	 */
	public Map<Integer,Relation> getRealLineMap() {
		return reallinemap;
	}
	
}
