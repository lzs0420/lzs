package com.imooc.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.imooc.entity.Book;
import com.imooc.handler.SAXParserHandler;

public class SAXTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//ï¿½ï¿½È¡Ò»ï¿½ï¿½SAXParserFactoryï¿½ï¿½Êµï¿½ï¿½
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//Í¨ï¿½ï¿½factoryï¿½ï¿½È¡SAXParserÊµï¿½ï¿½
		try {
			SAXParser parser = factory.newSAXParser();
			//ï¿½ï¿½ï¿½ï¿½SAXParserHandlerï¿½ï¿½ï¿½ï¿½
			SAXParserHandler handler = new SAXParserHandler();
			parser.parse("books.xml", handler);
			System.out.println("~£¡~£¡~£¡¹²ÓÐ" + handler.getBookList().size()
					+ "±¾Êé");
			for (Book book : handler.getBookList()) {
				System.out.println(book.getId());
				System.out.println(book.getName());
				System.out.println(book.getAuthor());
				System.out.println(book.getYear());
				System.out.println(book.getPrice());
				System.out.println(book.getLanguage());
				System.out.println("----finish----");
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
