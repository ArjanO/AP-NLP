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
import nl.han.ica.ap.nlp.NlpLexer;
import nl.han.ica.ap.nlp.NlpParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.errorhandler.ErrorHandler;




/**
 * @author Joell
 * 
 */
public class App {
	
	private TreeController controller;
	private static App app;
	private App(){}
	public static App getInstance() {
		if(app == null) {
			app = new App();
		}
		return app;
	}
	
	/**
	 * Starts the application with a given string to be parsed.
	 */
	public void start(String text) {
		ANTLRInputStream input = null;
		input = new ANTLRInputStream(text);
		parseInput(input);
	}
	
	/**
	 * Starts the application with input from the console
	 */
	public void start() {
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(System.in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parseInput(input);
	}
	
	/**
	 * Parses the input to an uml diagram xml file.
	 * @param input The input to be parsed
	 */
	private void parseInput(ANTLRInputStream input) {
		NlpLexer lexer = new NlpLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		NlpParser parser = new NlpParser(tokens);
		parser.setErrorHandler(new ErrorHandler());
		ParseTree tree = parser.tekst(); // begin parsing at init rule
		controller = new TreeController();
		controller.walkTree(tree, parser);
	}
	
	public TreeController getController() {
		return controller;
	}
		
	public static void main(String[] args) throws Exception {
		App app = App.getInstance();
		app.start();
	}
}