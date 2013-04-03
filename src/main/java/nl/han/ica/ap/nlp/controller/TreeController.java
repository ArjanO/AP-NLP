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

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.NlpParser;
import nl.han.ica.ap.nlp.export.IExport;
import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.listeners.ZelfstandignaamwoordListener;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.model.IClass;

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
		ZelfstandignaamwoordListener listener = new ZelfstandignaamwoordListener(this,parser);
		walker.walk(listener, tree);
		IExport export = new PowerDesignerExport();		
		System.out.println(export.export(classes));
	}

	public void addClass(Class c) {
		if(notExist(c)) {
			classes.add(c);
		}		
	}	
	
	private boolean notExist(Class c) {
		for(IClass cInList : classes) {
			if(cInList.getName().equalsIgnoreCase(c.getName()) || pluralExists(c.getName(),cInList)) {
				return false;
			}
		}
		return true;
	}

	private boolean pluralExists(String name, IClass cInList) {
		if(name.equalsIgnoreCase(cInList.getName() + "s")) {
			return true;
		} else if(cInList.getName().equalsIgnoreCase(name + "s")) {
			cInList.setName(name);
		} else if(name.equalsIgnoreCase(cInList.getName() + "'s")) {
			return true;
		} else if(cInList.getName().equalsIgnoreCase(name + "'s")) {
			cInList.setName(name);
		} else if(name.equalsIgnoreCase(cInList.getName() + "en")) {
			return true;
		} else if(cInList.getName().equalsIgnoreCase(name + "en")) {
			cInList.setName(name);
		}
		return false;
	}
}
