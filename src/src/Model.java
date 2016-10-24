package seproject5;

import java.util.ArrayList;				//serializable object

public class Model {
	
	private ArrayList<LineData> linelist = new ArrayList<>();
	private ArrayList<RectangleData> boxlist = new ArrayList<>();

	public Model(ArrayList<LineData> linelist, ArrayList<RectangleData> boxlist) {
		
		this.linelist = linelist;
		this.boxlist = boxlist;
		
	}

	public ArrayList<LineData> getLinelist() {
		return linelist;
	}

	public void setLinelist(ArrayList<LineData> linelist) {
		this.linelist = linelist;
	}

	public ArrayList<RectangleData> getBoxlist() {
		return boxlist;
	}

	public void setBoxlist(ArrayList<RectangleData> boxlist) {
		this.boxlist = boxlist;
	}
	
	
	
}
	
	