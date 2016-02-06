package mine.allen.util.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import mine.allen.util.lang.LogL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserHandler extends DefaultHandler {
	/**element的value*/
	private String value = null;
	private static ArrayList<SAXParserXmlRecord> recordList;

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		recordList = new ArrayList<SAXParserXmlRecord>();
		LogL.getInstance().getLog().debug("*********使用SAX解析XML开始*********");
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		LogL.getInstance().getLog().debug("*********使用SAX解析XML结束*********");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		HashMap<String,String> hm = new HashMap<String,String>();
		for(int i = 0; i < attributes.getLength(); i++){
			hm.put(attributes.getQName(i), attributes.getValue(i));
		}
		recordList.add(new SAXParserXmlRecord(qName, "", true, hm));
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		recordList.add(new SAXParserXmlRecord(qName, value, false, null));
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		String temp = new String(ch, start, length);
		if (!temp.trim().equals("")) {
			value = temp;
		}
	}
	
	public ArrayList<SAXParserXmlRecord> getRecordList(){
		return recordList;
	}
	
	/**
	 * SAX方式生成xml文档
	 */
	public void SAXCreateXML() {
		SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		try {
			TransformerHandler handler = tff.newTransformerHandler();
			Transformer tr = handler.getTransformer();
			tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			File f = new File("books2.xml");
			if (!f.exists()) {
				f.createNewFile();
			}
			Result result = new StreamResult(new FileOutputStream(f));
			handler.setResult(result);
			handler.startDocument();
			
			AttributesImpl attr = new AttributesImpl();
			handler.startElement("", "", "c3p0-config", attr);
			
			attr.clear();
			handler.startElement("", "", "default-config", attr);
			
			attr.clear();
			handler.startElement("", "", "property", attr);
			attr.addAttribute("", "", "name", "", "user");
			handler.characters("root".toCharArray(), 0, "root".length());
			handler.endElement("", "", "property");
			
			attr.clear();
			handler.startElement("", "", "property", attr);
			attr.addAttribute("", "", "name", "", "password");
			handler.characters("com.mysql.jdbc.Driver".toCharArray(), 0, "com.mysql.jdbc.Driver".length());
			handler.endElement("", "", "property");
		
			handler.endElement("", "", "book");
			handler.endElement("", "", "bookstore");
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
	
	public void saxXmlParser(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			SAXParserHandler handler = new SAXParserHandler();
			parser.parse("etc/c3p0-config.xml", handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class SAXParserXmlRecord{
		private String nodeName;
		private String nodeValue;
		private Boolean startOrEnd;
		private HashMap<String,String> attributes;
		public SAXParserXmlRecord() {
			super();
		}
		public SAXParserXmlRecord(String nodeName, String nodeValue,
				Boolean startOrEnd, HashMap<String, String> attributes) {
			super();
			this.nodeName = nodeName;
			this.nodeValue = nodeValue;
			this.startOrEnd = startOrEnd;
			this.attributes = attributes;
		}
		public String getNodeName() {
			return nodeName;
		}
		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}
		public String getNodeValue() {
			return nodeValue;
		}
		public void setNodeValue(String nodeValue) {
			this.nodeValue = nodeValue;
		}
		public Boolean getStartOrEnd() {
			return startOrEnd;
		}
		public void setStartOrEnd(Boolean startOrEnd) {
			this.startOrEnd = startOrEnd;
		}
		public HashMap<String, String> getAttributes() {
			return attributes;
		}
		public void setAttributes(HashMap<String, String> attributes) {
			this.attributes = attributes;
		}
	}
	
	public static void main(String[] args) {
		SAXParserHandler handler = new SAXParserHandler();
		handler.saxXmlParser();
		ArrayList<SAXParserXmlRecord> as = handler.getRecordList();
		for (SAXParserXmlRecord sp : as) {
			System.out.println("【"+as.indexOf(sp)+"】");
			System.out.println(sp.getNodeName());
			System.out.println(sp.getNodeValue());
			System.out.println(sp.getStartOrEnd());
			HashMap<String, String> hm = sp.getAttributes();
			if(hm!=null){
				Set<Entry<String, String>> en = hm.entrySet();
				Iterator<Entry<String, String>> ir = en.iterator();
				while(ir.hasNext()){
					Entry<String, String> ene = ir.next();
					System.out.println(ene.getKey()+" = "+ene.getValue());
				}
			}
			System.out.println("********************************");
		}
	}
}
