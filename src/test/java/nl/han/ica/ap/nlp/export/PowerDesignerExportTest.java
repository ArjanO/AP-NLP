package nl.han.ica.ap.nlp.export;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import nl.han.ica.ap.nlp.export.PowerDesignerExport;
import nl.han.ica.ap.nlp.model.Class;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

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
		
		String expected_output = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"?><uml:Modelname=\"ObjectOrientedModel\"xmi:version=\"2.1\"xmlns:uml=\"http://www.eclipse.org/uml2/2.1.0/UML\"xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\"><packagedElementname=\"Vliegtuig\"xmi:id=\"VLIEGTUIG\"xmi:type=\"uml:Class\"/><packagedElementname=\"Passagier\"xmi:id=\"PASSAGIER\"xmi:type=\"uml:Class\"/></uml:Model>";
		
		output = output.replaceAll("\\s","");
		expected_output = expected_output.replaceAll("\\s","");
		
		assertEquals(expected_output, output);
	}
}
