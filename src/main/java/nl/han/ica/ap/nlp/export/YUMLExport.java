package nl.han.ica.ap.nlp.export;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Class;

public class YUMLExport implements IExport{
	private ArrayList<ClassRelation> associationlist = new ArrayList<ClassRelation>();
	private String doc = "";
	
	public String exportSource(ArrayList<Class> classes) {
		createClasses(classes);
		doc = doc.trim();
		doc = doc.substring(0, doc.length()-1);
		return doc;
	}
	
	//http://yuml.me/diagram/scruffy;/class/
	public String export(ArrayList<Class> classes) {
		String filepath = "target/YUML-" + (System.currentTimeMillis()) + ".png";
		
		createClasses(classes);
		doc = doc.trim();
		doc = doc.substring(0, doc.length()-1);
		System.out.println(doc);
		
		//Download image
		URL url = null;
		try {
			url = new URL("http://yuml.me/diagram/plain;/class/"+doc);
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL.");
			e.printStackTrace();
		}
		InputStream in = null;
		ByteArrayOutputStream out = null;
		
		try {
			in = new BufferedInputStream(url.openStream());
			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			
			while (-1!=(n=in.read(buf))) {
			   out.write(buf, 0, n);
			}
			out.close();
			in.close();
		} catch (IOException e) {
			System.out.println("Can't download file.");
			e.printStackTrace();
		}

		byte[] response = out.toByteArray();
		
		//Save image
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
	    try {
			fos.write(response);
		} catch (IOException e) {
			System.out.println("Can't write to file.");
			e.printStackTrace();
		}
	    try {
			fos.close();
		} catch (IOException e) {
			System.out.println("Can't close file.");
			e.printStackTrace();
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
