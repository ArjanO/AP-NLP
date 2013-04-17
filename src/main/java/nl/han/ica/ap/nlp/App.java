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
package nl.han.ica.ap.nlp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.listeners.ZelfstandignaamwoordListener;
import nl.han.ica.ap.nlp.NlpLexer;
import nl.han.ica.ap.nlp.NlpParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.export.IExport;
import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.model.IClass;


/**
 * @author Joell
 * 
 */
public class App {
	
	private static App app;
	private App(){}
	public static App getInstance() {
		if(app == null) {
			app = new App();
		}
		return app;
	}
	
	/**
	 * Starts parsing the input text to an uml diagram xml file.
	 */
	public void start() {
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		NlpLexer lexer = new NlpLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		NlpParser parser = new NlpParser(tokens);
		ParseTree tree = parser.tekst(); // begin parsing at init rule
		TreeController controller = new TreeController();
		controller.walkTree(tree, parser);
	}
		
	public static void main(String[] args) throws Exception {
		App app = App.getInstance();
		app.start();
	}
}