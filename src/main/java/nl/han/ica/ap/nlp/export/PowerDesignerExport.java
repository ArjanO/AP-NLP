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
	
	public String export(ArrayList<Class> classes){
		String filepath = "target/Powerdesigner-xml-" + (System.currentTimeMillis()) + ".xml";
		
		return export(classes, filepath);
	}
	
    public String export(ArrayList<Class> classes, String filepath) {
    	
    	// Create a new document.
	    Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	    
	    // Create and add a root element and add attributes.
	    Element root = doc.createElement("uml:Model");
	    root.setAttribute("name", "ObjectOrientedModel");
	    root.setAttribute("xmi:version", "2.1");
	    root.setAttribute("xmlns:xmi", "http://schema.omg.org/spec/XMI/2.1");
	    root.setAttribute("xmlns:uml", "http://www.eclipse.org/uml2/2.1.0/UML");
	    doc.appendChild(root);
    	
	    //Create classes
	    createClasses(doc, root, classes);
		
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
        
        return filepath;
	}
    
    private void createClasses(Document doc, Element root, ArrayList<Class> classes) {
    	if (classes.size() > 0) {
    		for(Class child : classes){

    			//Create classes if not already created.
	    		if (!classlist.contains(child)) {
	    			Element childClassElement = createClass(doc, child);
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
		    			Element association = createAssociation(doc, child, asso.getChildClass(), asso);
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
    
    private Element createClass(Document doc, Class element_class) {
    	Element packagedElementClass = null;
    	packagedElementClass = doc.createElement("packagedElement");
    	packagedElementClass.setAttribute("xmi:type", "uml:Class");
    	packagedElementClass.setAttribute("xmi:id", element_class.getName().toUpperCase());
    	packagedElementClass.setAttribute("name", element_class.getName());
    	return packagedElementClass;
    }
    
    private Element createAssociation(Document doc, Class class1, Class class2, Association asso) {
    	Element packagedElementAssociation = null;
	    packagedElementAssociation = doc.createElement("packagedElement");
	    packagedElementAssociation.setAttribute("xmi:type", "uml:Association");
	    packagedElementAssociation.setAttribute("xmi:id", "ASSOCIATION_" + associationid);
	    packagedElementAssociation.setAttribute("memberEnd", "ASSOCIATION_" + associationid + "OWNEDEND_1" + " " + "ASSOCIATION_" + associationid + "OWNEDEND_2");
	    packagedElementAssociation.setAttribute("navigableOwnedEnd", "ASSOCIATION_" + associationid + "OWNEDEND_2");
    	
		    Element ownedEnd1 = null;
	    	ownedEnd1 = doc.createElement("ownedEnd");
	    	ownedEnd1.setAttribute("xmi:id", "ASSOCIATION_" + associationid + "OWNEDEND_1");
	    	ownedEnd1.setAttribute("visibility", "public");
	    	ownedEnd1.setAttribute("type", class1.getName().toUpperCase());
	    	ownedEnd1.setAttribute("association", "ASSOCIATION_" + associationid);
	    	packagedElementAssociation.appendChild(ownedEnd1);
	    	
		    	Element upperValue1 = null;
		    	upperValue1 = doc.createElement("upperValue");
		    	upperValue1.setAttribute("xmi:id", "ASSOCIATION_" + associationid + "UPPERVALUE_1");
		    	upperValue1.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		    	upperValue1.setAttribute("value", asso.getParentMultiplicity().getUpperBound().getValue());
		    	ownedEnd1.appendChild(upperValue1);	
		    	
		    	Element lowerValue1 = null;
		    	lowerValue1 = doc.createElement("lowerValue");
		    	lowerValue1.setAttribute("xmi:id", "ASSOCIATION_" + associationid + "LOWERVALUE_1");
		    	lowerValue1.setAttribute("xmi:type", "LiteralInteger");
		    	lowerValue1.setAttribute("value", asso.getParentMultiplicity().getLowerBound().getValue());
		    	ownedEnd1.appendChild(lowerValue1);
	    	
	    	Element ownedEnd2 = null;
	    	ownedEnd2 = doc.createElement("ownedEnd");
	    	ownedEnd2.setAttribute("xmi:id", "ASSOCIATION_" + associationid + "OWNEDEND_2");
	    	ownedEnd2.setAttribute("visibility", "public");
	    	ownedEnd2.setAttribute("type", class2.getName().toUpperCase());
	    	ownedEnd2.setAttribute("association", "ASSOCIATION_" + associationid);
	    	packagedElementAssociation.appendChild(ownedEnd2);
	    	
		    	Element upperValue2 = null;
		    	upperValue2 = doc.createElement("upperValue");
		    	upperValue2.setAttribute("xmi:id", "ASSOCIATION_" + associationid + "UPPERVALUE_2");
		    	upperValue2.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		    	upperValue2.setAttribute("value", asso.getChildMultiplicity().getUpperBound().getValue());
		    	ownedEnd2.appendChild(upperValue2);
		    	
		    	Element lowerValue2 = null;
		    	lowerValue2 = doc.createElement("lowerValue");
		    	lowerValue2.setAttribute("xmi:id", "ASSOCIATION_" + associationid + "LOWERVALUE_2");
		    	lowerValue2.setAttribute("xmi:type", "LiteralInteger");
		    	lowerValue2.setAttribute("value", asso.getChildMultiplicity().getLowerBound().getValue());
		    	ownedEnd2.appendChild(lowerValue2);	
    	
		associationid++;
		
	    return packagedElementAssociation;
    }
    
    class ClassRelation {
    	private Class class1;
    	private Class class2;
    	
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
    }
}
