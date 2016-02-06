package mine.allen.util.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * @author Russell.Loy, 
 * @version 0.1
 */
public class MyXMLParser   {

	private DocumentBuilder documentBuilder;
	String currentPath;
	Map<String, String> map;
	Map<String, Integer> countMap;

	private static MyXMLParser parserInstance;
	
	public static MyXMLParser getInstance() {
		if (parserInstance == null) {
			parserInstance = new MyXMLParser();
		}
		return parserInstance;
	}

	private MyXMLParser() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		map = new HashMap<String, String>();
		countMap = new HashMap<String, Integer>();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void parseDocument(String xmlString) {
		Document doc = null;
		map.clear();
		countMap.clear();
		try {
			doc = documentBuilder.parse(new ByteArrayInputStream(xmlString
					.getBytes("UTF-8")));

			Element root = doc.getDocumentElement();
			currentPath = "/" + root.getNodeName() + "#0";
			if (root.getNodeValue() != null) {
				map.put(currentPath, root.getNodeValue());
			}
			parseNode(root, currentPath);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseNode(Node e, String path) {
		NamedNodeMap attrs = e.getAttributes();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				map.put(path + "@" + attrs.item(i).getNodeName(), attrs.item(i)
						.getNodeValue());
			}
		}

		NodeList list = e.getChildNodes();
		if (list != null) {
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (e.getNodeValue() == null) {
					if (node.getNodeValue() != null) { /* text node */
						map.put(path, node.getNodeValue());
					}
				}
				if (countMap.containsKey(path + "/" + node.getNodeName())) {
					countMap.put(path + "/" + node.getNodeName(),
							countMap.get(path + "/" + node.getNodeName()) + 1);
				} else {
					countMap.put(path + "/" + node.getNodeName(), 0);
				}
				parseNode(node, path + "/" + node.getNodeName() + "#"
						+ countMap.get(path + "/" + node.getNodeName()));
			}
		}
	}
	
	
	public void parse(String xmlString) {
		parseDocument(xmlString);
	}

	
	public int getNodeCount(String path) {
		Integer i = countMap.get(path) == null ? countMap.get(formatPath(path, true))
				: countMap.get(path);
		return i == null ? 0 : i + 1;
	}


	public static String formatPath(String path, boolean isGetCount) {
		String t = path.replaceFirst("^/", "");
		t = t.replaceAll("/", "#0/").replaceAll("(#\\d+)#0\\/", "$1/");
		t = t.replaceAll("@", "#0@").replaceAll("(#\\d+)#0@", "$1@");
		if (!t.contains("@") && !t.matches(".*#\\d+$") && !isGetCount) {
			t += "#0";
		}		
		return "/" + t;
	}

	public String getNodeValue(String path) {
		return map.get(path) == null ? map.get(formatPath(path, false)) : map
				.get(path);
	}
	
	private void debug(){
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		System.out.println("~~~~~~~");
		for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
	public static void main(String[] args) throws Exception {
		MyXMLParser parser = new MyXMLParser();
		String xml = new String("<RootNode rAttr1='root_attr_1' rAttr2='root_attr2'>"
				+ "<other property='other prop'>123</other>"
				+ "<other property='other prop'>456</other>"
				+ "<level1 attr='level1_attr1'>"
				+ "1111111"
				+ "</level1>"
				+ "<level1 attr='level1_attr2'>"
				+ "中文"
				+ "</level1>"
				+ "<level1 attr='level1_attr3'>"
				+ "22222"
				+ "</level1>"
				+ "<level1 attr='level1_attr4' attr2='test'>"
				+ "<level2 attr='l2_attr1'><level3><![CDATA[<mytest>]]></level3></level2>"
				+ "</level1>" + "</RootNode>");
		// System.out.println(xml);
		parser.parse(xml);
		
		System.out.println("RootNode/level1 COUNT:" + parser.getNodeCount("/RootNode/level1"));
		for (int i = 0; i < parser.getNodeCount("/RootNode/level1"); i++) {
			System.out.println("~~"
					+ parser.getNodeValue("/RootNode/level1#" + i + ""));
		}
		System.out.println( parser.getNodeValue("/RootNode/level1#" + 3 + "/level2/level3"));
		System.out.println(parser.getNodeValue("/RootNode/level1#1@attr"));
		
		//----another xml test-----
		
		 parser.parse("<another><test1 attr='mytest'>hello</test1><test1>world</test1></another>");
		 System.out.println(parser.getNodeValue("/RootNode/level1#3/level2/level3"));
		 System.out.println(parser.getNodeValue("/another/test1#0@attr"));
		 System.out.println(parser.getNodeValue("/another/test1#1"));
		 System.out.println(parser.getNodeCount("/another/test1"));
	
	}
/*
 * <RootNode rAttr1='root_attr_1' rAttr2='root_attr2'>  
 <other property='other prop'>123</other>  
 <other property='other2 prop'>456</other>  
 <level1 attr='level1_attr1'>1111111</level1>  
 <level1 attr='level1_attr2'>1111111</level1>  
 <level1 attr='level1_attr3'>22222</level1>  
 <level1 attr='level1_attr4' attr2='test'>  
   <level2 attr='l2_attr1'>  
     <level3><![CDATA[<mytest>]]></level3>  
   </level2>  
  </level1>  
</RootNode>  

可以通过parser.getNodeValue("/RootNode#0/level1#3/level2#0/level3#0")或者
parser.getNodeValue("/RootNode/level1#3/level2/level3")访问到值mytest（路径中#0可写可不写） 
通过路径/RootNode/level1#1@attr可以访问到值level1_attr2 
通过路径/RootNode/level3#1@attr2可以访问到值test 
通过parser.getNodeCount("/RootNode/level1")可以返回level1的结点数是4。
 * */
}