/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.hwpf.usermodel;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hwpf.HWPFDocument;

import junit.framework.TestCase;

/**
 * Tests to ensure that our ranges end up with
 *  the right text in them, and the right font/styling
 *  properties applied to them.
 *
 * TODO - re-enable me when unicode paragraph stuff is fixed!
 */
public abstract class TestRangeProperties extends TestCase {
	private static final char page_break = (char)12;
	
	private static final String u_page_1 =
		"This is a fairly simple word document, over two pages, with headers and footers.\r" +
		"The trick with this one is that it contains some Unicode based strings in it.\r" +
		"Firstly, some currency symbols:\r" +
		"\tGBP - \u00a3\r" +
        "\tEUR - \u20ac\r" +
        "Now, we\u2019ll have some French text, in bold and big:\r" +
        "\tMoli\u00e8re\r" +
        "And some normal French text:\r" +
        "\tL'Avare ou l'\u00c9cole du mensonge\r" +
        "That\u2019s it for page one\r"
	;
	private static final String u_page_2 =
		"This is page two. Les Pr\u00e9cieuses ridicules. The end.\r"
	;
	
	private static final String a_page_1 = 
		"I am a test document\r" +
		"This is page 1\r" + 
		"I am Calibri (Body) in font size 11\r"
	;
	private static final String a_page_2 = 
		"This is page two\r" + 
		"It\u2019s Arial Black in 16 point\r" +
		"It\u2019s also in blue\r"
	;
	
	private HWPFDocument u;
	private HWPFDocument a;
	
	private String dirname;
	
	protected void setUp() throws Exception {
		dirname = System.getProperty("HWPF.testdata.path");
		u = new HWPFDocument(
				new FileInputStream(new File(dirname, "HeaderFooterUnicode.doc"))
		);
		a = new HWPFDocument(
				new FileInputStream(new File(dirname, "SampleDoc.doc"))
		);
	}
	
	
	public void testAsciiTextParagraphs() throws Exception {
		Range r = a.getRange();
		assertEquals(
				a_page_1 +
				page_break + "\r" + 
				a_page_2,
				r.text()
		);
		
		assertEquals(
				7,
				r.numParagraphs()
		);
		String[] p1_parts = a_page_1.split("\r");
		String[] p2_parts = a_page_2.split("\r");
		
		// Check paragraph contents
		assertEquals(
				p1_parts[0] + "\r",
				r.getParagraph(0).text()
		);
		assertEquals(
				p1_parts[1] + "\r",
				r.getParagraph(1).text()
		);
		assertEquals(
				p1_parts[2] + "\r",
				r.getParagraph(2).text()
		);
		
		assertEquals(
				page_break + "\r",
				r.getParagraph(3).text()
		);
		
		assertEquals(
				p2_parts[0] + "\r",
				r.getParagraph(4).text()
		);
		assertEquals(
				p2_parts[1] + "\r",
				r.getParagraph(5).text()
		);
		assertEquals(
				p2_parts[2] + "\r",
				r.getParagraph(6).text()
		);
	}
	
	public void testAsciiStyling() throws Exception {
		Range r = a.getRange();
		
		Paragraph p1 = r.getParagraph(0);
		Paragraph p7 = r.getParagraph(6);
		
		assertEquals(1, p1.numCharacterRuns());
		assertEquals(1, p7.numCharacterRuns());
		
		CharacterRun c1 = p1.getCharacterRun(0);
		CharacterRun c7 = p7.getCharacterRun(0);
		
		assertEquals("Times New Roman", c1.getFontName()); // No Calibri
		assertEquals("Arial Black", c7.getFontName());
		assertEquals(22, c1.getFontSize());
		assertEquals(32, c7.getFontSize());
	}
	

	public void testUnicodeTextParagraphs() throws Exception {
		Range r = u.getRange();
		assertEquals(
				u_page_1 +
				page_break + "\r" + 
				u_page_2,
				r.text()
		);
		
		assertEquals(
				5,
				r.numParagraphs()
		);
		String[] p1_parts = u_page_1.split("\r");
		String[] p2_parts = u_page_2.split("\r");
		
		System.out.println(r.getParagraph(2).text());
		// TODO
	}
	public void testUnicodeStyling() throws Exception {
		// TODO
	}
}
