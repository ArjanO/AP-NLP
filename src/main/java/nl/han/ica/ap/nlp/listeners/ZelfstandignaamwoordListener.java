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

import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.controller.VerbDirectionController;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.NlpBaseListener;
import nl.han.ica.ap.nlp.NlpParser.SamengesteldContext;
import nl.han.ica.ap.nlp.NlpParser.VerbaleconstituentContext;
import nl.han.ica.ap.nlp.NlpParser.VoegwoordContext;
import nl.han.ica.ap.nlp.NlpParser.WerkwoordContext;
import nl.han.ica.ap.nlp.NlpParser.ZelfstandignaamwoordContext;

public class ZelfstandignaamwoordListener extends NlpBaseListener {
	private TreeController controller;	
	private ZelfstandignaamwoordContext zelfstandignaamwoord;
	private boolean direction;
	private boolean start = false;
	
	public ZelfstandignaamwoordListener(TreeController controller) {
		this.controller = controller;
	}
	
	@Override
	public void enterZelfstandignaamwoord(ZelfstandignaamwoordContext ctx) {
		if(start) {
			if(!direction) {
				Class c = new Class(zelfstandignaamwoord.getText());
				c.addAssociation(new Class(ctx.getText()));					
				controller.addClass(c);
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
	public void enterWerkwoord(WerkwoordContext ctx) {
		VerbDirectionController vc = new VerbDirectionController();
		direction = vc.getDirection(ctx.getText());		
	}
	
	@Override
	public void exitVerbaleconstituent(VerbaleconstituentContext ctx) {
		start = false;
	}
}

