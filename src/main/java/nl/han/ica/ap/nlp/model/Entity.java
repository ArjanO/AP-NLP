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

public abstract class Entity {
	
	protected String name;
	
	public Entity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Check if the name of a new class/asso-class already exists as plural 
	 * @param name The new name to be compared
	 * @param cInList The classes to be compared with
	 * @return True if the plural exists. False if the plural doesn't exists
	 */
	public boolean pluralExists(String name) {
		if(name.equalsIgnoreCase(this.getName() + "s")) {
			return true;
		} else if(this.getName().equalsIgnoreCase(name + "s")) {
			this.setName(name);
			return true;
		} else if(name.equalsIgnoreCase(this.getName() + "'s")) {
			return true;
		} else if(this.getName().equalsIgnoreCase(name + "'s")) {
			this.setName(name);
			return true;
		} else if(name.equalsIgnoreCase(getClassSingular(this.getName()))){
			this.setName(name);
			return true;
		} else if(this.getName().equalsIgnoreCase(getInputSingular(name))){			
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  Get the word stem of the input noun.
	 *
	 */
	private String getInputSingular(String name) {
		int inputLength= name.length();
		
		if(name.endsWith("en")) {
			if(name.charAt(inputLength-3) == name.charAt(inputLength-4)) {
				name= name.substring(0, inputLength-3);	// Get stem of noun (- "nen" or "pen" etc).
			} else {
				name= name.substring(0, inputLength-2);	// Get stem of noun (- "en").
				inputLength= name.length()-1;
				// Check if letter before the second to last is a vowel. 
				if(name.charAt(inputLength-2) == 'a' || name.charAt(inputLength-2) == 'e' || name.charAt(inputLength-2) == 'o' || name.charAt(inputLength-2) == 'i' || name.charAt(inputLength-2) == 'u' || name.charAt(inputLength-2) == 'y') {
					return name;
				} else {
					char charToAdd= name.charAt(inputLength-1);
					name= name.substring(0, inputLength) + charToAdd + name.substring(inputLength);
				}	
			}
		}
		return name;
	}
	
	/**
	 *  Get the word stem of the existing noun.
	 *
	 */
	private String getClassSingular(String cInList) {
		int cInListLength= cInList.length();
		
		if(cInList.endsWith("en")) {
			if(cInList.charAt(cInListLength-3) == cInList.charAt(cInListLength-4)) {
				cInList= cInList.substring(0, cInListLength-3); // Get stem of noun (- "nen" or "pen" etc). 
			} else {
				cInList= cInList.substring(0, cInListLength-2); // Get stem of noun (- "en").
				cInListLength= cInList.length()-1;
				// Check if letter before the second to last is a vowel.
				if(cInList.charAt(cInListLength-2) == 'a' || cInList.charAt(cInListLength-2) == 'e' || cInList.charAt(cInListLength-2) == 'o' || cInList.charAt(cInListLength-2) == 'i' || cInList.charAt(cInListLength-2) == 'u'  || cInList.charAt(cInListLength-2) == 'y') {
					return cInList;
				} else {
					char charToAdd= cInList.charAt(cInListLength-1);
					cInList= cInList.substring(0, cInListLength) + charToAdd + cInList.substring(cInListLength);
				}
			}
		}
		return cInList;
	}
}
