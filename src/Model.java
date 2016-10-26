
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
	
	public Model(Stage primaryStage) {
		this.stage = primaryStage;
	}
	
	public void openFile(){
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
				reinstantiateobjects(model);
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
	
	public static void reinstantiateobjects(Model model){
		
	}
	
	public Map<Integer,RectangleData> getBoxMap() {
		return boxmap;
	}
	
	public Map<Integer,LineData> getLineMap() {
		return linemap;
	}
}
