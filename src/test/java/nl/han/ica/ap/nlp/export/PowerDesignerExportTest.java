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
package nl.han.ica.ap.nlp.export;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.util.IFile;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.capture;
import static org.junit.Assert.*;

import org.easymock.Capture;
import org.junit.Test;

public class PowerDesignerExportTest {
	private Capture<String> content = new Capture<String>(); 
	
	public IFile buildFileMockTest() {
		IFile testFile = createMock(IFile.class);
		
		expect(testFile.getContent()).andReturn("").anyTimes();
		testFile.setContent(capture(content));
		expect(testFile.write()).andReturn(true).anyTimes();
		
		replay(testFile);
		
		return testFile;
	}
	
	@Test
	public void testExport() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		
		vliegtuig.addAssociation(passagier);
		
		classes.add(vliegtuig);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
	
	@Test
	public void testScenario1() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class bus = new Class("Bus");
		
		vliegtuig.addAssociation(passagier);
		bus.addAssociation(passagier);
		
		classes.add(vliegtuig);
		classes.add(bus);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/><packagedElement name=\"Bus\" xmi:id=\"BUS\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_1OWNEDEND_1 ASSOCIATION_1OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_1OWNEDEND_2\" xmi:id=\"ASSOCIATION_1\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_1\" type=\"BUS\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_1UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_1\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_1UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
	
	@Test
	public void testScenario2() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class passagier = new Class("Passagier");
		
		vliegtuig.addAssociation(passagier);
		vliegtuigmaatschappij.addAssociation(vliegtuig);
		
		classes.add(vliegtuigmaatschappij);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuigmaatschappij\" xmi:id=\"VLIEGTUIGMAATSCHAPPIJ\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIGMAATSCHAPPIJ\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_1OWNEDEND_1 ASSOCIATION_1OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_1OWNEDEND_2\" xmi:id=\"ASSOCIATION_1\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_1\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_1UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_1\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_1UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
	
	@Test
	public void testScenario3() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class passpoort = new Class("Passpoort");
		
		vliegtuig.addAssociation(passagier);
		passagier.addAssociation(passpoort);
		
		classes.add(vliegtuig);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_1OWNEDEND_1 ASSOCIATION_1OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_1OWNEDEND_2\" xmi:id=\"ASSOCIATION_1\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_1\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_1UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_1\" type=\"PASSPOORT\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_1UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passpoort\" xmi:id=\"PASSPOORT\" xmi:type=\"uml:Class\"/></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
	
	@Test
	public void testScenario4() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class piloot = new Class("Piloot");
		
		vliegtuig.addAssociation(passagier);
		vliegtuig.addAssociation(piloot);
		
		classes.add(vliegtuig);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement memberEnd=\"ASSOCIATION_1OWNEDEND_1 ASSOCIATION_1OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_1OWNEDEND_2\" xmi:id=\"ASSOCIATION_1\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_1\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_1UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_1\" type=\"PILOOT\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_1UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/><packagedElement name=\"Piloot\" xmi:id=\"PILOOT\" xmi:type=\"uml:Class\"/></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
	
	@Test
	public void testScenario5() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class vliegtuigmaatschappij = new Class("Vliegtuigmaatschappij");
		Class passagier = new Class("Passagier");
		Class paspoort = new Class("Paspoort");
		
		passagier.addAssociation(paspoort);
		vliegtuigmaatschappij.addAssociation(vliegtuig);
		vliegtuig.addAssociation(passagier);
		
		classes.add(vliegtuigmaatschappij);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuigmaatschappij\" xmi:id=\"VLIEGTUIGMAATSCHAPPIJ\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIGMAATSCHAPPIJ\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_1OWNEDEND_1 ASSOCIATION_1OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_1OWNEDEND_2\" xmi:id=\"ASSOCIATION_1\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_1\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_1UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_1\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_1UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_2OWNEDEND_1 ASSOCIATION_2OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_2OWNEDEND_2\" xmi:id=\"ASSOCIATION_2\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_2\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_2OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_2UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_2LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_2\" type=\"PASPOORT\" visibility=\"public\" xmi:id=\"ASSOCIATION_2OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_2UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_2LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Paspoort\" xmi:id=\"PASPOORT\" xmi:type=\"uml:Class\"/></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
	
	@Test
	public void testScenario6() {
		IFile fileMock = buildFileMockTest();
		PowerDesignerExport exporter = new PowerDesignerExport();
		exporter.setFile(fileMock);
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class paspoort = new Class("Paspoort");
		
		vliegtuig.addAssociation(passagier);
		passagier.addAssociation(paspoort);
		paspoort.addAssociation(vliegtuig);
		
		classes.add(vliegtuig);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = content.getValue();
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_0OWNEDEND_1 ASSOCIATION_0OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_0OWNEDEND_2\" xmi:id=\"ASSOCIATION_0\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_0\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_0UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_0\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_0OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_0UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_0LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_1OWNEDEND_1 ASSOCIATION_1OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_1OWNEDEND_2\" xmi:id=\"ASSOCIATION_1\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_1\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_1UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_1\" type=\"PASPOORT\" visibility=\"public\" xmi:id=\"ASSOCIATION_1OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_1UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_1LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement><packagedElement name=\"Paspoort\" xmi:id=\"PASPOORT\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"ASSOCIATION_2OWNEDEND_1 ASSOCIATION_2OWNEDEND_2\" navigableOwnedEnd=\"ASSOCIATION_2OWNEDEND_2\" xmi:id=\"ASSOCIATION_2\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_2\" type=\"PASPOORT\" visibility=\"public\" xmi:id=\"ASSOCIATION_2OWNEDEND_1\"><upperValue value=\"1\" xmi:id=\"ASSOCIATION_2UPPERVALUE_1\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_2LOWERVALUE_1\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_2\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"ASSOCIATION_2OWNEDEND_2\"><upperValue value=\"*\" xmi:id=\"ASSOCIATION_2UPPERVALUE_2\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue value=\"0\" xmi:id=\"ASSOCIATION_2LOWERVALUE_2\" xmi:type=\"uml:LiteralInteger\"/></ownedEnd></packagedElement></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
		
		verify(fileMock);
	}
}
