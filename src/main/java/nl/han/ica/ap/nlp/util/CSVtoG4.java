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
package nl.han.ica.ap.nlp.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.han.ica.ap.nlp.CSVtoG4Lexer;
import nl.han.ica.ap.nlp.CSVtoG4Parser;
import nl.han.ica.ap.nlp.listeners.CSVRowListener;

public class CSVtoG4 {
	private IFile importfile;
	private IFile exportfile;
	
	public CSVtoG4() {
		String import_filepath = "res/werkwoorden.xml";
		String export_filepath = "src/main/antlr/imports/NlpWerkwoorden.g4";
		
		importfile = new File(import_filepath);
		exportfile = new File(export_filepath);
	}
	
	public void setExportFile(IFile file) {
		this.exportfile = file;
	}
	public void setImportFile(IFile file) {
		this.importfile = file;
	}
	
	public boolean export(){
		try {
			importfile.read();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		String csvfile = importfile.getContent();
		
		ANTLRInputStream 	input 	= new ANTLRInputStream(csvfile);
		CSVtoG4Lexer 		lexer 	= new CSVtoG4Lexer(input);
		CommonTokenStream 	tokens 	= new CommonTokenStream(lexer);
		CSVtoG4Parser 		parser 	= new CSVtoG4Parser(tokens);
		ParseTreeWalker 	walker 	= new ParseTreeWalker();
		
		ParseTree tree = parser.csv();
		
		CSVRowListener listener = new CSVRowListener(parser);
		
		walker.walk(listener, tree);
		
		ArrayList<String> conjugations = listener.getConjugations();
		
		String werkwoorden = "lexer grammar NlpWerkwoorden;\n\nWERKWOORD : (";
		
		for (int i=0; i<conjugations.size(); i++) {
			werkwoorden += "'"+conjugations.get(i)+"'|";
		}
		werkwoorden = werkwoorden.substring(0, werkwoorden.length()-1);
		werkwoorden += ");";
		
		exportfile.setContent(werkwoorden);

		return exportfile.write();
	}
}