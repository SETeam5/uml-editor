package seproject5;

import java.util.ArrayList;

public class LineData {
	
	private double startx;
	private double starty;
	private double endx;
	private double endy;
	private ArrayList<String> linetextdata;

	public LineData(double startx, double starty, double endx, double endy,
			ArrayList<String> linetextdata, Model model) {
		this.startx = startx;
		this.starty = starty;
		this.endx = endx;
		this.endy = endy;
		this.linetextdata = linetextdata;
		model.getLinelist().add(this);
	}

	public double getStartx() {
		return startx;
	}

	public void setStartx(double startx) {
		this.startx = startx;
	}

	public double getStarty() {
		return starty;
	}

	public void setStarty(double starty) {
		this.starty = starty;
	}

	public double getEndx() {
		return endx;
	}

	public void setEndx(double endx) {
		this.endx = endx;
	}

	public double getEndy() {
		return endy;
	}

	public void setEndy(double endy) {
		this.endy = endy;
	}

	public ArrayList<String> getLinetextdata() {
		return linetextdata;
	}

	public void setLinetextdata(ArrayList<String> linetextdata) {
		this.linetextdata = linetextdata;
	}

}
