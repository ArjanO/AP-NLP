package nl.han.ica.ap.nlp.export;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.model.Class;
import nl.han.ica.ap.nlp.model.IClass;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

import junit.framework.TestCase;

public class PowerDesignerExportTest extends TestCase{
	
	@Test
	public void testExport() {
		PowerDesignerExport exporter = new PowerDesignerExport();
		
		ArrayList<IClass> classes = new ArrayList<IClass>();
		IClass vliegtuig = new Class("Vliegtuig");
		IClass passagier = new Class("Passagier");
		vliegtuig.getAttributes().add(passagier);
		classes.add(vliegtuig);
		
		String exportpath = exporter.export(classes);
		
		assertNotNull(exportpath);
		
		String output = null;
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(exportpath);
		} catch (FileNotFoundException e) {
			fail("File was not found.");
		}
		
	    try {
	        output = IOUtil.toString(inputStream);
	    } catch (IOException e) {
	    	fail("Can't read file.");
		} finally {
	        try {
				inputStream.close();
			} catch (IOException e) {
				fail("Can't close file.");
			}
	    }
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uml:Model name=\"ObjectOrientedModel\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/><packagedElement memberEnd=\"OWNEDEND_VLIEGTUIG OWNEDEND_PASSAGIER\" name=\"Association_TEST\" navigableOwnedEnd=\"OWNEDEND_PASSAGIER\" xmi:id=\"ASSOCIATION_TEST\" xmi:type=\"uml:Association\"><ownedEnd association=\"ASSOCIATION_VLIEGTUIG\" name=\"\" type=\"VLIEGTUIG\" visibility=\"public\" xmi:id=\"OWNEDEND_VLIEGTUIG\"><upperValue value=\"1\" xmi:id=\"UPPERVALUE_VLIEGTUIG\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue xmi:id=\"LOWERVALUE_VLIEGTUIG\" xmi:type=\"LiteralInteger\"/></ownedEnd><ownedEnd association=\"ASSOCIATION_PASSAGIER\" name=\"\" type=\"PASSAGIER\" visibility=\"public\" xmi:id=\"OWNEDEND_PASSAGIER\"><upperValue value=\"1\" xmi:id=\"UPPERVALUE_PASSAGIER\" xmi:type=\"uml:LiteralUnlimitedNatural\"/><lowerValue xmi:id=\"LOWERVALUE_PASSAGIER\" xmi:type=\"LiteralInteger\"/></ownedEnd></packagedElement></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
	}
}
