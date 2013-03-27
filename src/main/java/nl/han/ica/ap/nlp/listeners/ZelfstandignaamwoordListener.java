package nl.han.ica.ap.nlp.listeners;

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.model.Class;
import nl.ica.ap.nlp.NlpBaseListener;
import nl.ica.ap.nlp.NlpParser;
import nl.ica.ap.nlp.NlpParser.ZelfstandignaamwoordContext;

public class ZelfstandignaamwoordListener extends NlpBaseListener {
	NlpParser parser;
	App app;
	public ZelfstandignaamwoordListener(NlpParser parser, App app) {
		this.parser = parser;
		this.app = app;
	}
	
	@Override
	public void enterZelfstandignaamwoord(ZelfstandignaamwoordContext ctx) {
		Class c = new Class(ctx.getText());
		app.classes.add(c);
	}
}
