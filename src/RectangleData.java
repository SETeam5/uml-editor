import java.io.Serializable;
import java.util.ArrayList;

public class RectangleData implements Serializable{

	double width;
	double height;
	double xposition;
	double yposition;
	ArrayList<String> boxtextdata;
	Integer id;
	
	public RectangleData(double i, double j, double k, double
			l, ArrayList<String> boxtextdata, Model model, Integer id) {
		this.width = i;
		this.height = j;
		this.xposition = k;
		this.yposition = l;
		this.boxtextdata = boxtextdata;
		this.id = id;
		model.getBoxMap().put(id, this);
	}
	
	public void ResetRectangleData(double width, double height, double xposition, double
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