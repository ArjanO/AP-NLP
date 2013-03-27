package nl.han.ica.ap.nlp.model;

import java.util.ArrayList;

public interface IClass extends IAttribute{
	public String getName();
	public ArrayList<IAttribute> getAttributes();
}