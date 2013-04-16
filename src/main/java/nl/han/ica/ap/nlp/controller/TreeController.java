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
 * @author Joell
 *
 */
public class TreeController {
	public ArrayList<IClass> classes = new ArrayList<IClass>();
	
	public TreeController() {
		
	}
	
	public void walkTree(ParseTree tree,NlpParser parser) {
		ParseTreeWalker walker = new ParseTreeWalker();
		ZelfstandignaamwoordListener listener = new ZelfstandignaamwoordListener(this);
		walker.walk(listener, tree);
		IExport export = new PowerDesignerExport();		
		System.out.println(export.export(classes));
	}

	public void addClass(Class c) {
		for(Entry<IClass,Multiplicity[]> entry : transformToIClass(c.getAttributes()).entrySet()) {
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
	
	private IClass getClass(IClass c,ArrayList<IClass> classlist,TreeSet<IClass> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new TreeSet<IClass>();
		}
		for(IClass cInList : classlist) {
			if(cInList.getName().equalsIgnoreCase(c.getName()) || pluralExists(c.getName(),cInList)) {
				return cInList;
			} else if(cInList.getAttributes().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				IClass result = getClass(c,transformToIClass(new ArrayList<IAttribute>(cInList.getAttributes().keySet())),checkedClasses);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	private ArrayList<IClass> transformToIClass(ArrayList<IAttribute> attributes) {
		ArrayList<IClass> _classes = new ArrayList<IClass>();
		for(IAttribute attribute : attributes) {
			if(attribute instanceof IClass) {
				_classes.add((IClass) attribute);
			}
		}
		return _classes;
	}
	
	private TreeMap<IClass,Multiplicity[]> transformToIClass(TreeMap<IAttribute,Multiplicity[]> attributes) {
		TreeMap<IClass,Multiplicity[]> _classes = new TreeMap<IClass,Multiplicity[]>();
		for(Entry<IAttribute,Multiplicity[]> entry : attributes.entrySet()) {
			if(entry.getKey() instanceof IClass) {
				_classes.put((IClass) entry.getKey(),entry.getValue());
			}
		}
		return _classes;
	}

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

	// Krijg de stam van het ingevoerde zelfstandignaamwoord.
	private String getInputSingular(String name) {
		int inputLength= name.length();
		
		if(name.endsWith("en")) {
			if(name.charAt(inputLength-3) == name.charAt(inputLength-4)) {
				name= name.substring(0, inputLength-3);
			} else {
				name= name.substring(0, inputLength-2);
				inputLength= name.length()-1;
				if(name.charAt(inputLength-2) == 'a' || name.charAt(inputLength-2) == 'e' || name.charAt(inputLength-2) == 'o' || name.charAt(inputLength-2) == 'i' || name.charAt(inputLength-2) == 'u') {
					return name;
				} else {
					name= name.replace(name.substring(inputLength), name.substring(inputLength-1));
				}	
			}
		}
		return name;
	}

	// Krijg de stam van het het bestaande zelfstandignaamwoord.
	private String getClassSingular(String cInList) {
		int cInListLength= cInList.length();
		
		if(cInList.endsWith("en")) {
			if(cInList.charAt(cInListLength-3) == cInList.charAt(cInListLength-4)) {
				cInList= cInList.substring(0, cInListLength-3);
			} else {
				cInList= cInList.substring(0, cInListLength-2);
				cInListLength= cInList.length();
				if(cInList.charAt(cInListLength-2) == 'a' || cInList.charAt(cInListLength-2) == 'e' || cInList.charAt(cInListLength-2) == 'o' || cInList.charAt(cInListLength-2) == 'i' || cInList.charAt(cInListLength-2) == 'u') {
					return cInList;
				} else {
					cInList= cInList.replace(cInList.substring(cInListLength), cInList.substring(cInListLength-1));
				}
			}
		}
		return cInList;
	}
}

