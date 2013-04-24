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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.export.ClassRelation;
import nl.han.ica.ap.nlp.model.Association;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class PowerDesignerExportTest {
	private Capture<String> content = new Capture<String>(); 
	
	public IFile buildFileMockTest() {
		IFile testFile = createMock(IFile.class);
		
		expect(testFile.getContent()).andReturn("").anyTimes();
		testFile.setContent(capture(content));
		expect(testFile.write()).andReturn(true).anyTimes();
		expect(testFile.getPath()).andReturn("").anyTimes();
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
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
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
	}
	
	@Test
	public void testDoc() {
		PowerDesignerExport exporter = new PowerDesignerExport();
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		Class paspoort = new Class("Paspoort");

		vliegtuig.addAssociation(passagier);
		passagier.addAssociation(paspoort);
		paspoort.addAssociation(vliegtuig);
		classes.add(vliegtuig);
		
		Document resultdoc = exporter.exportDoc(classes);
		
		ArrayList<Class> class_list = exporter.getClassList(classes, new ArrayList<Class>());
		ArrayList<ClassRelation> asso_list = exporter.getAssociationList(classes, new ArrayList<ClassRelation>());
		
		assertTrue(testDoc(resultdoc, class_list, asso_list));
	}
	
	/**
	 * Check a Document for classes and associations, if all are found in the arraylists the doc is correct.
	 * @param resultdoc The document to check
	 * @param class_list List of Classes
	 * @param asso_list List of ClassRelations
	 * @return true if document is correct, false if incorrect
	 */
	public boolean testDoc(Document resultdoc, ArrayList<Class> class_list, ArrayList<ClassRelation> asso_list){
		for (int i = 0; i < resultdoc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node childnode = resultdoc.getDocumentElement().getChildNodes().item(i);
			String nodetype = childnode.getAttributes().getNamedItem(PowerDesignerXML.attributename_xmi_type).getNodeValue();
			
			if(nodetype == PowerDesignerXML.attributetype_uml_class){
				String nodename = childnode.getAttributes().getNamedItem(PowerDesignerXML.attributename_name).getNodeValue();
				Class tmpclass = new Class(nodename);
				if(!class_list.contains(tmpclass))
					return false;
			}
			
			if(nodetype == PowerDesignerXML.attributetype_uml_association){
				String classname1 = childnode.getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue();
				String classname2 = childnode.getChildNodes().item(1).getAttributes().getNamedItem("type").getNodeValue();
				ClassRelation tmprelation = new ClassRelation(new Class(classname1), new Class(classname2));
				
				boolean relation_found = false;
				for(ClassRelation asso_list_relation : asso_list){
					if(asso_list_relation.class1.getName().toUpperCase().equals(tmprelation.class1.getName()) &&
					   asso_list_relation.class2.getName().toUpperCase().equals(tmprelation.class2.getName())){
						relation_found = true;
						break;
					}
				}
				if(!relation_found)
					return false;
			}
		}
		return true;
	}
}
