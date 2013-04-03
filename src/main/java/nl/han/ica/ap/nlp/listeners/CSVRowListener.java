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
	CSVtoG4 csvtog4;

	ArrayList<String> conjugations = new ArrayList<String>();
	
	public CSVRowListener(CSVtoG4Parser parser, CSVtoG4 csvtog4) {
		this.parser = parser;
		this.csvtog4 = csvtog4;
	}

	@Override
	public void exitCsv(CsvContext ctx) {
		csvtog4.conjugations = conjugations;
	}
	
	@Override
	public void enterConjugation(ConjugationContext ctx) {
		conjugations.add(ctx.getText());
	}
}
