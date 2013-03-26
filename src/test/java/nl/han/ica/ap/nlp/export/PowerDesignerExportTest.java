package nl.han.ica.ap.nlp.export;

import java.util.ArrayList;

import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.model.Class;

import org.junit.Test;

import static junit.framework.Assert.*;
import junit.framework.TestCase;

public class PowerDesignerExportTest extends TestCase{
	
	@Test
	public void testExport() {
		PowerDesignerExport exporter = new PowerDesignerExport();
		
		ArrayList<Class> classes = new ArrayList<Class>();
		Class vliegtuig = new Class("Vliegtuig");
		Class passagier = new Class("Passagier");
		classes.add(vliegtuig);
		classes.add(passagier);
		
		String output = exporter.export(classes);
		
		String expected_output = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n"
				+ "<uml:Model name=\"ObjectOrientedModel\" xmi:id=\"MODEL1\" xmi:version=\"2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElement name=\"Vliegtuig\" xmi:id=\"VLIEGTUIG\" xmi:type=\"uml:Class\"/><packagedElement name=\"Passagier\" xmi:id=\"PASSAGIER\" xmi:type=\"uml:Class\"/></uml:Model>";
		
		assertEquals(expected_output, output);
	}
}
