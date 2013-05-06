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

import java.util.TreeSet;
import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.controller.VerbDirectionController;
import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.NlpBaseListener;
import nl.han.ica.ap.nlp.NlpParser.BijwoordContext;
import nl.han.ica.ap.nlp.NlpParser.EnumeratieContext;
import nl.han.ica.ap.nlp.NlpParser.TelwoordContext;
import nl.han.ica.ap.nlp.NlpParser.VerbaleconstituentContext;
import nl.han.ica.ap.nlp.NlpParser.WerkwoordContext;
import nl.han.ica.ap.nlp.NlpParser.ZelfstandignaamwoordContext;

public class ZelfstandignaamwoordListener extends NlpBaseListener {
	private TreeController controller;	
	private ZelfstandignaamwoordContext zelfstandignaamwoord;
	private BijwoordContext bijwoord;
	private TelwoordContext telwoord;
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
				Class c = new Class(zelfstandignaamwoord.getText());
				addAssociationToClass(ctx, c);
			} else {
				Class c = new Class(ctx.getText());
				c.addAssociation(new Class(zelfstandignaamwoord.getText()));	
				controller.addClass(c);
			}		
		} else {
			zelfstandignaamwoord = ctx;
			start = true;
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
		if(telwoord != null) {
			Association a = new Association(new Class(ctx.getText()),null);
			if(hasMeaning(bijwoord)) {
				String bijwoordval = bijwoord.getText();
				if(bijwoordval.equals("maximaal")) {				
					a.getChildMultiplicity().setUpperBound(telwoord.getText());
				} else if(bijwoordval.equals("minimaal")) {
					a.getChildMultiplicity().setLowerBound(telwoord.getText());
				}
			} else {
				a.getChildMultiplicity().setLowerBound(telwoord.getText());
				a.getChildMultiplicity().setUpperBound(telwoord.getText());
			}
			c.getAssociations().add(a);
		} else {
			c.addAssociation(new Class(ctx.getText()));
		}
		controller.addClass(c);
	}
	
	

	/**
	 * Checks of the given bijwoord has an contextual meaning.
	 * @param bijwoord The bijwoord to be checked.
	 * @return True if it has an extra meaning, false if not.
	 */
	public boolean hasMeaning(BijwoordContext bijwoord) {
		if(bijwoord != null) {
			return keywords.contains(bijwoord.getText());
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
		bijwoord = ctx;
	}
	
	@Override
	public void enterTelwoord(TelwoordContext ctx) {
		telwoord = ctx;
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
	
}