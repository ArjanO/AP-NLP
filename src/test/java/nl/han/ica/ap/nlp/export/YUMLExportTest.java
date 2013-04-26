package nl.han.ica.ap.nlp.export;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuig]1..0-*..0[Passagier]");
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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuig]1..0-*..0[Passagier]\n[Bus]1..0-*..0[Passagier]");
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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuigmaatschappij]1..0-*..0[Vliegtuig]\n[Vliegtuig]1..0-*..0[Passagier]");
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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuig]1..0-*..0[Passagier]\n[Passagier]1..0-*..0[Passpoort]");
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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuig]1..0-*..0[Passagier]\n[Vliegtuig]1..0-*..0[Piloot]");
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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuigmaatschappij]1..0-*..0[Vliegtuig]\n[Vliegtuig]1..0-*..0[Passagier]\n[Passagier]1..0-*..0[Paspoort]");
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
		
		String yumlresult = exporter.export(classes);
		assertEquals(yumlresult, "[Vliegtuig]1..0-*..0[Passagier]\n[Passagier]1..0-*..0[Paspoort]\n[Paspoort]1..0-*..0[Vliegtuig]");
	}
}