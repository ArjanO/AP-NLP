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
	public void testAttributeAlreadyExistsAsAssociation() {
		TreeController controller = new TreeController();
		Class passagier = new Class("Passagier");
		Class naamAsAssociation = new Class("Naam");	
		passagier.addAssociation(naamAsAssociation);
		controller.addClass(passagier);
		Attribute naamAsAttribute = new Attribute("Naam", String.class);
		controller.addAttribute(naamAsAttribute);
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(0, controller.classes.get(0).getAssociations().size());
		assertEquals(naamAsAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());	
		assertEquals(naamAsAttribute.getType(), controller.classes.get(0).getAttributes().get(0).getType());	
	}
	
	@Test
	public void testAddSimpleAttributeClassPair(){
		TreeController controller = new TreeController();
		Class passagier = new Class("Passagier");
		Attribute naamAsAttribute = new Attribute("Naam", String.class);
		passagier.addAttribute(naamAsAttribute);
		controller.addClass(passagier);
		assertEquals(1, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(passagier.getName(), controller.classes.get(0).getName());
		assertEquals(naamAsAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());
	}
	
	@Test
	public void testAddAttributeWithExistingClass(){
		TreeController controller = new TreeController();
		Class passagier = new Class("Passagier");
		Class passagier2 = new Class("Passagier");
		Attribute naamAsAttribute = new Attribute("Naam", String.class);
		passagier2.addAttribute(naamAsAttribute);
		controller.addClass(passagier);
		controller.addClass(passagier2);
		assertEquals(1, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(passagier.getName(), controller.classes.get(0).getName());
		assertEquals(naamAsAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());
	}
	
	@Test
	public void testAddExistingAttributeWithExistingClass(){
		TreeController controller = new TreeController();
		Class passagier = new Class("Passagier");
		Class passagier2 = new Class("Passagier");
		Attribute naamAsAttribute = new Attribute("Naam", String.class);
		Attribute naamAsAttribute2 = new Attribute("Naam", String.class);
		passagier.addAttribute(naamAsAttribute);
		passagier2.addAttribute(naamAsAttribute2);
		controller.addClass(passagier);
		controller.addClass(passagier2);
		assertEquals(1, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(passagier.getName(), controller.classes.get(0).getName());
		assertEquals(naamAsAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());
	}
	
	@Test
	public void testClassWithAddAttributeWithDoubleAttributeInQueue(){
		TreeController controller = new TreeController();
		Attribute idIntegerAttribute = new Attribute("id", Integer.class);
		controller.attributesToAssign.add(idIntegerAttribute);
		Attribute idStringAttribute = new Attribute("id", String.class);
		controller.attributesToAssign.add(idStringAttribute);
		Class passagier = new Class("Passagier");
		Class idAssociation = new Class("id");
		passagier.addAssociation(idAssociation);
		controller.addClass(passagier);
		assertEquals(1, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(1, controller.attributesToAssign.size());
	}
	
	@Test
	public void testDoubleClassWithAddAttributeWithDoubleAttributeInQueue(){
		TreeController controller = new TreeController();
		Attribute idIntegerAttribute = new Attribute("id", Integer.class);
		controller.attributesToAssign.add(idIntegerAttribute);
		Attribute idStringAttribute = new Attribute("id", String.class);
		controller.attributesToAssign.add(idStringAttribute);
		Class passagier = new Class("Passagier");
		Class idAssociationPassagier = new Class("id");
		passagier.addAssociation(idAssociationPassagier);
		controller.addClass(passagier);
		Class vliegtuig = new Class("Vliegtuig");
		Class idAssociationVliegtuig = new Class("id");
		vliegtuig.addAssociation(idAssociationVliegtuig);
		controller.addClass(vliegtuig);
		assertEquals(2, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(1, controller.classes.get(1).getAttributes().size());
		assertEquals(0, controller.attributesToAssign.size());		
	}
	
	//Een passagier heeft een id.
	//Een piloot heeft als id "AB".
	@Test
	public void testClassAssociationClassAttribute() {
		TreeController controller = new TreeController();
		Class passagier = new Class("Passagier");
		Class idAssociationPassagier = new Class("id");
		passagier.addAssociation(idAssociationPassagier);
		controller.addClass(passagier);
		Class piloot = new Class("Piloot");
		Attribute idStringAttribute = new Attribute("id", String.class);
		piloot.addAttribute(idStringAttribute);
		controller.addClass(piloot);
		assertEquals(2, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(idStringAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());
		assertEquals(1, controller.classes.get(1).getAttributes().size());
		assertEquals(0, controller.attributesToAssign.size());
	}
	
	//Een naam is "Michiel".
	//Een persoon heeft namen.
	@Test
	public void testAttributeAndClassWithPlural() {
		TreeController controller = new TreeController();
		Attribute naamStringAttribute = new Attribute("naam", String.class);
		controller.addAttribute(naamStringAttribute);
		Class persoon = new Class("Persoon");
		Class namenAssociationPersoon = new Class("namen");
		persoon.addAssociation(namenAssociationPersoon);
		controller.addClass(persoon);
		assertEquals(1, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(naamStringAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());
		assertEquals(0, controller.attributesToAssign.size());
	}
	
	//Een persoon heeft namen.
	//Een naam is "Michiel".
	@Test
	public void testClassWithPluralAndAttribute() {
		TreeController controller = new TreeController();
		Class persoon = new Class("Persoon");
		Class namenAssociationPersoon = new Class("namen");
		persoon.addAssociation(namenAssociationPersoon);
		controller.addClass(persoon);
		Attribute naamStringAttribute = new Attribute("naam", String.class);
		controller.addAttribute(naamStringAttribute);
		assertEquals(1, controller.classes.size());
		assertEquals(1, controller.classes.get(0).getAttributes().size());
		assertEquals(naamStringAttribute.getName(), controller.classes.get(0).getAttributes().get(0).getName());
		assertEquals(0, controller.attributesToAssign.size());
	}
}
