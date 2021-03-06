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
import java.util.BitSet;
import java.util.Iterator;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTree;
import nl.han.ica.ap.nlp.NlpParser;
import nl.han.ica.ap.nlp.export.IExport;
import nl.han.ica.ap.nlp.listeners.ZelfstandignaamwoordListener;
import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Attribute;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.util.ErrorAlarm;

/**
 * @author Joell & Boyd
 *
 */
public class TreeController implements ANTLRErrorListener{
	public ArrayList<Class> classes = new ArrayList<Class>();
	public ArrayList<Attribute> attributesToAssign = new ArrayList<Attribute>();
	private boolean isWithoutGrammerError = true;
	
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
		try {
			walker.walk(listener, tree);
		} catch (Exception e) {			
			isWithoutGrammerError = false;
		}
		if(!isWithoutGrammerError) {
			ErrorAlarm alarm = new ErrorAlarm();
			alarm.soundAlarm();		
			classes.clear();
		} else if(exporter != null) {
			System.out.println(exporter.export(classes));
		}
	}

	/**
	 * Adds a new class-attribute or class-association pair to the existing Classlist which represents the model.
	 * @param c The Class to be added.
	 */
	public void addClass(Class c) {		
		Class existingParent = getClass(c.getName(), classes, null);
		addAssociationClass(c, existingParent);		
		existingParent = getClass(c.getName(), classes, null); //Maybe it exists after adding association.
		
		addAttributeClass(c, existingParent);
	}
	
	/**
	 * Add a attribute class pair to the classlist model.
	 * @param c Class with attribute(s)
	 * @param existingClass Possible existing class
	 */
	private void addAttributeClass(Class c, Class existingClass) {
		//Attributes
		for(Attribute attribute : c.getAttributes()) {
			//Are there any associations with the same name as this attribute? change them
			changeAssociationToAttribute(attribute, classes, null);
			
			//Class doesn't exist. Add new class with attribute.
			if(existingClass == null) {
				classes.add(c);
			//Class exists.
			}else if(existingClass != null){
				Attribute found_attribute = getAttribute(attribute.getName(), existingClass.getAttributes());
				//Overwrite attribute if it is already found in attributelist from class.
				if(found_attribute != null){
					int index = existingClass.getAttributes().indexOf(found_attribute);
					existingClass.getAttributes().set(index, attribute);
				//Attribute doesn't exist in class yet. Add it.
				} else {
					existingClass.addAttribute(attribute);
				}
			}
		}
	}
	
	/**
	 * Adds an association-class pair to the classlist model.
	 * @param c The class with an association.
	 * @param existingClass Class which is already in the model with the same name.
	 */
	private void addAssociationClass(Class c, Class existingClass) {
		//Associations
		for(Iterator<Association> it = c.getAssociations().iterator(); it.hasNext();){
			Association association = it.next();			
			Class child = association.getChildClass();
			Class existingChild = getClass(child.getName(), classes, null);
			Attribute queue_attribute = getAttribute(child.getName(), attributesToAssign);
			
			//Is there an attribute in the queue that has the same name?
			if(queue_attribute != null) {				
				//Does the class already exist?
				if(existingClass != null) {
					Attribute existing_attribute = getAttribute(queue_attribute.getName(), c.getAttributes());					
					//Does the class already have an attribute with the same name as the queue attribute, if so, overwrite it from queue.
					if(existing_attribute != null) {
						int index = existingClass.getAttributes().indexOf(existing_attribute);
						existingClass.getAttributes().set(index, queue_attribute);
					//Add copy of queue attribute to the existing class, remove from queue.
					} else {
						Attribute queue_attribute_clone = (Attribute) queue_attribute.clone();
						existingClass.addAttribute(queue_attribute_clone);						
						attributesToAssign.remove(queue_attribute);
					}
				//Add new class with copy of attribute, remove Association. Remove from queue.
				} else {
					Attribute queue_attribute_clone = (Attribute) queue_attribute.clone();
					c.addAttribute(queue_attribute_clone);
					it.remove(); //Remove association
					classes.add(c);					
					attributesToAssign.remove(queue_attribute);
				}
			} else {
				Attribute existing_attribute_from_classes = getAttribute(child.getName(), classes, null);
				
				//Is there an attribute in the classlist with the same name?
				if(existing_attribute_from_classes != null) {
					//Does the class already exist?
					if(existingClass != null) {
						Attribute existing_attribute_from_class = getAttribute(child.getName(), existingClass.getAttributes());
						//Does attribute already exist in class, if true, overwrite attribute.
						if(existing_attribute_from_class != null) {
							int index = existingClass.getAttributes().indexOf(existing_attribute_from_class);
							existingClass.getAttributes().set(index, existing_attribute_from_classes);
						//Add copy of attribute to existing class.
						} else {
							Attribute existing_attribute_from_classes_clone = (Attribute) existing_attribute_from_classes.clone();
							existingClass.addAttribute(existing_attribute_from_classes_clone);
							
							attributesToAssign.remove(existing_attribute_from_classes);
						}
					//Add new class with copy of attribute.
					} else {
						Attribute existing_attribute_from_classes_clone = (Attribute) existing_attribute_from_classes.clone();
						c.addAttribute(existing_attribute_from_classes_clone);
						classes.add(c);						
						attributesToAssign.remove(existing_attribute_from_classes);
					}
				} else {					
					//Parent and Child class don't exist yet, add both.
					if(existingClass == null && existingChild == null) {
						classes.add(c);
					//Parent already exists, child doesn't. Add association to existing parent.
					} else if(existingClass != null && existingChild == null) {
						existingClass.getAssociations().add(association);
					//Parent doesn't exist, child does. Remove child-class from classlist and add class with existing child as associationchild.
					} else if(existingClass == null && existingChild != null) {
						c.getAssociations().get(0).setChild(existingChild);		
						classes.remove(existingChild);
						classes.add(c);
					} else {
						association.setChild(existingChild);
						existingClass.getAssociations().add(association);
						ArrayList<Class> _classes = new ArrayList<Class>(classes);
						_classes.remove(existingChild);
						if(getClass(existingChild.getName(), _classes, null) != null) {
							classes.remove(existingChild);
						}
					}
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
	private Class getClass(String className, ArrayList<Class> classlist, ArrayList<Class> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new ArrayList<Class>();
		}
		for(Class cInList : classlist) {
			if(cInList.getName().equalsIgnoreCase(className) || cInList.pluralExists(className)) {
				return cInList;
			} else if(cInList.getAssociations().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				Class result = getClass(className, extractChildClassFromAssociations(cInList.getAssociations()), checkedClasses);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds the attribute in the classlist or in the attributes of a class.
	 * @param c The classname to be compared.
	 * @param classlist The classlist which the class will be compared to.
	 * @param checkedClasses The (attribute)classes of the classlist which already are checked.
	 * @return The class that alreadt exists or null if the class doesn't exist.
	 */
	private Attribute getAttribute(String attributeName, ArrayList<Class> classlist, ArrayList<Class> checkedClasses) {
		if(checkedClasses == null) {
			checkedClasses = new ArrayList<Class>();
		}
		for(Class cInList : classlist) {
			for(Attribute attributeInClass : cInList.getAttributes()) {
				if(attributeInClass.getName().equalsIgnoreCase(attributeName) || attributeInClass.pluralExists(attributeName)) {
					return attributeInClass;
				}
			}
			if(cInList.getAssociations().size() > 0 && !checkedClasses.contains(cInList)){		
				checkedClasses.add(cInList);
				Attribute result = getAttribute(attributeName, extractChildClassFromAssociations(cInList.getAssociations()), checkedClasses);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get an attribute if it is in the list, else return null.
	 * @param attributeName Name of attribute to check for.
	 * @param attributelist List of attributes.
	 * @return
	 */
	private Attribute getAttribute(String attributeName, ArrayList<Attribute> attributelist){
		for(Attribute attribute : attributelist) {
			if(attribute.getName().equalsIgnoreCase(attributeName) || attribute.pluralExists(attributeName)) {
				return attribute;
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
					cInList.addAttribute(new Attribute(attribute.getName(), attribute.getType()));
				}
			}
		}
		return null;
	}

	@Override
	public void reportAmbiguity(@NotNull Parser arg0, DFA arg1, int arg2,
			int arg3, @NotNull BitSet arg4, @NotNull ATNConfigSet arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportAttemptingFullContext(@NotNull Parser arg0,
			@NotNull DFA arg1, int arg2, int arg3, @NotNull ATNConfigSet arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportContextSensitivity(@NotNull Parser arg0,
			@NotNull DFA arg1, int arg2, int arg3, @NotNull ATNConfigSet arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syntaxError(Recognizer<?, ?> arg0, @Nullable Object arg1,
			int arg2, int arg3, String arg4, @Nullable RecognitionException arg5) {
		isWithoutGrammerError = false;	
	}
	
}

