package com.chccc.bible;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;


public class ChapterXmlParser {
	
	public static ChapterDO getChapterContent(Context applicationContext, String bookVersion, String bookNumber, String chapterNumber) {
		ArrayList<String> lines = new ArrayList();
		
		ChapterDO chapter = new ChapterDO();
		
		try {
			InputStream is = null;
			is = applicationContext.getAssets().open(String.format("data/%s/%s%s.xml", bookVersion, bookVersion, bookNumber));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(is));
			doc.getDocumentElement().normalize();

			NodeList bookNodeList = doc.getElementsByTagName("Book");
			
			if (bookNodeList !=null) {
				Element bookElement = (Element) bookNodeList.item(0);
				
				chapter.setBookChineseName(bookElement.getAttribute("name"));
				chapter.setBookEnglishName(bookElement.getAttribute("englishName"));
				chapter.setChapterCount(bookElement.getAttribute("chapters"));
				
				NodeList chapterNodeList = bookElement.getElementsByTagName("Chapter");
				
				for (int i = 0; i < chapterNodeList.getLength(); i++) {
					Element chapterElement = (Element) chapterNodeList.item(i);
					String chapterNumberT = chapterElement.getAttribute("number");
					if (chapterNumberT.equals(chapterNumber)) {
						NodeList lineNodeList = chapterElement.getElementsByTagName("Line");
						for (int j = 0; j < lineNodeList.getLength(); j++) {
							Element LineElement = (Element) lineNodeList.item(j);
							String lineNumber = LineElement.getAttribute("number");
							String lineContent = lineNodeList.item(j).getChildNodes().item(0).getNodeValue();
							lines.add(lineNumber + "\n" + lineContent + "\n\n");
						}
						
						break;
					}
				}
			}
			
			chapter.setVerses(lines);
			

		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}
		
		return chapter;
	}
}
