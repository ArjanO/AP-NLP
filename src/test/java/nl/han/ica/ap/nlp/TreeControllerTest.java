package nl.han.ica.ap.nlp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import nl.han.ica.ap.nlp.model.*;
import nl.han.ica.ap.nlp.model.Class;

import nl.han.ica.ap.nlp.controller.TreeController;

import org.junit.BeforeClass;
import org.junit.Test;

public class TreeControllerTest {
	static Multiplicity[] m = new Multiplicity[2];
	
	@BeforeClass
	public static void testSetup() {
		m[0] = new Multiplicity();
		m[1] = new Multiplicity();
	}

	@Test
	public void testClassAlreadyExistsAsAttribute() {
		TreeController controller = new TreeController();	
		Class vliegtuig = new Class("Vliegtuig");		
		Class passagier = new Class("Passagier");
		vliegtuig.addAttribute(passagier,m);
		controller.addClass(vliegtuig);	
		Class passagier2 = new Class("Passagier");
		Class paspoort = new Class("Paspoort");
		passagier2.addAttribute(paspoort,m);
		controller.addClass(passagier2);				
		int aantalpassagiers=0;
		for(IClass classe : controller.classes) {
			if(classe.getName().equals("Passagier")) {
				aantalpassagiers++;
			}
		}
		assertSame(0, aantalpassagiers);	
		ArrayList<IClass> attributes = controller.classes;		
		assertSame(vliegtuig, controller.classes.get(0));
		Class a1 = (Class)((Entry<IAttribute, Multiplicity[]>) controller.classes.get(0).getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(passagier, a1);
		Class a2 = (Class)((Entry<IAttribute, Multiplicity[]>) a1.getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(paspoort,a2);
	}
	
	@Test
	public void testClassBecomesAttribute() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		vliegtuig.addAttribute(passagier,m);
		controller.addClass(vliegtuig);
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class vliegtuig2 = new Class("vliegtuig");
		vliegtuigmaatschappij.addAttribute(vliegtuig2,m);
		controller.addClass(vliegtuigmaatschappij);
		assertSame(1, controller.classes.size());	
		assertSame(vliegtuigmaatschappij,controller.classes.get(0));	
		Class a1 = (Class)((Entry<IAttribute, Multiplicity[]>) controller.classes.get(0).getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(vliegtuig,a1);
		Class a2 = (Class)((Entry<IAttribute, Multiplicity[]>) a1.getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(passagier,a2);
	}
	
	
	@Test
	public void testClassesShareAttribute() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier1 = new Class("passagier");
		vliegtuig.addAttribute(passagier1,m);
		controller.addClass(vliegtuig);
		Class bus = new Class("bus");
		Class passagier2 = new Class("passagier");
		bus.addAttribute(passagier2,m);
		controller.addClass(bus);
		assertSame(2, controller.classes.size());
		Class a1 = (Class) ((Entry<IAttribute,Multiplicity[]>)controller.classes.get(0).getAttributes().entrySet().toArray()[0]).getKey();
		Class a2 = (Class) ((Entry<IAttribute,Multiplicity[]>)controller.classes.get(1).getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(a1,a2);
		assertFalse(controller.classes.contains(passagier1));
	}
	
	@Test
	public void testAlreadyExistsAsClass() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier = new Class("passagier");
		vliegtuig.addAttribute(passagier,m);
		controller.addClass(vliegtuig);
		Class vliegtuig2 = new Class("vliegtuig");
		Class piloot = new Class("piloot");
		vliegtuig2.addAttribute(piloot,m);
		controller.addClass(vliegtuig2);		
		assertSame(1, controller.classes.size());
		assertSame(2, controller.classes.get(0).getAttributes().keySet().size());
		assertSame(vliegtuig,controller.classes.get(0));
		Class a1 = (Class) ((Entry<IAttribute,Multiplicity[]>)controller.classes.get(0).getAttributes().entrySet().toArray()[1]).getKey();
		assertSame(passagier,a1);
		Class a2 = (Class) ((Entry<IAttribute,Multiplicity[]>)controller.classes.get(0).getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(piloot,a2);
	}
	
	@Test
	public void testClassAndAttributeAlreadyExist() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier = new Class("passagier");
		vliegtuig.addAttribute(passagier,m);
		controller.addClass(vliegtuig);
		Class paspoort = new Class("paspoort");
		Class bnnr = new Class("burgernummer");
		paspoort.addAttribute(bnnr,m);
		controller.addClass(paspoort);
		Class passagier2 = new Class("passagier");
		Class paspoort2 = new Class("paspoort");
		passagier2.addAttribute(paspoort2,m);
		controller.addClass(passagier2);
		assertSame(vliegtuig, controller.classes.get(0));
		Class a1 = (Class) ((Entry<IAttribute,Multiplicity[]>)controller.classes.get(0).getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(passagier,a1);
		Class a2 = (Class) ((Entry<IAttribute,Multiplicity[]>)a1.getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(paspoort,a2);
		Class a3 = (Class) ((Entry<IAttribute,Multiplicity[]>)a2.getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(bnnr,a3);
		assertEquals(1, controller.classes.size());
	}
	
	@Test
	public void testClassAttributeLoop() {
		TreeController controller = new TreeController();
		Class ouder1 = new Class("ouder");
		Class kind1 = new Class("kind");
		ouder1.addAttribute(kind1,m);
		controller.addClass(ouder1);
		Class kind2 = new Class("kind");
		Class ouder2 = new Class("ouder");
		kind2.addAttribute(ouder2,m);
		controller.addClass(kind2);
		Class kind3 = new Class("kind");
		Class voorouder1 = new Class("voorouder");
		kind3.addAttribute(voorouder1,m);
		controller.addClass(kind3);	
		assertEquals(1, controller.classes.size());
		assertSame(ouder1, controller.classes.get(0));
		Class a1 = (Class) ((Entry<IAttribute,Multiplicity[]>)controller.classes.get(0).getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(kind1,a1);
		Class a2 = (Class) ((Entry<IAttribute,Multiplicity[]>)a1.getAttributes().entrySet().toArray()[0]).getKey();
		assertSame(voorouder1,a2);
		Class a3 = (Class) ((Entry<IAttribute,Multiplicity[]>)a1.getAttributes().entrySet().toArray()[1]).getKey();
		assertSame(ouder1,a3);
	}	
}
