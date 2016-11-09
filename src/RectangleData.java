import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * RectangleData class
 * Class is similiar to the LindeData class, it posesses the critical information of
 * the box objects
 */
@SuppressWarnings("serial")
public class RectangleData implements Serializable{

	int xposition;
	int yposition;
	Integer id;
	CopyOnWriteArrayList<RectangleTextData> boxtextdata = new CopyOnWriteArrayList<RectangleTextData>();
	
	/**
	 * when a new RectangleData object is created it is passed off to be stored in
	 * the model
	 * @param k
	 * @param l
	 * @param model
	 * @param id
	 */
	public RectangleData(int k, int l, Model model, Integer id) {
		this.xposition = k;
		this.yposition = l;
		this.id = id;
		model.getBoxMap().put(id, this);
	}
	
	/**
	 * Whenever a box is modifed it's critical information is changed in the model
	 * @param width
	 * @param height
	 * @param xposition
	 * @param yposition
	 * @param boxtextdata
	 * @param model
	 * @param id
	 */
	public void ResetRectangleData(int xposition, int
			yposition, Model model, Integer id, CopyOnWriteArrayList<RectangleTextData> boxtextin) {
		this.xposition = xposition;
		this.yposition = yposition;
		this.id = id;
		this.boxtextdata = boxtextin;
		model.getBoxMap().put(id, this);
	}
	
	/**
	 * a simple getter for the id of the box
	 * @return
	 */
	public int getId() {
		return id;
	}

}