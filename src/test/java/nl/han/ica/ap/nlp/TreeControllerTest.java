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
	
	@Test
	public void testClassBecomesAttribute() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		vliegtuig.addAttribute(passagier);
		controller.addClass(vliegtuig);
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class vliegtuig2 = new Class("vliegtuig");
		vliegtuigmaatschappij.addAttribute(vliegtuig2);
		controller.addClass(vliegtuigmaatschappij);
		assertEquals(1, controller.classes.size());		
		assertEquals(vliegtuigmaatschappij,controller.classes.get(0));		
		assertEquals(vliegtuig,controller.classes.get(0).getAttributes().get(0));
		IClass actualclass = (IClass) controller.classes.get(0).getAttributes().get(0);
		assertEquals(passagier,actualclass.getAttributes().get(0));
	}
	
	@Test
	public void testClassesShareAttribute() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier1 = new Class("passagier");
		vliegtuig.addAttribute(passagier1);
		controller.addClass(vliegtuig);
		Class bus = new Class("bus");
		Class passagier2 = new Class("passagier");
		bus.addAttribute(passagier2);
		controller.addClass(bus);
		assertEquals(2, controller.classes.size());
		assertEquals(controller.classes.get(0).getAttributes().get(0), controller.classes.get(1).getAttributes().get(0));
	}
	
	@Test
	public void testAlreadyExistsAsClass() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier = new Class("passagier");
		vliegtuig.addAttribute(passagier);
		controller.addClass(vliegtuig);
		Class vliegtuig2 = new Class("vliegtuig");
		Class piloot = new Class("piloot");
		vliegtuig2.addAttribute(piloot);
		controller.addClass(vliegtuig2);
		assertEquals(1, controller.classes.size());
		assertEquals(2, controller.classes.get(0).getAttributes().size());
		assertEquals(vliegtuig,controller.classes.get(0));
		assertEquals(passagier,controller.classes.get(0).getAttributes().get(0));
		assertEquals(piloot,controller.classes.get(0).getAttributes().get(1));
	}
	
	
}
