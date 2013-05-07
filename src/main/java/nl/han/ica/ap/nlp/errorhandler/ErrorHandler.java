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
package nl.han.ica.ap.nlp.errorhandler;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.misc.IntervalSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.*;
import javax.sound.sampled.*;

import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.sound.sampled.*;

/**
 * @author Joell
 * 
 */
public class ErrorHandler extends DefaultErrorStrategy {
	private String msg = "Wrong input, exiting application...";
	
	public ErrorHandler() {
		super();
	}	
	
	@Override
	public void reportUnwantedToken(Parser recognizer) {
		handleError();			
	}
	
	@Override
	public void reportError(Parser recognizer, RecognitionException e)
		throws RecognitionException
	{
		handleError();
	}

	@Override
	public void reportNoViableAlternative(Parser recognizer, NoViableAltException e)
	throws RecognitionException
	{
		handleError();
	}

	@Override
	public void reportInputMismatch(Parser recognizer, InputMismatchException e)
		throws RecognitionException
	{
		handleError();
	}

	@Override
	public void reportFailedPredicate(Parser recognizer, FailedPredicateException e)
		throws RecognitionException
	{
		handleError();
	}


	@Override
	public void reportMissingToken(Parser recognizer) {
		handleError();
	}
	
	/**
	 * Gives a textual errormessage and a brilliant sound message.
	 */
	private void handleError() {		
		try {
			File file = new File("res/fail.wav");
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			clip.open(ais);
			clip.loop(0);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {					
					JOptionPane.showMessageDialog(null, "Close to exit!");
				}
			});
			System.out.println(msg);
			Thread.sleep(3000);					
			System.exit(1);
		} catch (Exception e) {}	
	}
}
