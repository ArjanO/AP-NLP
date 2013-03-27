package nl.han.ica.ap.nlp.export;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Class;

public interface IExport {
	
	/**
	 * Exports NLP model to other format.
	 * @param classes List of classes.
	 * @return path of exported file or empty string if error.
	 */
	String export(ArrayList<Class> classes);
}
