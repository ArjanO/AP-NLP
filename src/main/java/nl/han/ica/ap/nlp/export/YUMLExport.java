package nl.han.ica.ap.nlp.export;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Class;

public class YUMLExport implements IExport{
	private ArrayList<ClassRelation> associationlist = new ArrayList<ClassRelation>();
	private String doc = "";
	
	public String export(ArrayList<Class> classes) {
		createClasses(classes);
		return doc.trim();
	}
	
	public String exportImage(ArrayList<Class> classes) {
		createClasses(classes);
		//http://yuml.me/diagram/scruffy;/class/
		return null;
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
		    			doc += "["+child.getName()+"]"+asso.getParentMultiplicity().getUpperBound().getValue()+".."+asso.getParentMultiplicity().getLowerBound().getValue()+"-"+asso.getChildMultiplicity().getUpperBound().getValue()+".."+asso.getChildMultiplicity().getLowerBound().getValue()+"["+asso.getChildClass().getName()+"]"+"\n";
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
