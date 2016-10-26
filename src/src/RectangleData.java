import java.util.ArrayList;

public class RectangleData {

	Double width;
	Double height;
	Double xposition;
	Double yposition;
	ArrayList<String> boxtextdata;
	Integer id;
	
	public RectangleData(Double width, Double height, Double xposition, Double
			yposition, ArrayList<String> boxtextdata, Model model, Integer id) {
		this.width = width;
		this.height = height;
		this.xposition = xposition;
		this.yposition = yposition;
		this.boxtextdata = boxtextdata;
		this.id = id;
		model.getBoxMap().put(id, this);
	}
	
	public void ResetRectangleData(Double width, Double height, Double xposition, Double
			yposition, ArrayList<String> boxtextdata, Model model, Integer id) {
		this.width = width;
		this.height = height;
		this.xposition = xposition;
		this.yposition = yposition;
		this.boxtextdata = boxtextdata;
		model.getBoxMap().replace(id, this);
	}

	public void DeleteRectangleData(Model model, Integer id) {
		model.getBoxMap().remove(id);
	}

}