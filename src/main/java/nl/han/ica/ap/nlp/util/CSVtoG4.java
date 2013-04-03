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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.han.ica.ap.nlp.CSVtoG4Lexer;
import nl.han.ica.ap.nlp.CSVtoG4Parser;
import nl.han.ica.ap.nlp.listeners.CSVRowListener;

public class CSVtoG4 {
	
	public static ArrayList<String> conjugations = new ArrayList<String>();
	
	private static CSVtoG4 csvtog4;
	
	private CSVtoG4(){}
	public static CSVtoG4 getInstance() {
		if(csvtog4 == null) {
			csvtog4 = new CSVtoG4();
		}
		return csvtog4;
	}
	
	public static void main(String[] args) throws Exception {
		CSVtoG4 csvtog4 = CSVtoG4.getInstance();
		String csvfile = readFileAsString("res/werkwoorden.csv");
		ANTLRInputStream input = new ANTLRInputStream(csvfile);
		CSVtoG4Lexer lexer = new CSVtoG4Lexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CSVtoG4Parser parser = new CSVtoG4Parser(tokens);
		ParseTree tree = parser.csv();
		ParseTreeWalker walker = new ParseTreeWalker();
		CSVRowListener listener = new CSVRowListener(parser, csvtog4);
		walker.walk(listener, tree);
		
		String werkwoorden = "lexer grammar NlpWerkwoorden;\n\nWERKWOORD : (";
		for(int i=0; i<conjugations.size(); i++){
			werkwoorden += "'"+conjugations.get(i)+"'|";
		}
		werkwoorden = werkwoorden.substring(0, werkwoorden.length()-1);
		werkwoorden += ");";
		
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("src/main/antlr/imports/NlpWerkwoorden.g4"));
		    out.print(werkwoorden);
		}
		finally {
		    if (out != null) out.close();
		}
		System.out.println("Finished exporting werkwoorden to NlpWerkwoorden.g4");
	}
	
    private static String readFileAsString(String filePath) throws java.io.IOException{
    	StringBuffer fileData = new StringBuffer(1000);
    	BufferedReader reader = new BufferedReader(new FileReader(filePath));
    	char[] buf = new char[1024];
    	int numRead=0;
    	while((numRead=reader.read(buf)) != -1){
    		String readData = String.valueOf(buf, 0, numRead);
    		fileData.append(readData);
    		buf = new char[1024];
    	}
    	reader.close();
    	return fileData.toString();
    }
}