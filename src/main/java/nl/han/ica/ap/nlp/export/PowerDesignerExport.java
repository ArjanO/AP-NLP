/**
 * Copyright (c) 2013 HAN University of Applied Sciences
 * Arjan Oortgiese
 * Boyd Hofman
 * Joëll Portier
 * Michiel Westerbeek
 * Tim Waalewijn
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package nl.han.ica.ap.nlp.export;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.util.File;
import nl.han.ica.ap.nlp.util.IFile;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class PowerDesignerExport implements IExport {
	
	private IFile file;
	public int associationid = 0;
	private ArrayList<Class> classlist = new ArrayList<Class>();
	private ArrayList<ClassRelation> associationlist = new ArrayList<ClassRelation>();
	
	public PowerDesignerExport() {
		String filepath = "target/Powerdesigner-xml-" + (System.currentTimeMillis()) + ".xml";
		file = new File(filepath);
	}
	
	public void setFile(IFile file) {
		this.file = file;
	}
	
	public Document exportDoc(ArrayList<Class> classes) {
		// Create a new document.
	    Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		Element root = PowerDesignerXML.createRoot(doc);
		doc.appendChild(root);
    	
	    //Create classes
	    createClasses(doc, root, classes);
	    
	    return doc;
	}
	
    public String export(ArrayList<Class> classes) {
    	
    	Document doc = exportDoc(classes);
		
		// Output the document to string.
	    DOMImplementation impl = doc.getImplementation();
	    DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");
        LSSerializer lsSerializer = implLS.createLSSerializer();
        
        LSOutput lsOutput = implLS.createLSOutput();
        lsOutput.setEncoding("UTF-8");
        Writer stringWriter = new StringWriter();
        lsOutput.setCharacterStream(stringWriter);
        lsSerializer.write(doc, lsOutput);
        
        String output = stringWriter.toString();
        
        //Write to file
        file.setContent(output);
        file.write();
        
        return file.getPath();
	}
    
    /**
     * Create all classes and associations and add them to the document.
     * @param doc
     * @param root
     * @param classes
     */
    private void createClasses(Document doc, Element root, ArrayList<Class> classes) {
    	if (classes.size() > 0) {
    		for(Class child : classes){

    			//Create classes if not already created.
	    		if (!classlist.contains(child)) {
	    			Element childClassElement = PowerDesignerXML.createClass(doc, child);
	    			root.appendChild(childClassElement);
	    			classlist.add(child);
	    		}
	    		
	    		//Create associations
	    		ArrayList<Class> association_classes = new ArrayList<Class>();
    			for(Association asso : child.getAssociations()){
    				association_classes.add(asso.getChildClass());
    				
		    		//Check if association is already created, if it hasn't create it.
		    		ClassRelation tmprelation = new ClassRelation(asso.getChildClass(), child);
		    		if (!associationlist.contains(tmprelation)) {
		    			Element association = PowerDesignerXML.createAssociation(doc, child, asso.getChildClass(), asso);
		    			root.appendChild(association);
		    			associationlist.add(tmprelation);
		    		} else {
		    			//We already have this association, so stop
		    			return;
		    		}
    			}		    	
		    	createClasses(doc, root, association_classes);
		    }
    	}
    }
    
    /**
	 * Get list of classes from recursive class.
	 * @param classes
	 * @param class_list
	 * @return
	 */
	public ArrayList<Class> getClassList(ArrayList<Class> classes, ArrayList<Class> class_list) {
		for(Class child_class : classes){
			if(!class_list.contains(child_class))
				class_list.add(child_class);
			
			if(child_class.getAssociations().size() > 0){
				for(Association asso : child_class.getAssociations()){
					if(!class_list.contains(asso.getChildClass())){
						class_list.add(asso.getChildClass());
						
						ArrayList<Class> child_class_list = new ArrayList<Class>();
						child_class_list.add(asso.getChildClass());
						getClassList(child_class_list, class_list);
					}
				}
			}
		}
		return class_list;
	}
	
	/**
	 * Get list of associations of recursive class.
	 * @param classes
	 * @param asso_list
	 * @return
	 */
	public ArrayList<ClassRelation> getAssociationList(ArrayList<Class> classes, ArrayList<ClassRelation> asso_list) {
		for(Class child_class : classes){
			if(child_class.getAssociations().size() > 0){
				for(Association asso : child_class.getAssociations()){
					ClassRelation tmp_relation = new ClassRelation(child_class, asso.getChildClass());
					if(!asso_list.contains(tmp_relation)){
						asso_list.add(tmp_relation);
						
						ArrayList<Class> child_class_list = new ArrayList<Class>();
						child_class_list.add(asso.getChildClass());
						getAssociationList(child_class_list, asso_list);
					}
				}	
			}
		}
		return asso_list;
	}
}
class ClassRelation {
	public Class class1;
	public Class class2;
	
	public ClassRelation(Class class1, Class class2) {
		this.class1 = class1;
		this.class2 = class2;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClassRelation)) {
			return super.equals(obj);
		}
		
		ClassRelation other = (ClassRelation)obj;
		
		if (class1 != other.class1) {
			return false;
		}
		
		if (class2 != other.class2) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + class1.hashCode();
		hash = hash * 31 + class2.hashCode();
		return hash;
	}
	
	@Override
	public String toString() {
		return class1.getName() + " -> " + class2.getName();
	}
}
