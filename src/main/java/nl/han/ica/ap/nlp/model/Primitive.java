package nl.han.ica.ap.nlp.model;

public class Primitive {
	private String name;
	private String value;
	
	public Primitive(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public String getName(){
		return name;
	}
	
	public String getValue(){
		return value;
	}
}
