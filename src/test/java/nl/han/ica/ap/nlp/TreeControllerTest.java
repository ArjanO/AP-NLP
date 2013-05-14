package nl.han.ica.ap.nlp;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map.Entry;

import nl.han.ica.ap.nlp.model.*;
import nl.han.ica.ap.nlp.model.Class;

import nl.han.ica.ap.nlp.controller.TreeController;

import org.junit.BeforeClass;
import org.junit.Test;

public class TreeControllerTest {
	
	@Test
	public void testClassAlreadyExistsAsAssociation() {
		TreeController controller = new TreeController();	
		Class vliegtuig = new Class("Vliegtuig");		
		Class passagier = new Class("Passagier");
		vliegtuig.addAssociation(passagier);
		controller.addClass(vliegtuig);	
		Class passagier2 = new Class("Passagier");
		Class paspoort = new Class("Paspoort");
		passagier2.addAssociation(paspoort);
		controller.addClass(passagier2);				
		int aantalpassagiers = 0;
		for(Class _class : controller.classes) {
			if(_class.getName().equals("Passagier")) {
				aantalpassagiers++;
			}
		}
		assertSame(0, aantalpassagiers);	
		assertSame(vliegtuig, controller.classes.get(0));
		assertSame(passagier, controller.classes.get(0).getAssociations().get(0).getChildClass());
		assertSame(paspoort,controller.classes.get(0).getAssociations().get(0).getChildClass().getAssociations().get(0).getChildClass());
	}
	
	@Test
	public void testClassBecomesAssociation() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		vliegtuig.addAssociation(passagier);
		controller.addClass(vliegtuig);
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class vliegtuig2 = new Class("vliegtuig");
		vliegtuigmaatschappij.addAssociation(vliegtuig2);
		controller.addClass(vliegtuigmaatschappij);
		assertSame(1, controller.classes.size());	
		assertSame(vliegtuigmaatschappij,controller.classes.get(0));	
		assertSame(vliegtuig,controller.classes.get(0).getAssociations().get(0).getChildClass());
		assertSame(passagier,controller.classes.get(0).getAssociations().get(0).getChildClass().getAssociations().get(0).getChildClass());
	}
	
	@Test
	public void testClassesShareAssociation() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier1 = new Class("passagier");
		vliegtuig.addAssociation(passagier1);
		controller.addClass(vliegtuig);
		Class bus = new Class("bus");
		Class passagier2 = new Class("passagier");
		bus.addAssociation(passagier2);
		controller.addClass(bus);
		assertSame(2, controller.classes.size());
		assertSame(controller.classes.get(0).getAssociations().get(0).getChildClass(),controller.classes.get(1).getAssociations().get(0).getChildClass());
		assertFalse(controller.classes.contains(passagier1));
	}
	
	@Test
	public void testAlreadyExistsAsClass() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier = new Class("passagier");
		vliegtuig.addAssociation(passagier);
		controller.addClass(vliegtuig);
		Class vliegtuig2 = new Class("vliegtuig");
		Class piloot = new Class("piloot");
		vliegtuig2.addAssociation(piloot);
		controller.addClass(vliegtuig2);		
		assertSame(1, controller.classes.size());
		assertSame(2, controller.classes.get(0).getAssociations().size());
		assertSame(vliegtuig,controller.classes.get(0));
		assertSame(passagier,controller.classes.get(0).getAssociations().get(0).getChildClass());
		assertSame(piloot,controller.classes.get(0).getAssociations().get(1).getChildClass());
	}
	
	@Test
	public void testClassAndAssociationAlreadyExist() {
		TreeController controller = new TreeController();
		Class vliegtuig = new Class("vliegtuig");
		Class passagier = new Class("passagier");
		vliegtuig.addAssociation(passagier);
		controller.addClass(vliegtuig);
		Class paspoort = new Class("paspoort");
		Class bnnr = new Class("burgernummer");
		paspoort.addAssociation(bnnr);
		controller.addClass(paspoort);
		Class passagier2 = new Class("passagier");
		Class paspoort2 = new Class("paspoort");
		passagier2.addAssociation(paspoort2);
		controller.addClass(passagier2);
		assertSame(vliegtuig, controller.classes.get(0));
		assertSame(passagier,controller.classes.get(0).getAssociations().get(0).getChildClass());
		assertSame(paspoort,controller.classes.get(0).getAssociations().get(0).getChildClass().getAssociations().get(0).getChildClass());
		assertSame(bnnr,controller.classes.get(0).getAssociations().get(0).getChildClass().getAssociations().get(0).getChildClass().getAssociations().get(0).getChildClass());
		assertEquals(1, controller.classes.size());
	}
	
	@Test
	public void testClassAssociationLoop() {
		TreeController controller = new TreeController();
		Class ouder1 = new Class("ouder");
		Class kind1 = new Class("kind");
		ouder1.addAssociation(kind1);
		controller.addClass(ouder1);
		Class kind2 = new Class("kind");
		Class ouder2 = new Class("ouder");
		kind2.addAssociation(ouder2);
		controller.addClass(kind2);
		Class kind3 = new Class("kind");
		Class voorouder1 = new Class("voorouder");
		kind3.addAssociation(voorouder1);
		controller.addClass(kind3);	
		assertEquals(1, controller.classes.size());
		assertSame(ouder1, controller.classes.get(0));
		assertSame(kind1,controller.classes.get(0).getAssociations().get(0).getChildClass());
		assertSame(ouder1,controller.classes.get(0).getAssociations().get(0).getChildClass().getAssociations().get(0).getChildClass());
		assertSame(voorouder1,controller.classes.get(0).getAssociations().get(0).getChildClass().getAssociations().get(1).getChildClass());
	}
	
	@Test	
	public void testAttributeAlreadyExists() {
		TreeController controller = new TreeController();
		Class passagier = new Class("Passagier");
		Class naamAsAssociation = new Class("Naam");	
		passagier.addAssociation(naamAsAssociation);
		controller.addClass(passagier);
		Attribute naamAsAttribute = new Attribute("Naam", String.class);
		controller.addAttribute(naamAsAttribute);
		assertEquals(1,controller.classes.get(0).getAttributes().size());
		assertEquals(0, controller.classes.get(0).getAssociations().size());
		assertEquals(naamAsAttribute, controller.classes.get(0).getAttributes().get(0));		
	}
}
