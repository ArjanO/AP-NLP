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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.IAttribute;
import nl.han.ica.ap.nlp.model.IClass;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class PowerDesignerExport implements IExport {
	
	public String export(ArrayList<IClass> classes){
		String filepath = "target/Powerdesigner-xml-" + (System.currentTimeMillis()/1000) + ".xml";
		return export(classes, filepath);
	}
	
    public String export(ArrayList<IClass> classes, String filepath){
    	
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
	    
	    //Create Associations
	    createAssociations(doc, root, classes);
		
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
        
        //Write file
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(output);
            out.close();
        }catch (IOException e){ 
           System.out.println("Can't write file.");
        }
        
        return filepath;
	}
    
    private void createClasses(Document doc, Element root, ArrayList<IClass> classes){
    	if(classes.size() > 0){
	    	for (IAttribute element_class : classes) {
			    root.appendChild(createClass(doc, element_class));

			    if(element_class instanceof IClass){
			    	ArrayList<IClass> attribute_classes = new ArrayList<IClass>();
			    	ArrayList<IAttribute> class_attributes = ((IClass)element_class).getAttributes();
			    	for (IAttribute attribute : class_attributes) {
						if(attribute instanceof IClass){
							attribute_classes.add(((IClass)attribute));
						}
					}
			    	createClasses(doc, root, attribute_classes);
			    }
			}
    	}else{
    		return;
    	}
    }
    
    private Element createClass(Document doc, IAttribute element_class){
    	Element packagedElementClass = null;
    	packagedElementClass = doc.createElement("packagedElement");
    	packagedElementClass.setAttribute("xmi:type", "uml:Class");
    	packagedElementClass.setAttribute("xmi:id", element_class.getName().toUpperCase());
    	packagedElementClass.setAttribute("name", element_class.getName());
    	return packagedElementClass;
    }
    
    private void createAssociations(Document doc, Element root, ArrayList<IClass> classes){
    	//foreach class
    	for (int i = 0; i < classes.size(); i++) {
			IClass element_class = classes.get(i);
			
			//foreach element
			for (int j = 0; j < element_class.getAttributes().size(); j++) {
				IAttribute element_attribute = element_class.getAttributes().get(j);
				if(element_attribute instanceof IClass){
					IClass element_attributeclass = (IClass)element_attribute;
					root.appendChild(createAssociation(doc, element_class, element_attributeclass));
				}
			}
		}
    }
    
    private Element createAssociation(Document doc, IClass class1, IClass class2){
    	Element packagedElementAssociation = null;
	    packagedElementAssociation = doc.createElement("packagedElement");
	    packagedElementAssociation.setAttribute("xmi:type", "uml:Association");
	    packagedElementAssociation.setAttribute("xmi:id", "ASSOCIATION_" + "TEST");
	    packagedElementAssociation.setAttribute("name", "Association_" + "TEST");
	    packagedElementAssociation.setAttribute("memberEnd", "OWNEDEND_" + class1.getName().toUpperCase() + " " + "OWNEDEND_" + class2.getName().toUpperCase());
	    packagedElementAssociation.setAttribute("navigableOwnedEnd", "OWNEDEND_" + class2.getName().toUpperCase());
    	
		    Element ownedEnd1 = null;
	    	ownedEnd1 = doc.createElement("ownedEnd");
	    	ownedEnd1.setAttribute("xmi:id", "OWNEDEND_" + class1.getName().toUpperCase());
	    	ownedEnd1.setAttribute("name", "");
	    	ownedEnd1.setAttribute("visibility", "public");
	    	ownedEnd1.setAttribute("type", class1.getName().toUpperCase());
	    	ownedEnd1.setAttribute("association", "ASSOCIATION_" + class1.getName().toUpperCase());
	    	packagedElementAssociation.appendChild(ownedEnd1);
	    	
		    	Element upperValue1 = null;
		    	upperValue1 = doc.createElement("upperValue");
		    	upperValue1.setAttribute("xmi:id", "UPPERVALUE_" + class1.getName().toUpperCase());
		    	upperValue1.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		    	upperValue1.setAttribute("value", "1");
		    	ownedEnd1.appendChild(upperValue1);	
		    	
		    	Element lowerValue1 = null;
		    	lowerValue1 = doc.createElement("lowerValue");
		    	lowerValue1.setAttribute("xmi:id", "LOWERVALUE_" + class1.getName().toUpperCase());
		    	lowerValue1.setAttribute("xmi:type", "LiteralInteger");
		    	ownedEnd1.appendChild(lowerValue1);
	    	
	    	Element ownedEnd2 = null;
	    	ownedEnd2 = doc.createElement("ownedEnd");
	    	ownedEnd2.setAttribute("xmi:id", "OWNEDEND_" + class2.getName().toUpperCase());
	    	ownedEnd2.setAttribute("name", "");
	    	ownedEnd2.setAttribute("visibility", "public");
	    	ownedEnd2.setAttribute("type", class2.getName().toUpperCase());
	    	ownedEnd2.setAttribute("association", "ASSOCIATION_" + class2.getName().toUpperCase());
	    	packagedElementAssociation.appendChild(ownedEnd2);	
	    	
		    	Element upperValue2 = null;
		    	upperValue2 = doc.createElement("upperValue");
		    	upperValue2.setAttribute("xmi:id", "UPPERVALUE_" + class2.getName().toUpperCase());
		    	upperValue2.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
		    	upperValue2.setAttribute("value", "1");
		    	ownedEnd2.appendChild(upperValue2);
		    	
		    	Element lowerValue2 = null;
		    	lowerValue2 = doc.createElement("lowerValue");
		    	lowerValue2.setAttribute("xmi:id", "LOWERVALUE_" + class2.getName().toUpperCase());
		    	lowerValue2.setAttribute("xmi:type", "LiteralInteger");
		    	ownedEnd2.appendChild(lowerValue2);	
    	
	    return packagedElementAssociation;
    }
}
