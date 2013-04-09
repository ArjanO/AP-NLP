package nl.han.ica.ap.nlp.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import nl.han.ica.ap.nlp.CSVtoG4Lexer;
import nl.han.ica.ap.nlp.CSVtoG4Parser;
import nl.han.ica.ap.nlp.listeners.CSVRowListener;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class VerbDirectionController {

	public boolean getDirection(String name) {
		String csvfile = readFileAsString("res/werkwoorden.csv");	
		ANTLRInputStream 	input 	= new ANTLRInputStream(csvfile);
		CSVtoG4Lexer 		lexer 	= new CSVtoG4Lexer(input);
		CommonTokenStream 	tokens 	= new CommonTokenStream(lexer);
		CSVtoG4Parser 		parser 	= new CSVtoG4Parser(tokens);
		ParseTreeWalker 	walker 	= new ParseTreeWalker();		
		ParseTree tree = parser.csv();
		CSVRowListener listener = new CSVRowListener(parser);		
		walker.walk(listener, tree);		
		return listener.getDirection(name);
	}
	
	private static String readFileAsString(String filePath){
    	StringBuffer fileData = new StringBuffer(1000);
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
	    	int numRead=0;
	    	while((numRead=reader.read(buf)) != -1){
	    		String readData = String.valueOf(buf, 0, numRead);
	    		fileData.append(readData);
	    		buf = new char[1024];
	    	}
	    	reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error whilest loading the csv file");
			return null;
		}    	
    	return fileData.toString();
	}
	
}
