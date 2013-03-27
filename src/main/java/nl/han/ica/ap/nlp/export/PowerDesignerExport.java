package nl.han.ica.ap.nlp.export;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Class;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class PowerDesignerExport implements IExport {
	
	public String export(ArrayList<Class> classes){
		String filepath = "target/Powerdesigner-xml-" + (System.currentTimeMillis()/1000) + ".xml";
		return export(classes, filepath);
	}
	
    public String export(ArrayList<Class> classes, String filepath){
    	
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
	    Element packagedElement = null;
	    for (Class element : classes) {
		    packagedElement = doc.createElement("packagedElement");
		    packagedElement.setAttribute("xmi:type", "uml:Class");
		    packagedElement.setAttribute("xmi:id", element.getName().toUpperCase());
		    packagedElement.setAttribute("name", element.getName());
		    root.appendChild(packagedElement);
		}
		
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
}
