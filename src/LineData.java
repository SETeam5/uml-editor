import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class LineData implements Serializable{
	
	Integer startboxid;
	Integer endboxid;
	ArrayList<String> linetextdata;
	Integer id;

	public LineData(int startboxidin, int endboxidin,ArrayList<String> linetextdata, Model model, Integer id) {
		this.startboxid = startboxidin;
		this.endboxid = endboxidin;
		this.linetextdata = linetextdata;
		this.id = id;
		model.getLineMap().put(id, this);
	}
	
	public void ResetLineData(int startboxidin,int endboxidin,ArrayList<String> linetextdata, Model model, Integer id) {
		this.id = id;
		this.startboxid = startboxidin;
		this.endboxid = endboxidin;
		this.linetextdata = linetextdata;
		model.getLineMap().put(id, this);
	}

}