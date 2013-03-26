package nl.han.ica.ap.nlp.export;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Class;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class PowerDesignerExport implements IExport {
	
    public String export(ArrayList<Class> classes){
    	
    	// Create a new document.
	    Document doc = null;
	    
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	    
	    // Create and add a root element and an attribute.
	    Element root = doc.createElement("uml:Model");
	    doc.appendChild(root);
	    root.setAttribute("xmi:version", "2.1");
	    root.setAttribute("xmlns:xmi", "http://schema.omg.org/spec/XMI/2.1");
	    root.setAttribute("xmlns:uml", "http://www.eclipse.org/uml2/2.1.0/UML");
	    root.setAttribute("xmi:id", "MODEL1");
	    root.setAttribute("name", "ObjectOrientedModel");
    	
	    Element packagedElement = null;
	    
	    for (Class element : classes) {
		    packagedElement = doc.createElement("packagedElement");
		    packagedElement.setAttribute("xmi:type", "uml:Class");
		    packagedElement.setAttribute("xmi:id", element.getName().toUpperCase());
		    packagedElement.setAttribute("name", element.getName());
		    root.appendChild(packagedElement);
		}
		
		// Output the document.
	    DOMImplementationLS domImplementationLS = (DOMImplementationLS) doc.getImplementation();
        LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
        String output = lsSerializer.writeToString(doc);
        
        return output;
	}
}
