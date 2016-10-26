
import java.util.ArrayList;

public class LineData {
	
	Double startx;
	Double starty;
	Double endx;
	Double endy;
	ArrayList<String> linetextdata;
	Integer id;

	public LineData(Double startx, Double starty, Double endx, Double endy,
			ArrayList<String> linetextdata, Model model, Integer id) {
		this.startx = startx;
		this.starty = starty;
		this.endx = endx;
		this.endy = endy;
		this.linetextdata = linetextdata;
		this.id = id;
		model.getLineMap().put(id, this);
	}
	
	public void ResetLineData(Double startx, Double starty, Double endx, Double endy,
			ArrayList<String> linetextdata, Model model, Integer id) {
		this.startx = startx;
		this.starty = starty;
		this.endx = endx;
		this.endy = endy;
		this.linetextdata = linetextdata;
		model.getLineMap().replace(id, this);
	}

	public void DeleteLineData(Model model, Integer id) {
		model.getLineMap().remove(id);
	}

}