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
import nl.han.ica.ap.nlp.model.Class;

/**
 * @author Joell & Boyd
 *
 */
public class TreeController {
	public ArrayList<Class> classes = new ArrayList<Class>();
	
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
		System.out.println(exporter.export(classes));
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
			Class existingParent = getClass(c, classes,null);
			Class existingChild = getClass(child,classes,null);
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
				if(getClass(existingChild, _classes, null) != null) {
					classes.remove(existingChild);
				}
			}
		}
	}	
	/**
	 * Finds the class in the classlist or in the attributes of a class.
	 * @param c The class to be compared.
	 * @param classlist The classlist which the class will be compared to.
	 * @param checkedClasses The (attribute)classes of the classlist which already are checked.
	 * @return The class that alreadt exists or null if the class doesn't exist.
	 */
	private Class getClass(Class c,ArrayList<Class> classlist,ArrayList<Class> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new ArrayList<Class>();
		}
		for(Class cInList : classlist) {
			if(cInList.getName().equalsIgnoreCase(c.getName()) || pluralExists(c.getName(),cInList)) {
				return cInList;
			} else if(cInList.getAssociations().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				Class result = getClass(c,extractChildClassFromAssociations(cInList.getAssociations()),checkedClasses);
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
	 * Check if the name of a new class/attribute already exists as plural 
	 * @param name The new name to be compared
	 * @param cInList The classes to be compared with
	 * @return True if the plural exists. False if the plural doesn't exists
	 */
	private boolean pluralExists(String name, Class cInList) {
		if(name.equalsIgnoreCase(cInList.getName() + "s")) {
			return true;
		} else if(cInList.getName().equalsIgnoreCase(name + "s")) {
			cInList.setName(name);
			return true;
		} else if(name.equalsIgnoreCase(cInList.getName() + "'s")) {
			return true;
		} else if(cInList.getName().equalsIgnoreCase(name + "'s")) {
			cInList.setName(name);
			return true;
		} else if(name.equalsIgnoreCase(getClassSingular(cInList.getName()))){
			cInList.setName(name);
			return true;
		} else if(cInList.getName().equalsIgnoreCase(getInputSingular(name))){			
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  Get the singular of the input noun.
	 *	@param inputLength The length of the new input zelfstandig naamwoord
	 */
	private String getInputSingular(String name) {
		int inputLength= name.length();
		
		if(name.endsWith("en")) {
			if(name.charAt(inputLength-3) == name.charAt(inputLength-4)) {
				name= name.substring(0, inputLength-3);	// Get stem of noun (- "nen" or "pen" etc).
			} else {
				name= name.substring(0, inputLength-2);	// Get stem of noun (- "en").
				inputLength= name.length()-1;
				// Check if letter before the second to last is a vowel. 
				if(name.charAt(inputLength-2) == 'a' || name.charAt(inputLength-2) == 'e' || name.charAt(inputLength-2) == 'o' || name.charAt(inputLength-2) == 'i' || name.charAt(inputLength-2) == 'u' || name.charAt(inputLength-2) == 'y') {
					return name;
				} else {
					char charToAdd= name.charAt(inputLength-1);
					name= name.substring(0, inputLength) + charToAdd + name.substring(inputLength);
				}	
			}
		}
		return name;
	}

	/**
	 *  Get the singular of the existing noun.
	 *	@param cInListLength The length of the existing zelfstandig naamwoord
	 */
	private String getClassSingular(String cInList) {
		int cInListLength= cInList.length();
		
		if(cInList.endsWith("en")) {
			if(cInList.charAt(cInListLength-3) == cInList.charAt(cInListLength-4)) {
				cInList= cInList.substring(0, cInListLength-3); // Get stem of noun (- "nen" or "pen" etc). 
			} else {
				cInList= cInList.substring(0, cInListLength-2); // Get stem of noun (- "en").
				cInListLength= cInList.length()-1;
				// Check if letter before the second to last is a vowel.
				if(cInList.charAt(cInListLength-2) == 'a' || cInList.charAt(cInListLength-2) == 'e' || cInList.charAt(cInListLength-2) == 'o' || cInList.charAt(cInListLength-2) == 'i' || cInList.charAt(cInListLength-2) == 'u'  || cInList.charAt(cInListLength-2) == 'y') {
					return cInList;
				} else {
					char charToAdd= cInList.charAt(cInListLength-1);
					cInList= cInList.substring(0, cInListLength) + charToAdd + cInList.substring(cInListLength);
				}
			}
		}
		return cInList;
	}
}

