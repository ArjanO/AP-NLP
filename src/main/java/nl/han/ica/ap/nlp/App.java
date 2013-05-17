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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import nl.han.ica.ap.nlp.NlpLexer;
import nl.han.ica.ap.nlp.NlpParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.export.ExportFactory;
import nl.han.ica.ap.nlp.export.IExport;
import nl.han.ica.ap.nlp.export.YUMLExport;
import nl.han.ica.ap.nlp.util.File;

/**
 * @author Joell
 * 
 */
public class App {
	
	private TreeController controller;
	private static App app;
	public String exportName;
	private IExport export;
	private ExportFactory exportFactory;
	
	private App(){
		exportFactory = new ExportFactory();
	}
	
	public static App getInstance() {
		if(app == null) {
			app = new App();
		}
		return app;
	}
	
	/**
	 * Sets the exporter to be used
	 * @param method The exportname.
	 */
	public void setExportType(String method) {
		//Load the Class.
		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
		String classNameToBeLoaded = "nl.han.ica.ap.nlp.export."+method+"Export";
		java.lang.Class<?> myClass = null;
		try {
			myClass = myClassLoader.loadClass(classNameToBeLoaded);
			if(myClass.newInstance() instanceof IExport){
				export = (IExport) myClass.newInstance();
			}else{
				throw new Exception("Not an instance of IExport");
			}
		} catch (Exception e) {
			System.out.println("Invalid exporter name. Switching to default exporter.");
			export = new YUMLExport();
		}
		export = exportFactory.createExport(method);
	}
	
	/**
	 * Give the user an option to set the exporter.
	 * @param method The exportername of the chosen exportmethod.
	 */
	public void selectExporter() {		
		System.out.println("Please choose your export method. (PowerDesigner, YUML):");
		Scanner s = new Scanner(System.in);
		String method = s.next();
		setExportType(method);
	}
	
	/**
	 * Starts the application with a given textfile to be parsed.
	 * @param File to be parsed
	 */
	private void start(File inputfile) {
		try {
			if(inputfile.read()) {
				start(inputfile.getContent());
			} else {
				start();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found, switching to default input.");
			start();
		}
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
		export = new YUMLExport();
		System.out.println("Give your input-sentences:");
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(System.in);
		} catch (IOException e) {
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
	//	parser.setErrorHandler(new ErrorHandler());
		controller = new TreeController();
		parser.addErrorListener(controller);
		ParseTree tree = parser.tekst(); // begin parsing at init rule
		controller.walkTree(tree, parser, export);
	}
	
	/**
	 * 
	 * @return The TreeController for this application.
	 */
	public TreeController getController() {
		return controller;
	}
		
	public static void main(String[] args) throws Exception {
		App app = App.getInstance();
		OptionsHandler opthandler = new OptionsHandler(args,app);		
		if(opthandler.getInputfile() != null) {
			app.start(opthandler.getInputfile());
		} else {
			app.start();
		}
		System.out.println("Done!");
	}		
}