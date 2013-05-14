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
package nl.han.ica.ap.nlp.controller;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTree;
import nl.han.ica.ap.nlp.NlpParser;
import nl.han.ica.ap.nlp.export.IExport;
import nl.han.ica.ap.nlp.listeners.ZelfstandignaamwoordListener;
import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Attribute;
import nl.han.ica.ap.nlp.model.Class;

/**
 * @author Joell & Boyd
 *
 */
public class TreeController {
	public ArrayList<Class> classes = new ArrayList<Class>();
	private ArrayList<Attribute> attributesToAssign = new ArrayList<Attribute>();
	
	public TreeController() {		
	}
	
	/**
	 * Walks the tree with a ZelfstandignaamwoordListener
	 * @param tree The tree of the input text
	 * @param parser The parser which parses the input text
	 */
	public void walkTree(ParseTree tree, NlpParser parser, IExport exporter) {
		ParseTreeWalker walker = new ParseTreeWalker();
		ZelfstandignaamwoordListener listener = new ZelfstandignaamwoordListener(this);
		walker.walk(listener, tree);
		
		if(exporter != null) {
			System.out.println(exporter.export(classes));
		}
	}

	/**
	 * Adds a new class-attribute pair to the existing Classlist.
	 * If the class of a class-attribute already exists the class-attribute pair will be added as a attribute.
	 * If the attribute of a class-attribute already exists the class will be added to the classlist and the 
	 * attribute will be replaced with the existing attribute.
	 * If the class and the attribute of a class-attribute already exists, the existing attribute will be added to the existing class.
	 * @param c The Class to be added.
	 */
	public void addClass(Class c) {
		for(Association association : c.getAssociations()) {
			Class child = association.getChildClass();
			Class existingParent = getClass(c.getName(), classes,null);
			Class existingChild = getClass(child.getName(),classes,null);
			if(existingParent == null && existingChild == null){
				classes.add(c);			
			} else if(existingParent != null && existingChild == null) {
				existingParent.getAssociations().add(association);
			} else if(existingParent == null && existingChild != null) {
				c.getAssociations().get(0).setChild(existingChild);				
				classes.remove(existingChild);
				classes.add(c);
			} else {
				association.setChild(existingChild);
				existingParent.getAssociations().add(association);
				ArrayList<Class> _classes = new ArrayList<Class>(classes);
				_classes.remove(existingChild);
				if(getClass(existingChild.getName(), _classes, null) != null) {
					classes.remove(existingChild);
				}
			}
		}
	}	
	/**
	 * Finds the class in the classlist or in the attributes of a class.
	 * @param c The classname to be compared.
	 * @param classlist The classlist which the class will be compared to.
	 * @param checkedClasses The (attribute)classes of the classlist which already are checked.
	 * @return The class that alreadt exists or null if the class doesn't exist.
	 */
	private Class getClass(String className,ArrayList<Class> classlist,ArrayList<Class> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new ArrayList<Class>();
		}
		for(Class cInList : classlist) {
			if(cInList.getName().equalsIgnoreCase(className) || cInList.pluralExists(className)) {
				return cInList;
			} else if(cInList.getAssociations().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				Class result = getClass(className,extractChildClassFromAssociations(cInList.getAssociations()),checkedClasses);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the child class from every association of a parent class and collect them in an arraylist.
	 * @param associations All the associations of a parent class.
	 * @return The child classes of the associations.
	 */
	public ArrayList<Class> extractChildClassFromAssociations(ArrayList<Association> associations) {
		ArrayList<Class> childClasses = new ArrayList<Class>();
		for(Association a : associations) {
			childClasses.add(a.getChildClass());
		}
		return childClasses;
	}
		
	/**
	 * Adds this attribute doesn't already exist as association, it will be added to the waiting list.
	 * If it already exists, all associations will be replaced with attributes.
	 * @param attr
	 */
	public void addAttribute(Attribute attr) {
		if(getClass(attr.getName(), classes, null) != null) {
			changeAssociationToAttribute(attr,classes,null);
		} else {
			attributesToAssign.add(attr);
		}
	}
		
	/**
	 * Changes all associations that match a given attribute to attributes.
	 * All associations that match the given attribute will be removed from a class. A new attribute will be added to that class.
	 * 
	 * @param attribute The element to be switched from association to attribute.
	 * @param classlist The class graph to be searched.
	 * @param checkedClasses The classes which already are checked.
	 * @return 
	 */
	private Class changeAssociationToAttribute(Attribute attribute,ArrayList<Class> classlist,ArrayList<Class> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new ArrayList<Class>();
		}
		for(Class cInList : classlist) {
			if(cInList.getName().equalsIgnoreCase(attribute.getName()) || cInList.pluralExists(attribute.getName())) {
				return cInList;
			} else if(cInList.getAssociations().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				Class result = changeAssociationToAttribute(attribute,extractChildClassFromAssociations(cInList.getAssociations()),checkedClasses);
				if(result != null) {
					cInList.removeAssociation(result);
					cInList.addAttribute(attribute);
				}
			}
		}
		return null;
	}
	
}

