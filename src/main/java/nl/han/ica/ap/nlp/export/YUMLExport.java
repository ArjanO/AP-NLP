package nl.han.ica.ap.nlp.export;

import java.io.IOException;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.util.File;

public class YUMLExport implements IExport{
	private ArrayList<ClassRelation> associationlist = new ArrayList<ClassRelation>();
	private String doc = "";
	
	public String exportSource(ArrayList<Class> classes) {
		doc = "";
		createClasses(classes);
		doc = doc.trim();
		doc = doc.substring(0, doc.length()-1);
		return doc;
	}
	
	//http://yuml.me/diagram/scruffy;/class/
	public String export(ArrayList<Class> classes) {
		doc = "";
		
		createClasses(classes);
		doc = doc.trim();
		doc = doc.substring(0, doc.length()-1);
		
		//Download and save image
		String url = "http://yuml.me/diagram/plain;/class/"+doc;
		String filepath = "target/YUML-" + (System.currentTimeMillis()) + ".png";
		
		File file = new File(filepath);
		
		//Download
		try {
			file.download(url);
		} catch (IOException e) {
			System.out.println("Can't download yuml image.");
		}
		
		return filepath;
	}
    
    private void createClasses(ArrayList<Class> classes) {
    	if (classes.size() > 0) {
    		for(Class child : classes){
	    		
	    		//Create associations
	    		ArrayList<Class> association_classes = new ArrayList<Class>();
    			for(Association asso : child.getAssociations()){
    				association_classes.add(asso.getChildClass());
    				
		    		//Check if association is already created, if it hasn't create it.
		    		ClassRelation tmprelation = new ClassRelation(asso.getChildClass(), child);
		    		if (!associationlist.contains(tmprelation)) {
		    			//[classname]<multiplicity>-<multiplicity>[classname]
		    			doc += "["+child.getName()+"]";
		    			doc += asso.getParentMultiplicity().getLowerBound().getValue()+".."+asso.getParentMultiplicity().getUpperBound().getValue();
		    			doc += "-";
		    			doc += asso.getChildMultiplicity().getLowerBound().getValue()+".."+asso.getChildMultiplicity().getUpperBound().getValue();
		    			doc += "["+asso.getChildClass().getName()+"]";
		    			doc += ", ";
		    			associationlist.add(tmprelation);
		    		} else {
		    			//We already have this association, so stop
		    			return;
		    		}
    			}		    	
		    	createClasses(association_classes);
		    }
    	}
    }

}
