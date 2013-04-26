package nl.han.ica.ap.nlp.export;

import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Class;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PowerDesignerXML {
	
	//XML standard
	public static final String attributename_xmi_type = "xmi:type";
	public static final String attributename_xmi_id = "xmi:id";
	public static final String attributename_xmi_version = "xmi:version";
	public static final String attributename_xmlns_xmi = "xmlns:xmi";
	public static final String attributename_xmlns_uml = "xmlns:uml";
	public static final String attributetype_uml_model = "uml:Model";
	public static final String attributetype_uml_association = "uml:Association";
	public static final String attributetype_uml_literalUnlimitedNatural = "uml:LiteralUnlimitedNatural";
	public static final String attributetype_uml_literalInteger = "uml:LiteralInteger";
	public static final String attributetype_uml_class = "uml:Class";
	public static final String attributename_navigableOwnedEnd = "navigableOwnedEnd";
	public static final String attributename_memberEnd = "memberEnd";
	public static final String attributename_value = "value";
	public static final String attributename_type = "type";
	public static final String attributename_name = "name";
	public static final String attributename_visibility = "visibility";
	public static final String attributename_association = "association";
	public static final String elementname_packagedElement = "packagedElement";
	public static final String elementname_ownedEnd = "ownedEnd";
	public static final String elementname_lowerValue = "lowerValue";
	public static final String elementname_upperValue = "upperValue";
	public static final String visibility_public = "public";
	public static final String default_modelname = "ObjectOrientedModel";
	public static final String default_xmi_version = "2.1";
	public static final String default_xmlns_xmi_url = "http://schema.omg.org/spec/XMI/2.1";
	public static final String default_xmlns_uml_url = "http://www.eclipse.org/uml2/2.1.0/UML";
	
	private static int associationid = 0;
	
	public static Element createRoot(Document doc){
		// Create and add a root element and add attributes.
	    Element root = doc.createElement(PowerDesignerXML.attributetype_uml_model);
	    root.setAttribute(PowerDesignerXML.attributename_name, PowerDesignerXML.default_modelname);
	    root.setAttribute(PowerDesignerXML.attributename_xmi_version, PowerDesignerXML.default_xmi_version);
	    root.setAttribute(PowerDesignerXML.attributename_xmlns_xmi, PowerDesignerXML.default_xmlns_xmi_url);
	    root.setAttribute(PowerDesignerXML.attributename_xmlns_uml, PowerDesignerXML.default_xmlns_uml_url);
	    return root;
	}
	
	/**
	 * Create a Class Element.
     * @param doc
	
}