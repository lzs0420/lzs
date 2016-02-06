package com.imooc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.imooc.entity.Book;

public class SAXTestCreate {

	public void createXML() {
		Book b1 = new Book();
		b1.setId("1");
		b1.setName("冰与火之歌");
		b1.setAuthor("乔治马丁");
		b1.setYear("2014");
		b1.setPrice("89");
		Book b2 = new Book();
		b2.setId("2");
		b2.setName("安徒生童话");
		b2.setAuthor("乔治马丁");
		b2.setYear("2004");
		b2.setPrice("77");
		b2.setLanguage("English");
		ArrayList<Book> bookList = new ArrayList<Book>();
		bookList.add(b1);
		bookList.add(b2);
		// 生成xml
		// 1.创建一个TransformerFactory类的对象
		SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		try {
			// 2.通过SAXTransformerFactory对象创建一个TransformerHandler对象
			TransformerHandler handler = tff.newTransformerHandler();
			// 3.通过handler对象创建一个Transformer对象
//			Transformer tr = handler.getTransformer();
//			// 4.通过Transformer对象对生成的xml文件进行设置
//			// 设置xml的编码
//			tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			// 设置xml的“是否换行”
//			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			// 5.创建一个Result对象
			File f = new File("src/res/newbooks.xml");
			if (!f.exists()) {
				f.createNewFile();
			}
			// 6.创建Result对象，并且使其与handler关联
			Result result = new StreamResult(new FileOutputStream(f));
			handler.setResult(result);
			// 7.利用handler对象进行xml文件内容的编写O
			// 打开document
			handler.startDocument();
			AttributesImpl attr = new AttributesImpl();
			handler.startElement("", "", "bookstore", attr);
			
			for (Book book : bookList) {
				attr.clear();
				attr.addAttribute("", "", "id", "", book.getId());
				handler.startElement("", "", "book", attr);
				// 创建name节点
				if (book.getName() != null && !book.getName().trim().equals("")) {
					attr.clear();
					handler.startElement("", "", "name", attr);
					handler.characters(book.getName().toCharArray(), 0, book
							.getName().length());
					handler.endElement("", "", "name");
				}
				// 创建year节点
				if (book.getYear() != null && !book.getYear().trim().equals("")) {
					attr.clear();
					handler.startElement("", "", "year", attr);
					handler.characters(book.getYear().toCharArray(), 0, book
							.getYear().length());
					handler.endElement("", "", "year");
				}
				// 创建author节点
				if (book.getAuthor() != null && !book.getAuthor().trim().equals("")) {
					attr.clear();
					handler.startElement("", "", "author", attr);
					handler.characters(book.getAuthor().toCharArray(), 0, book
							.getAuthor().length());
					handler.endElement("", "", "author");
				}
				// 创建price节点
				if (book.getPrice() != null && !book.getPrice().trim().equals("")) {
					attr.clear();
					handler.startElement("", "", "price", attr);
					handler.characters(book.getPrice().toCharArray(), 0, book
							.getPrice().length());
					handler.endElement("", "", "price");
				}
				// 创建language节点
				if (book.getLanguage() != null && !book.getLanguage().trim().equals("")) {
					attr.clear();
					handler.startElement("", "", "language", attr);
					handler.characters(book.getLanguage().toCharArray(), 0, book
							.getLanguage().length());
					handler.endElement("", "", "language");
				}
				handler.endElement("", "", "book");
			}
			handler.endElement("", "", "bookstore");
			// 关闭document
			handler.endDocument();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SAXTestCreate test = new SAXTestCreate();
		test.createXML();
	}

}
