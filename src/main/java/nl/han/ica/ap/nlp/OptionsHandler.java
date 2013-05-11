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
package nl.han.ica.ap.nlp;

import nl.han.ica.ap.nlp.util.File;



/**
 * @author Joell
 *
 */
public class OptionsHandler {
	
	private File inputfile;
	private App app;
	
	public OptionsHandler(String[] args, App app) {
		this.app = app;
		handleOptions(args);
	}
	
	public File getInputfile() {
		return inputfile;
	}

	/**
	 * Handels the console options.
	 * @param args The options of the console input.
	 */
	public void handleOptions(String[] args) {
		for(int i = 0;i < args.length; i++) {
			if(args[i].equals("-inputfile")) {
				if(hasOptionValue(args,i)) {
					inputfile = new File(args[++i]);
				} else {
					System.out.println("No inputfile given, switching to default input");
				}
			} else if(args[i].equals("-exporter")) {
				if(hasOptionValue(args,i)) {
					app.setExportType(args[++i]);
				} else {
					app.selectExporter();
				}
			}
		}
	}
	
	private boolean hasOptionValue(String[] options, int option) {
		if(option + 1 >= options.length) {
			return false;
		} else if(options[option+1].startsWith("-")) {
			return false;
		}
		return true;		
	}
	
}
