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
package nl.han.ica.ap.nlp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

/**
 * A File class with write, read and append functionality.
 * @author michieltje
 *
 */
public class File implements IFile {
	private String path;
	private StringBuffer content;
	
	/**
	 * Create a new file at path.
	 * @param path
	 */
	public File(String path) {
		this.path = path;
	}
	
	/**
	 * Download a file from a url.
	 * @param urlstring
	 * @throws IOException
	 */
	public void download(String downloadpath) throws IOException {
		URL url = new URL(downloadpath);
		InputStream in = null;
		ByteArrayOutputStream out = null;
		
		in = new BufferedInputStream(url.openStream());
		out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		
		while (-1!=(n=in.read(buf))) {
		   out.write(buf, 0, n);
		}
		out.close();
		in.close();
		
		//save to file
		FileOutputStream fos = null;
		fos = new FileOutputStream(path);
		fos.write(out.toByteArray());
		fos.close();
	}
	
	/**
	 * Write content to path.
	 * @throws FileNotFoundException 
	 */
	public boolean write() {
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream(this.path));
		    out.print(this.content.toString());
		} catch (FileNotFoundException e) {
			System.out.println("File not found, can't write.");
		}
		finally {
		    if (out != null){
		    	out.close();
		    	return true;
		    }
		}
		return false;
	}
	
	/**
	 * Append content to path. 
	 */
	public void append(String content) {
		this.content.append(content);
	}
	
	/**
	 * Read a file as a string.
	 * @throws FileNotFoundException 
	 */
    public boolean read() throws FileNotFoundException {
    	StringBuffer fileData = new StringBuffer(1000);
    	BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(this.path));
		
    	char[] buf = new char[1024];
    	int numRead=0;
    	try {
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	this.content = fileData;
    	
    	if (fileData.length() > 0) {
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * Set the content of this file.
     * @param content
     */
    public void setContent(String content) {
    	this.content = new StringBuffer(content);
    }
    
    /**
     * Get the content of this class.
     * @return
     */
    public String getContent() {
    	return this.content.toString();
    }

	/* (non-Javadoc)
	 * @see nl.han.ica.ap.nlp.util.IFile#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}
}
