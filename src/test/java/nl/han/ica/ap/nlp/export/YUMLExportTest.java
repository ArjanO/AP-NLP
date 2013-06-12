package nl.han.ica.ap.nlp.export;

import static org.junit.Assert.*;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.model.Attribute;
import nl.han.ica.ap.nlp.model.Class;

import org.junit.Test;

public class YUMLExportTest {
	@Test
	public void testExport() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");

		vliegtuig.addAssociation(passagier);		
		classes.add(vliegtuig);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuig]0..1->0..*[Passagier]");
	}
	
	@Test
	public void testScenario1() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class bus = new Class("Bus");
		
		vliegtuig.addAssociation(passagier);
		bus.addAssociation(passagier);
		
		classes.add(vliegtuig);
		classes.add(bus);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuig]0..1->0..*[Passagier], [Bus]0..1->0..*[Passagier]");
	}
	
	@Test
	public void testScenario2() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class passagier = new Class("Passagier");
		
		vliegtuig.addAssociation(passagier);
		vliegtuigmaatschappij.addAssociation(vliegtuig);
		
		classes.add(vliegtuigmaatschappij);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuigmaatschappij]0..1->0..*[Vliegtuig], [Vliegtuig]0..1->0..*[Passagier]");
	}
	
	@Test
	public void testScenario3() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class passpoort = new Class("Passpoort");
		
		vliegtuig.addAssociation(passagier);
		passagier.addAssociation(passpoort);
		
		classes.add(vliegtuig);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuig]0..1->0..*[Passagier], [Passagier]0..1->0..*[Passpoort]");
	}
	
	@Test
	public void testScenario4() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class piloot = new Class("Piloot");
		
		vliegtuig.addAssociation(passagier);
		vliegtuig.addAssociation(piloot);
		
		classes.add(vliegtuig);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuig]0..1->0..*[Passagier], [Vliegtuig]0..1->0..*[Piloot]");
	}
	
	@Test
	public void testScenario5() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class passagier = new Class("Passagier");
		Class paspoort = new Class("Paspoort");

		passagier.addAssociation(paspoort);
		vliegtuigmaatschappij.addAssociation(vliegtuig);
		vliegtuig.addAssociation(passagier);
		
		classes.add(vliegtuigmaatschappij);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuigmaatschappij]0..1->0..*[Vliegtuig], [Vliegtuig]0..1->0..*[Passagier], [Passagier]0..1->0..*[Paspoort]");
	}
	
	@Test
	public void testScenario6() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class paspoort = new Class("Paspoort");
		
		vliegtuig.addAssociation(passagier);
		passagier.addAssociation(paspoort);
		paspoort.addAssociation(vliegtuig);
		
		classes.add(vliegtuig);
		
		String yumlresult = exporter.exportSource(classes);
		assertEquals(yumlresult, "[Vliegtuig]0..1->0..*[Passagier], [Passagier]0..1->0..*[Paspoort], [Paspoort]0..1->0..*[Vliegtuig]");
	}
	
	@Test
	public void testScenario7() {
		YUMLExport exporter = new YUMLExport();
		
		ArrayList<Class> classes1 = new ArrayList<Class>();
		Class passagier1 = new Class("Passagier");
		Class vliegtuig1 = new Class("Vliegtuig");
		vliegtuig1.addAssociation(passagier1);
		classes1.add(vliegtuig1);
		
		ArrayList<Class> classes2 = new ArrayList<Class>();
		Class passagier2 = new Class("Passagier");
		Class vliegtuig2 = new Class("Vliegtuig");
		vliegtuig2.addAssociation(passagier2);
		classes2.add(vliegtuig2);
		
		String yumlresult1 = exporter.exportSource(classes1);
		String yumlresult2 = exporter.exportSource(classes2);

		assertEquals(yumlresult1, "[Vliegtuig]0..1->0..*[Passagier]");
		assertEquals(yumlresult2, "[Vliegtuig]0..1->0..*[Passagier]");
	}
	
	@Test
	public void testModelWithAttributes() {
		YUMLExport export = new YUMLExport();
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Attribute id = new Attribute("ID", int.class);
		Attribute naam = new Attribute("Naam", String.class);
		Attribute werknemer = new Attribute("isWerknemer", boolean.class);
		vliegtuig.addAttribute(id);
		passagier.addAttribute(naam);
		passagier.addAttribute(werknemer);
		vliegtuig.addAssociation(passagier);
		classes.add(vliegtuig);
		assertEquals("[Vliegtuig|ID int;]0..1->0..*[Passagier|Naam String;isWerknemer boolean;]", export.exportSource(classes));
	}
}
