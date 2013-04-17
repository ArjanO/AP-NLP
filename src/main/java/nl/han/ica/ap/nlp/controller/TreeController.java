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
import java.util.Arrays;
import java.util.TreeMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.antlr.v4.runtime.tree.ParseTreeWalker;


import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.rules.ExternalResource;

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.NlpParser;
import nl.han.ica.ap.nlp.export.IExport;
import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.listeners.ZelfstandignaamwoordListener;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.model.IAttribute;
import nl.han.ica.ap.nlp.model.IClass;
import nl.han.ica.ap.nlp.model.Multiplicity;

/**
 * @author Joell & Boyd
 *
 */
public class TreeController {
	public ArrayList<IClass> classes = new ArrayList<IClass>();
	
	public TreeController() {
		
	}
	
	/**
	 * Walks the tree with a ZelfstandignaamwoordListener
	 * @param tree The tree of the input text
	 * @param parser The parser which parses the input text
	 */
	public void walkTree(ParseTree tree,NlpParser parser) {
		ParseTreeWalker walker = new ParseTreeWalker();
		ZelfstandignaamwoordListener listener = new ZelfstandignaamwoordListener(this);
		walker.walk(listener, tree);
		IExport export = new PowerDesignerExport();		
		System.out.println(export.export(classes));
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
		for(Entry<IClass,Multiplicity[]> entry : convertToIClass(c.getAttributes()).entrySet()) {
			IClass attribute = entry.getKey();
			IClass existingClass = getClass(c, classes,null);
			IClass existingAttribute = getClass(attribute,classes,null);
			if(existingClass == null && existingAttribute == null){
				classes.add(c);			
			} else if(existingClass != null && existingAttribute == null) {
				existingClass.addAttribute(attribute,entry.getValue());
			} else if(existingClass == null && existingAttribute != null) {
				c.getAttributes().put(existingAttribute, entry.getValue());
				c.getAttributes().remove(attribute);
				classes.remove(existingAttribute);
				classes.add(c);
			} else {
				existingClass.addAttribute(existingAttribute,entry.getValue());
				if(classes.size() > 1) {
					classes.remove(existingAttribute);
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
	private IClass getClass(IClass c,ArrayList<IClass> classlist,ArrayList<IClass> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new ArrayList<IClass>();
		}
		for(IClass cInList : classlist) {
			if(cInList.getName().equalsIgnoreCase(c.getName()) || pluralExists(c.getName(),cInList)) {
				return cInList;
			} else if(cInList.getAttributes().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				IClass result = getClass(c,convertToIClass(new ArrayList<IAttribute>(cInList.getAttributes().keySet())),checkedClasses);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Converts an IAttribute arraylist to a IClass arraylist.
	 * @param attributes The IAttribute arraylist to be converted.
	 * @return The converted IClass list.
	 */
	private ArrayList<IClass> convertToIClass(ArrayList<IAttribute> attributes) {
		ArrayList<IClass> _classes = new ArrayList<IClass>();
		for(IAttribute attribute : attributes) {
			if(attribute instanceof IClass) {
				_classes.add((IClass) attribute);
			}
		}
		return _classes;
	}
	
	/**
	 * Converts an IAttribute TreeMap to a IClass TreeMap.
	 * @param attributes The IAttribute TreeMap to be converted.
	 * @return The converted IClass map.
	 */
	private TreeMap<IClass,Multiplicity[]> convertToIClass(TreeMap<IAttribute,Multiplicity[]> attributes) {
		TreeMap<IClass,Multiplicity[]> _classes = new TreeMap<IClass,Multiplicity[]>();
		for(Entry<IAttribute,Multiplicity[]> entry : attributes.entrySet()) {
			if(entry.getKey() instanceof IClass) {
				_classes.put((IClass) entry.getKey(),entry.getValue());
			}
		}
		return _classes;
	}

	/**
	 * Check if the name of a new class/attribute already exists as plural 
	 * @param name The new name to be compared
	 * @param cInList The classes to be compared with
	 * @return True if the plural exists. False if the plural doesn't exists
	 */
	private boolean pluralExists(String name, IClass cInList) {
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
	 *  Get the word stem of the input noun.
	 *
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
	 *  Get the word stem of the existing noun.
	 *
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

