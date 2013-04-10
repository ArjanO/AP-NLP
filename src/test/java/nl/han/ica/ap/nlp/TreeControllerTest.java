package nl.han.ica.ap.nlp;

import static org.junit.Assert.*;
import nl.han.ica.ap.nlp.model.*;
import nl.han.ica.ap.nlp.model.Class;

import nl.han.ica.ap.nlp.controller.TreeController;

import org.junit.Test;

public class TreeControllerTest {

	@Test
	public void testClassAlreadyExistsAsAttribute() {
		TreeController controller = new TreeController();	
		Class vliegtuig = new Class("Vliegtuig");
		Class cabine = new Class("cabine");
		Class passagier = new Class("Passagier");
		cabine.addAttribute(passagier);
		vliegtuig.addAttribute(cabine);
		controller.addClass(vliegtuig);	
		Class passagier2 = new Class("Passagier");
		Class paspoort = new Class("Paspoort");
		passagier2.addAttribute(paspoort);
		controller.addClass(passagier2);				
		int aantalpassagiers=0;
		for(IClass classe : controller.classes) {
			if(classe.getName().equals("Passagier")) {
				aantalpassagiers++;
			}
		}
		assertEquals(0, aantalpassagiers);	
		Class a1 = (Class) controller.classes.get(0).getAttributes().get(0);
		Class a2 = (Class) a1.getAttributes().get(0);
		Class actual = (Class) a2.getAttributes().get(0);
		assertEquals(paspoort, actual);
	}
}
