import java.io.Serializable;

/**
 * An oject that groups important text information about boxes. dynamically updates
 * section size for all RTD objects of a given section each time a RTD object is added
 * to that section. also handles model state when editing text in a box.
 */
@SuppressWarnings("serial")
public class RectangleTextData implements Serializable{

	String text;
	int sectionindex;
	int sectionnumber;
	int sectionsize = 1;
	
	public RectangleTextData(String textin, int sectionindexin, int sectionnumberin, RectangleData rd) {
		
		this.text = textin;
		this.sectionindex = sectionindexin;
		this.sectionnumber = sectionnumberin;
		
		//remove any RectangleTextData object stored at this's index and section
		//this handles editing of the text
		/*for(RectangleTextData rtd: rd.boxtextdata){
			if((rtd.sectionnumber == sectionnumber) && (rtd.sectionindex == sectionindex)){
				rd.boxtextdata.remove(sectionindex);
				for(RectangleTextData rtdd: rd.boxtextdata)
					if(rtdd.sectionnumber == this.sectionnumber){
						rtdd.sectionsize--;
					}
			}
		}*/
		/*
		boolean p = rd.boxtextdata.removeIf(rtd -> (rtd.sectionnumber == sectionnumber) && (rtd.sectionindex == sectionindex));
		if(p){
			for(RectangleTextData rtd: rd.boxtextdata){
				if(rtd.sectionnumber == sectionnumber){
					rtd.sectionsize--;
				}
			}
		}*/
		//need this so all pieces of text in a section have the same section size
		for(RectangleTextData rtd: rd.boxtextdata){
			if((rtd.sectionnumber == this.sectionnumber) && (rtd.sectionindex == 0)) {
				rtd.sectionsize++;
				this.sectionsize = rtd.sectionsize;
				//update all other pieces of text in this texts section besides this and index o
				for(RectangleTextData rtdd: rd.boxtextdata)
					if(rtdd.sectionnumber == this.sectionnumber && rtdd.sectionindex != 0){
						rtdd.sectionsize = rtd.sectionsize;
					}
			}
		}
		
	}

}
