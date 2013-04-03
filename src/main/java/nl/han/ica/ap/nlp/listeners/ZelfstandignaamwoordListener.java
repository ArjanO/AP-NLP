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

import java.util.ArrayList;

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.NlpBaseListener;
import nl.han.ica.ap.nlp.NlpParser;
import nl.han.ica.ap.nlp.NlpParser.ZelfstandignaamwoordContext;

public class ZelfstandignaamwoordListener extends NlpBaseListener {
	NlpParser parser;
	App app;
	
	String direction;
	boolean direct; 		//Krijgt de waarde van de LR column uit het csv bestand
	
	public ZelfstandignaamwoordListener(NlpParser parser, App app) {
		this.parser = parser;
		this.app = app;
	}
	
	@Override
	public void enterZelfstandignaamwoord(ZelfstandignaamwoordContext ctx) {
		Class c = new Class(ctx.getText());
		ArrayList<String> znwlijst= new ArrayList<String>();
		znwlijst.add(ctx.getText());
	
		if(direct == false){
			direction= "RL";
		}else if(direct == true){
			direction= "LR";
		}else{
			direction= "";
		}
		
		app.classes.add(c);
	}
}
