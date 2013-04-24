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
     * @param doc     * @param element_class     * @return     */	public static Element createClass(Document doc, Class element_class) {		Element packagedElementClass = null;		packagedElementClass = doc.createElement(elementname_packagedElement);		packagedElementClass.setAttribute(attributename_xmi_type, attributetype_uml_class);		packagedElementClass.setAttribute(attributename_xmi_id, element_class.getName().toUpperCase());		packagedElementClass.setAttribute(attributename_name, element_class.getName());		return packagedElementClass;	}	/**	 * Create a association element between two classes.	 * @param doc	 * @param class1	 * @param class2	 * @param asso	 * @return	 */	public static Element createAssociation(Document doc, Class class1, Class class2, Association asso) {			Element packagedElementAssociation = createPackagedElementAssociation(doc);			Element ownedEnd1 = createOwnedEnd(doc, class1.getName().toUpperCase(), 1);		packagedElementAssociation.appendChild(ownedEnd1);				Element upperValue1 = createValue(doc, asso.getParentMultiplicity().getUpperBound().getValue(), 1, true);			ownedEnd1.appendChild(upperValue1);					Element lowerValue1 = createValue(doc, asso.getParentMultiplicity().getLowerBound().getValue(), 1, false);			ownedEnd1.appendChild(lowerValue1);			Element ownedEnd2 = createOwnedEnd(doc, class2.getName().toUpperCase(), 2);		packagedElementAssociation.appendChild(ownedEnd2);				Element upperValue2 = createValue(doc, asso.getChildMultiplicity().getUpperBound().getValue(), 2, true);			ownedEnd2.appendChild(upperValue2);					Element lowerValue2 = createValue(doc, asso.getChildMultiplicity().getLowerBound().getValue(), 2, false);			ownedEnd2.appendChild(lowerValue2);		associationid++;		return packagedElementAssociation;	}	/**	 * Create Upper or LowerValue element.	 * @param doc	 * @param multiplicity	 * @param id	 * @param upperValue If true, uppervalue, if false, lowervalue.	 * @return	 */	public static Element createValue(Document doc, String multiplicity, int id, boolean upperValue){		String element_name = (upperValue) ? elementname_upperValue : elementname_lowerValue;		String id_name = (upperValue) ? "UPPERVALUE_"+id : "LOWERVALUE_"+id;			Element valueElement = null;		valueElement = doc.createElement(element_name);		valueElement.setAttribute(attributename_xmi_id, "ASSOCIATION_" + associationid + id_name);		valueElement.setAttribute(attributename_xmi_type, attributetype_uml_literalUnlimitedNatural);		valueElement.setAttribute(attributename_value, multiplicity);		return valueElement;	}	/**	 * Create a OwnedEnd Element	 * @param doc	 * @param type To which class does this OwnedEnd belong.	 * @param id	 * @return Element	 */	public static Element createOwnedEnd(Document doc, String type, int id){		Element ownedEnd = null;		ownedEnd = doc.createElement(elementname_ownedEnd);		ownedEnd.setAttribute(attributename_xmi_id, "ASSOCIATION_" + associationid + "OWNEDEND_"+id);		ownedEnd.setAttribute(attributename_visibility, visibility_public);		ownedEnd.setAttribute(attributename_type, type);		ownedEnd.setAttribute(attributename_association, "ASSOCIATION_" + associationid);		return ownedEnd;	}	/**	 * Create a PackagedElement-Association Element.	 * @param doc	 * @return Element	 */	public static Element createPackagedElementAssociation(Document doc){		Element packagedElementAssociation = null;	    packagedElementAssociation = doc.createElement(elementname_packagedElement);	    packagedElementAssociation.setAttribute(attributename_xmi_type, attributetype_uml_association);	    packagedElementAssociation.setAttribute(attributename_xmi_id, "ASSOCIATION_" + associationid);	    packagedElementAssociation.setAttribute(attributename_memberEnd, "ASSOCIATION_" + associationid + "OWNEDEND_1" + " " + "ASSOCIATION_" + associationid + "OWNEDEND_2");	    packagedElementAssociation.setAttribute(attributename_navigableOwnedEnd, "ASSOCIATION_" + associationid + "OWNEDEND_2");	    return packagedElementAssociation;	}
	
}