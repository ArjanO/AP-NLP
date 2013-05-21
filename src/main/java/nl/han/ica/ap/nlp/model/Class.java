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
package nl.han.ica.ap.nlp.model;

import java.util.ArrayList;


/**
 * @author Joell
 *
 */
public class Class extends Entity{
	private String name;
	private ArrayList<Attribute> attributes;
	private ArrayList<Association> associations;

	public Class(String name) {
		this.name = name;
		this.attributes = new ArrayList<Attribute>();
		this.associations = new ArrayList<Association>();
	}
	
	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}
	
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}
	
	public void addAssociation(Class assocation) {
		associations.add(new Association(assocation,null));
	}
	
	public void addAssociation(String name, Class association) {
		associations.add(new Association(association,name));
	}
	
	public ArrayList<Association> getAssociations() {
		return associations;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Class)) {
			return super.equals(obj);
		}
		
		Class other = (Class)obj;
		
		if (this.name != other.name) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public void removeAssociation(Class result) {
		boolean removed = false;
		int i = 0;
		while(i < associations.size() && !removed) {
			if(associations.get(i).getChildClass() == result) {
				associations.remove(i);
				removed = true;
			}
		}
	}
}
