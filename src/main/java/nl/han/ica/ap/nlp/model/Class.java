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

import java.util.TreeMap;

/**
 * @author Joell
 *
 */
public class Class implements IClass, IAttribute, Comparable<Class>{
	private String name;
	private TreeMap<IAttribute,Multiplicity[]> attributes = new TreeMap<IAttribute,Multiplicity[]>();

	public Class(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addAttribute(IAttribute a,Multiplicity[] multiplicities) {
		attributes.put(a,multiplicities);
	}
	
	public TreeMap<IAttribute,Multiplicity[]> getAttributes(){
		return attributes;
	}
	
	public void setAttributes(TreeMap<IAttribute, Multiplicity[]> attributes){
		this.attributes = attributes;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public int compareTo(Class o) {
		if(this.equals(o)) {
			return 0;
		} else {
			return -1;
		}
	}
}
