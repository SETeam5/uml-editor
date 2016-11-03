import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class RectangleData implements Serializable{

	int width;
	int height;
	int xposition;
	int yposition;
	ArrayList<String> boxtextdata = new ArrayList<String>();
	Integer id;
	
	public RectangleData(int i, int j, int k, int l, Model model, Integer id) {
		this.width = i;
		this.height = j;
		this.xposition = k;
		this.yposition = l;
		this.id = id;
		model.getBoxMap().put(id, this);
	}
	
	public void ResetRectangleData(int width, int height, int xposition, int
			yposition, ArrayList<String> boxtextdata, Model model, Integer id) {
		this.width = width;
		this.height = height;
		this.xposition = xposition;
		this.yposition = yposition;
		this.boxtextdata = boxtextdata;
		this.id = id;
		model.getBoxMap().put(id, this);
	}
	
	public int getId() {
		return id;
	}

}