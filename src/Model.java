
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//Serializable Data Object

public class Model implements Serializable{

	transient Stage stage;
	
	Map<Integer,RectangleData> boxmap = new HashMap<Integer,RectangleData>();
	Map<Integer,LineData> linemap = new HashMap<Integer,LineData>();
	transient Map<Integer,Box> realboxmap = new HashMap<Integer,Box>();
	transient Map<Integer,Relation> reallinemap = new HashMap<Integer,Relation>();
	
	public Model(Stage primaryStage) {
		this.stage = primaryStage;
	}
	
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
	
	public void reinstantiateobjects(Model model, Controller controller){
		
		Map<Integer,RectangleData> myboxmap = model.getBoxMap();
		System.out.println(myboxmap.keySet());
		System.out.println(myboxmap.values());
		for(int id = 1; id < myboxmap.size() + 1; id++){
			RectangleData boxdata = myboxmap.get(id);
			Box rect = new Box(controller, this, id, boxdata);
			rect.setLayoutX(boxdata.xposition);
			rect.setLayoutY(boxdata.yposition);
			controller.workspace.getChildren().add(rect);
			
		}
		
		Map<Integer,LineData> mylinemap = model.getLineMap();
		System.out.println(mylinemap.keySet());
		System.out.println(mylinemap.values());
		for(int id = 1; id < mylinemap.size() + 1; id++){
			LineData linedata = mylinemap.get(id);
			Box startbox = realboxmap.get(linedata.startboxid);
			Box endbox = realboxmap.get(linedata.endboxid);
			Relation line = new Relation(startbox,controller, this);
			line.setEndPoint(endbox);
			controller.workspace.getChildren().add(line);
			line.toBack();
			line.SetId(id);
		}
		
	}
	
	public Map<Integer,RectangleData> getBoxMap() {
		return boxmap;
	}
	
	public Map<Integer,LineData> getLineMap() {
		return linemap;
	}
	
	public Map<Integer,Box> getRealBoxMap() {
		return realboxmap;
	}
	
	public Map<Integer,Relation> getRealLineMap() {
		return reallinemap;
	}
	
}
