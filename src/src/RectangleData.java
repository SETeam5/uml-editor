import java.util.ArrayList;

public class RectangleData {

	private double width;
	private double height;
	private double xposition;
	private double yposition;
	private ArrayList<String> boxtextdata;
	
	public RectangleData(double width, double height, double xposition, double
			yposition, ArrayList<String> boxtextdata, Model model) {
		this.width = width;
		this.height = height;
		this.xposition = xposition;
		this.yposition = yposition;
		this.boxtextdata = boxtextdata;
		model.getBoxlist().add(this);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getXposition() {
		return xposition;
	}

	public void setXposition(double xposition) {
		this.xposition = xposition;
	}

	public double getYposition() {
		return yposition;
	}

	public void setYposition(double yposition) {
		this.yposition = yposition;
	}

	public ArrayList<String> getBoxtextdata() {
		return boxtextdata;
	}

	public void setBoxtextdata(ArrayList<String> boxtextdata) {
		this.boxtextdata = boxtextdata;
	}

}