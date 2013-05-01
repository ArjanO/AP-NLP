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
package nl.han.ica.ap.nlp.listeners;

import static org.junit.Assert.*;

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.model.Association;

import org.junit.Test;

/**
 * @author Joell
 *
 */
public class ZelfstandignaamwoordListenerTest {
	
	@Test
	public void testDefaultMultiplicities(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft een passagier.");
		Association a = app.getController().classes.get(0).getAssociations().get(0);
		String parentLowerBound = a.getParentMultiplicity().getLowerBound().getValue();
		String parentUpperBound = a.getParentMultiplicity().getUpperBound().getValue();
		String childLowerBound = a.getChildMultiplicity().getLowerBound().getValue();
		String childUpperBound = a.getChildMultiplicity().getUpperBound().getValue();
		assertEquals("0",parentLowerBound);
		assertEquals("1",parentUpperBound);
		assertEquals("0",childLowerBound);
		assertEquals("*",childUpperBound);
	}

	@Test
	public void testChildMultiplicityUpperBound(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft maximaal 100 passagiers.");
		String actualUpperBound = app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getUpperBound().getValue();
		assertEquals("100", actualUpperBound);
	}
	
	@Test
	public void testChildMultiplicityLowerBound(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft minimaal 2 piloten.");
		String actualLowerBound = app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getLowerBound().getValue();
		assertEquals("2", actualLowerBound);
	}
	
	@Test
	public void testAbsoluteChildMultiplicity(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft 2 piloten.");
		String actualLowerBound = app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getLowerBound().getValue();
		String actualUpperBound = app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getUpperBound().getValue();
		assertEquals("2", actualLowerBound);
		assertEquals("2", actualUpperBound);
	}

}
