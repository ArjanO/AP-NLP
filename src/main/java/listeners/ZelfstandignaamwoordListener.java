package listeners;

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.model.Class;
import nl.ica.ap.nlp.NlpBaseListener;
import nl.ica.ap.nlp.NlpParser;
import nl.ica.ap.nlp.NlpParser.ZelfstandignaamwoordContext;

public class ZelfstandignaamwoordListener extends NlpBaseListener {
	NlpParser parser;
	public ZelfstandignaamwoordListener(NlpParser parser) {
		this.parser = parser;
	}
	
	@Override
	public void enterZelfstandignaamwoord(ZelfstandignaamwoordContext ctx) {
		Class c = new Class(ctx.getText());
		App.getInstance().classes.add(c);
	}
}
