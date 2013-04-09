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
import java.util.HashMap;
import java.util.Map;

import nl.han.ica.ap.nlp.CSVtoG4BaseListener;
import nl.han.ica.ap.nlp.CSVtoG4Parser;
import nl.han.ica.ap.nlp.CSVtoG4Parser.ConjugationContext;
import nl.han.ica.ap.nlp.CSVtoG4Parser.CsvContext;
import nl.han.ica.ap.nlp.CSVtoG4Parser.DirectionContext;
import nl.han.ica.ap.nlp.CSVtoG4Parser.NameContext;
import nl.han.ica.ap.nlp.CSVtoG4Parser.RowContext;
import nl.han.ica.ap.nlp.util.CSVtoG4;

public class CSVRowListener extends CSVtoG4BaseListener {
	CSVtoG4Parser parser;
	private HashMap<String, Boolean> verbForms;
	private boolean direction;

	private ArrayList<String> conjugations = new ArrayList<String>();
	
	public CSVRowListener(CSVtoG4Parser parser) {
		this.parser = parser;
		verbForms =  new HashMap<String, Boolean>();
	}
	
	@Override
	public void enterConjugation(ConjugationContext ctx) {
		conjugations.add(ctx.getText());
		verbForms.put(ctx.getText(), direction);
	}
	
	@Override
	public void enterDirection(CSVtoG4Parser.DirectionContext ctx) {
		if(ctx.getText().equals("LR")) {
			direction = false;
		} else {
			direction = true;
		}
	}
	
	public boolean getDirection(String name) {		
		return verbForms.get(name);
	}
	
	public ArrayList<String> getConjugations(){
		return conjugations;
	}
}
