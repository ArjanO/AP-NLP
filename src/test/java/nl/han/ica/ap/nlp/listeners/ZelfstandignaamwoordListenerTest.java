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

import java.util.Date;

import nl.han.ica.ap.nlp.App;
import nl.han.ica.ap.nlp.controller.TreeController;
import nl.han.ica.ap.nlp.model.Association;
import nl.han.ica.ap.nlp.model.Attribute;
import nl.han.ica.ap.nlp.model.Class;

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
	
	@Test
	public void testCompositeSentence(){
		App app= App.getInstance();
		app.start("Een vliegtuig heeft een passagier en een piloot.");
		assertEquals(1, app.getController().classes.size());
		assertEquals(2, app.getController().classes.get(0).getAssociations().size());
	}
	
	@Test
	public void testEnumerationEnSentence(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft passagiers en een piloot en pinda's en stoelen.");
		assertEquals("vliegtuig", app.getController().classes.get(0).getName());
		assertEquals(4, app.getController().classes.get(0).getAssociations().size());
	}
	
	@Test
	public void testEnumerationKommaSentence(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft passagiers, stoelen, een piloot, beeldschermen, een toilet.");
		assertEquals(1, app.getController().classes.size());
		assertEquals(5, app.getController().classes.get(0).getAssociations().size());
	}
	
	@Test
	public void testEnumerationEnKommaSentence(){
		App app= App.getInstance();
		app.start("Een vliegtuig heeft passagiers, stoelen, een piloot en een toilet.");
		assertEquals(1, app.getController().classes.size());
		assertEquals(4, app.getController().classes.get(0).getAssociations().size());
	}
	
	@Test
	public void testCompositeEnumerationMixSentence(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft passagiers, piloten en stewardesses en baggage.");
		assertEquals(1, app.getController().classes.size());
		assertEquals(4, app.getController().classes.get(0).getAssociations().size());
	}
	
	@Test
	public void testCompositeSentenceExplicitMultiplicity(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft minimaal 3 piloten, 5 stewardesses en 50 passagiers en 2 vleugels.");
		assertEquals("3", app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("*", app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("5", app.getController().classes.get(0).getAssociations().get(1).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("*", app.getController().classes.get(0).getAssociations().get(1).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("50", app.getController().classes.get(0).getAssociations().get(2).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("*", app.getController().classes.get(0).getAssociations().get(2).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("2", app.getController().classes.get(0).getAssociations().get(3).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("2", app.getController().classes.get(0).getAssociations().get(3).getChildMultiplicity().getUpperBound().getValue());	
	}
	
	@Test
	public void testCompositeSentenceMixedMultiplicities(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft 3 piloten, 5 stewardesses en passagiers en 2 vleugels.");
		assertEquals("3", app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("3", app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("5", app.getController().classes.get(0).getAssociations().get(1).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("5", app.getController().classes.get(0).getAssociations().get(1).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("0", app.getController().classes.get(0).getAssociations().get(2).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("*", app.getController().classes.get(0).getAssociations().get(2).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("2", app.getController().classes.get(0).getAssociations().get(3).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("2", app.getController().classes.get(0).getAssociations().get(3).getChildMultiplicity().getUpperBound().getValue());	
	}
	
	@Test
	public void testCompositeSentenceExplicitSeperateMultiplicities(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft minimaal 3 piloten, minimaal 5 stewardesses en maximaal 500 passagiers en 2 vleugels.");
		assertEquals("3", app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("*", app.getController().classes.get(0).getAssociations().get(0).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("5", app.getController().classes.get(0).getAssociations().get(1).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("*", app.getController().classes.get(0).getAssociations().get(1).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("0", app.getController().classes.get(0).getAssociations().get(2).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("500", app.getController().classes.get(0).getAssociations().get(2).getChildMultiplicity().getUpperBound().getValue());
		assertEquals("2", app.getController().classes.get(0).getAssociations().get(3).getChildMultiplicity().getLowerBound().getValue());
		assertEquals("2", app.getController().classes.get(0).getAssociations().get(3).getChildMultiplicity().getUpperBound().getValue());	
	}
	
	@Test
	public void testParentMultiplicityAbsolute(){
		App app = App.getInstance();
		app.start("1 vliegtuig heeft maximaal 2 piloten.");
		Association a = app.getController().classes.get(0).getAssociations().get(0);
		assertEquals("1",a.getParentMultiplicity().getLowerBound().getValue());
		assertEquals("1",a.getParentMultiplicity().getUpperBound().getValue());
	}
	
	@Test
	public void testParentMultiplicityLowerBound(){
		App app = App.getInstance();
		app.start("Minimaal 1 vliegveld heeft een vliegtuig.");
		Association a = app.getController().classes.get(0).getAssociations().get(0);
		assertEquals("1",a.getParentMultiplicity().getLowerBound().getValue());
		assertEquals("1",a.getParentMultiplicity().getUpperBound().getValue());
	}
	
	@Test
	public void testAssociationBecomesAttribute(){
		App app = App.getInstance();
		app.start("Een vliegtuig heeft een type. Een type is \"boeing\" .");
		assertEquals(0,app.getController().classes.get(0).getAssociations().size());
		assertEquals(1,app.getController().classes.get(0).getAttributes().size());
	}
	
	@Test
	public void testStringAsAttribute() {
		App app = App.getInstance();
		app.start("Een vliegtuig heeft een type. Een type is \"boeing\" .");    
		assertEquals(String.class,app.getController().classes.get(0).getAttributes().get(0).getType());
	}
	
	@Test
	public void testDateAsAttribute() {
	 	App app = App.getInstance();
	 	app.start("Een begindatum heeft een type. Een type is 21/05/2013.");    
	 	assertEquals(Date.class,app.getController().classes.get(0).getAttributes().get(0).getType());
	}
	
	@Test
	public void testTimeAsAttribute() {
		App app = App.getInstance();
		app.start("Een begindatum heeft een type. Een type is 15:01.");    
		assertEquals(Date.class,app.getController().classes.get(0).getAttributes().get(0).getType());
	}
	
	@Test
	public void testDoubleAsAttribute() {
		App app = App.getInstance();
		app.start("Een vliegtuig heeft een id. Een id is 2,5.");
		assertEquals(Double.class,app.getController().classes.get(0).getAttributes().get(0).getType());
	}
	
	@Test
	public void testAttributeWithAdverbSentence() {
		App app = App.getInstance();
		app.start("Een passagier heeft als geboortedatum 17/09/1991.");
		assertEquals(1, app.getController().classes.get(0).getAttributes().size());
		assertEquals(0, app.getController().classes.get(0).getAssociations().size());
		assertEquals(Date.class,app.getController().classes.get(0).getAttributes().get(0).getType());
	}
	
	@Test
	public void testMultipleVerbsInSenctece() {
		App app = App.getInstance();
		app.start("Een vliegtuig heeft een passagier en een passagier heeft een paspoort en een vliegtuig heeft een piloot.");
		assertEquals(1, app.getController().classes.size());
		assertEquals(2, app.getController().classes.get(0).getAssociations().size());
	}
}
