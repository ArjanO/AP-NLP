package nl.han.ica.ap.nlp;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.*;
import nl.han.ica.ap.nlp.model.Class;

import nl.han.ica.ap.nlp.controller.TreeController;

import org.junit.Test;
import org.omg.PortableInterceptor.SUCCESSFUL;

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
		assertSame(0, aantalpassagiers);	
		Class a1 = (Class) controller.classes.get(0).getAttributes().get(0);
		Class a2 = (Class) a1.getAttributes().get(0);
		Class actual = (Class) a2.getAttributes().get(0);
		assertSame(paspoort, actual);
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
		assertSame(1, controller.classes.size());		
		assertSame(vliegtuigmaatschappij,controller.classes.get(0));		
		assertSame(vliegtuig,controller.classes.get(0).getAttributes().get(0));
		IClass actualclass = (IClass) controller.classes.get(0).getAttributes().get(0);
		assertSame(passagier,actualclass.getAttributes().get(0));
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
		assertSame(2, controller.classes.size());
		assertSame(controller.classes.get(0).getAttributes().get(0), controller.classes.get(1).getAttributes().get(0));
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
		assertSame(1, controller.classes.size());
		assertSame(2, controller.classes.get(0).getAttributes().size());
		assertSame(vliegtuig,controller.classes.get(0));
		assertSame(passagier,controller.classes.get(0).getAttributes().get(0));
		assertSame(piloot,controller.classes.get(0).getAttributes().get(1));
	}
	
	@Test
	public void testClassAndAttributeAlreadyExist() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier = new Class("passagier");
		vliegtuig.addAttribute(passagier);
		controller.addClass(vliegtuig);
		Class paspoort = new Class("paspoort");
		Class bnnr = new Class("burgernummer");
		paspoort.addAttribute(bnnr);
		controller.addClass(paspoort);
		Class passagier2 = new Class("passagier");
		Class paspoort2 = new Class("paspoort");
		passagier2.addAttribute(paspoort2);
		controller.addClass(passagier2);
		assertSame(vliegtuig, controller.classes.get(0));
		assertSame(passagier, controller.classes.get(0).getAttributes().get(0));
		IClass approvedPassagier = (IClass) controller.classes.get(0).getAttributes().get(0);
		ArrayList<IClass> classes = controller.classes;
		assertSame(paspoort,approvedPassagier.getAttributes().get(0));
	}
	
	@Test
	public void testClassAttributeLoop() {
		TreeController controller = new TreeController();
		Class ouder1 = new Class("ouder");
		Class kind1 = new Class("kind");
		ouder1.addAttribute(kind1);
		controller.addClass(ouder1);
		Class kind2 = new Class("kind");
		Class ouder2 = new Class("ouder");
		kind2.addAttribute(ouder2);
		controller.addClass(kind2);
		Class kind3 = new Class("kind");
		Class voorouder1 = new Class("voorouder");
		kind3.addAttribute(voorouder1);
		controller.addClass(kind3);
		
	}
	
	
}
