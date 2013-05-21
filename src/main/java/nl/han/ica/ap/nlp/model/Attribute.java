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

import java.lang.Class;

/**
 * @author Joell
 *
 */
public class Attribute extends Entity implements Cloneable {

	private IMultiplicity multiplicity;
	private Class<?> type;
	private String name;
	
	public Attribute(String name, Class<?> type) {
		multiplicity = new AttributeMultiplicity();
		this.type = type;
		this.name = name;
	}
	
	public IMultiplicity getMultiplicity() {
		return multiplicity;
	}

	public java.lang.Class<?> getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Attribute)) {
			return super.equals(obj);
		}
		
		Attribute other = (Attribute)obj;
		
		if (this.name != other.name) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
