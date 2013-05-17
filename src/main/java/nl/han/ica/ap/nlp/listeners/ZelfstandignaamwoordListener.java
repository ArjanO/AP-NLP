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
package nl.han.ica.ap.nlp.listeners;

import java.lang.reflect.Type;
import java.util.TreeSet;
import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.controller.VerbDirectionController;
import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Attribute;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.model.IMultiplicity;
import nl.han.ica.ap.nlp.model.ParentMultiplicity;
import nl.han.ica.ap.nlp.NlpBaseListener;
import nl.han.ica.ap.nlp.NlpParser.BijwoordContext;
import nl.han.ica.ap.nlp.NlpParser.EnumeratieContext;
import nl.han.ica.ap.nlp.NlpParser.StringContext;
import nl.han.ica.ap.nlp.NlpParser.TelwoordContext;
import nl.han.ica.ap.nlp.NlpParser.VerbaleconstituentContext;
import nl.han.ica.ap.nlp.NlpParser.WerkwoordContext;
import nl.han.ica.ap.nlp.NlpParser.ZelfstandignaamwoordContext;
import nl.han.ica.ap.nlp.NlpParser.ZinContext;

public class ZelfstandignaamwoordListener extends NlpBaseListener {
	private TreeController controller;	
	private String zelfstandignaamwoord1;
	private String zelfstandignaamwoord2;
	private IMultiplicity parentMultiplicity;
	private String bijwoord;
	private String telwoord;
	private boolean direction;
	private boolean start = false;
	private TreeSet<String> keywords = new TreeSet<String>();
	
	public ZelfstandignaamwoordListener(TreeController controller) {
		this.controller = controller;
		fillKeyWords();
	}
	
	/**
	 * fills the keywords list with words with an extra contextual meaning 
	 */
	private void fillKeyWords() {
		keywords.add("maximaal");
		keywords.add("minimaal");
	}

	@Override
	public void enterZelfstandignaamwoord(ZelfstandignaamwoordContext ctx) {
		if(start) {
			if(!direction) {				
				Class c = new Class(zelfstandignaamwoord1);
				addAssociationToClass(ctx, c);
			} else {
				Class c = new Class(ctx.getText());
				c.addAssociation(new Class(zelfstandignaamwoord1));	
				controller.addClass(c);
			}
			zelfstandignaamwoord2 = ctx.getText();
		} else {
			zelfstandignaamwoord1 = ctx.getText();
			start = true;
			setParentMultiplicity(); 
		}		
	}

	/**
	 * Sets the parent multiplicity if a telwoord and proper bijwoord is available.
	 */
	private void setParentMultiplicity() {
		if(telwoord != null) {
			parentMultiplicity = new ParentMultiplicity();
			if(hasMeaning(bijwoord)) {
				if(bijwoord.equals("maximaal")) {
					parentMultiplicity.setUpperBound(telwoord);
				} else if(bijwoord.equals("minimaal")) {
					parentMultiplicity.setLowerBound(telwoord);
				}
			} else {
				parentMultiplicity.setLowerBound(telwoord);
				parentMultiplicity.setUpperBound(telwoord);
			}
		}
	}
	
	@Override
	public void exitZelfstandignaamwoord(ZelfstandignaamwoordContext ctx) {
		telwoord = null;
	}

	/**
	 * Adds an association to a class, which will be sent to the controller as Class-Association pair.
	 * If a bijwoord + telwoord is detected a multiplicity bound will modified from the default value to the
	 * value of the telwoord.
	 * @param ctx A name for a childclass
	 * @param c The class which gets a association
	 */
	private void addAssociationToClass(ZelfstandignaamwoordContext ctx, Class c) {
		Association a = new Association(new Class(ctx.getText()),null);
		if(parentMultiplicity != null) {
			a.getParentMultiplicity().setLowerBound(parentMultiplicity.getLowerBound().getValue());
			a.getParentMultiplicity().setUpperBound(parentMultiplicity.getUpperBound().getValue());
		}
		if(telwoord != null) {
			if(hasMeaning(bijwoord)) {
				if(bijwoord.equals("maximaal")) {
					a.getChildMultiplicity().setUpperBound(telwoord);
				} else if(bijwoord.equals("minimaal")) {
					a.getChildMultiplicity().setLowerBound(telwoord);
				}
			} else {
				a.getChildMultiplicity().setLowerBound(telwoord);
				a.getChildMultiplicity().setUpperBound(telwoord);
			}
		
		}
		c.getAssociations().add(a);
		controller.addClass(c);
	}

	/**
	 * Checks of the given bijwoord has an contextual meaning.
	 * @param bijwoord The bijwoord to be checked.
	 * @return True if it has an extra meaning, false if not.
	 */
	public boolean hasMeaning(String bijwoord) {
		if(bijwoord != null) {
			return keywords.contains(bijwoord);
		} else {
			return false;
		}
	}

	@Override
	public void enterWerkwoord(WerkwoordContext ctx) {
		VerbDirectionController vc = new VerbDirectionController();
		direction = vc.getDirection(ctx.getText());		
	}
	
	@Override
	public void enterBijwoord(BijwoordContext ctx) {
		bijwoord = ctx.getText().toLowerCase();
	}
	
	@Override
	public void enterTelwoord(TelwoordContext ctx) {
		telwoord = ctx.getText();
	}
	
	@Override
	public void exitVerbaleconstituent(VerbaleconstituentContext ctx) {
		start = false;
		bijwoord = null;
		telwoord = null;
	}
	
	@Override
	public void exitEnumeratie(EnumeratieContext ctx) {
		bijwoord = null;
	}	
	
	@Override
	public void exitZin(ZinContext ctx) {
		parentMultiplicity = null;
		zelfstandignaamwoord1 = null;
		zelfstandignaamwoord2 = null;
	}	
	
	@Override
	public void enterString(StringContext ctx) {
		addAttributeToController(String.class);
	}
	
	/**
	 * Adds a found attribute to the controller. 
	 * Either by adding it directly to a Class or have it send as a single attribute
	 * @param type Java type of the attribute.
	 */
	private void addAttributeToController(java.lang.Class<?> type) {
		if(zelfstandignaamwoord2 == null) {
			Class c = new Class(zelfstandignaamwoord1);
			c.addAttribute(new Attribute(zelfstandignaamwoord2,type));
			controller.addClass(c);
		} else {
			Attribute attr = new Attribute(zelfstandignaamwoord1, type);
			controller.addAttribute(attr);
		}
	}
}