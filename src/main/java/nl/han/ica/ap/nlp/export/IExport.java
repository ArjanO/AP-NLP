package nl.han.ica.ap.nlp.export;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Class;

public interface IExport {
	String export(ArrayList<Class> classes);
}
