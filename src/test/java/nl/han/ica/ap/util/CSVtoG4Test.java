package nl.han.ica.ap.util;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import nl.han.ica.ap.nlp.util.CSVtoG4;
import nl.han.ica.ap.nlp.util.File;
import nl.han.ica.ap.nlp.util.IFile;

import org.easymock.Capture;
import org.junit.Test;

public class CSVtoG4Test {
	private Capture<String> content = new Capture<String>();

	public IFile buildFileMockTest() {
		IFile testFile = createMock(IFile.class);
		
		testFile.setContent(capture(content));
		expect(testFile.getContent()).andReturn("hebben;LR;heeft,had,hebben\nbevatten;LR;bevat,bevatten\nrijden;LR;rijd,rijdt,reed,reden").anyTimes();
		try {
			expect(testFile.read()).andReturn(true).anyTimes();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		expect(testFile.write()).andReturn(true).anyTimes();
		
		replay(testFile);
		
		return testFile;
	}
	
	@Test
	public void export() {
		CSVtoG4 csvtog4 = new CSVtoG4();
		
		IFile importFile = buildFileMockTest();
		csvtog4.setImportFile(importFile);
		
		IFile exportFile = buildFileMockTest();
		csvtog4.setExportFile(exportFile);
		csvtog4.export();
		
		String output = content.getValue();

		String expected_output = "lexer grammar NlpWerkwoorden;\n\nWERKWOORD : ('heeft'|'had'|'hebben'|'bevat'|'bevatten'|'rijd'|'rijdt'|'reed'|'reden');";
		
		assertEquals(expected_output, output);
		
		verify(exportFile);
	}

}
