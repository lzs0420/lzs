package own.test.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory; //下面主要是org.xml.sax包的类
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SetLogUtils {
	private Logger log = Logger.getLogger(SetLogUtils.class);
	private String xmlfilename;

	public SetLogUtils(){
		xmlfilename = SetLogUtils.class.getResource("/").getPath();
		xmlfilename = xmlfilename.substring(1,xmlfilename.indexOf("classes")) + "etc/log4j.xml";
	}
	
	public void setloglevel(String logpackage, String loglevel) {
		log.info("修改日志级别");
		Document document = load(xmlfilename);
		Node root = document.getDocumentElement();
		// 获得第二级子节点的集合
		NodeList secondNodes = root.getChildNodes();
		if (secondNodes != null) {
			for (int i = 0; i < secondNodes.getLength(); i++) {
				// 循环获得第二级子节点
				Node secondNode = secondNodes.item(i);
				System.out.println(secondNode.getNodeName());
				log.info(secondNode.getNodeName());
				// 判断第二级节点是否为logger节点
				if (secondNode.getNodeType() == Node.ELEMENT_NODE
						&& secondNode.getNodeName().equals("logger")) {
					// 获得logger节点的name属性
					String name = secondNode.getAttributes().getNamedItem(
							"name").getNodeValue();
					System.out.println(name);
					log.info(name);
					// 循环获得logger下面的第三级节点
					for (Node thirdNode = secondNode.getFirstChild(); thirdNode != null; thirdNode = thirdNode
							.getNextSibling()) {
						// 判断第三级节点是否为level节点
						if (thirdNode.getNodeType() == Node.ELEMENT_NODE
								&& thirdNode.getNodeName().equals("level")) {
							// 获得level节点的value属性
							String value = thirdNode.getAttributes()
									.getNamedItem("value").getNodeValue();
							// 修改level节点的value属性
							if (logpackage.equals(name)) {
								thirdNode.getAttributes().getNamedItem("value")
										.setNodeValue(loglevel);
							}
						}
					}
				}
			}
		}
		// 修改class路径下面的log4j.xml文件
		doc2XmlFile(document, xmlfilename);
		// 在xml配置文件中追加<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">
		appendMethodA(xmlfilename);
		// 在内存中改变日志级别。
		changeLogLever(logpackage, loglevel);
		//动态加载日志文件
		configure(xmlfilename);
		log.info("修改完成!!");
	}

	// 修改文件日志路径
	public void setlogpath(String logname, String logpath) {
		log.info("修改日志路径");
		Document document = load(xmlfilename);
		Node root = document.getDocumentElement();
		// 获得第二级子节点的集合
		NodeList secondNodes = root.getChildNodes();
		if (secondNodes != null) {
			for (int i = 0; i < secondNodes.getLength(); i++) {
				// 循环获得第二级子节点
				Node secondNode = secondNodes.item(i);
				System.out.println("appender节点：" + secondNode.getNodeName());
				log.info("appender节点：" + secondNode.getNodeName());
				// 判断第二级节点是否为logger节点
				if (secondNode.getNodeType() == Node.ELEMENT_NODE
						&& secondNode.getNodeName().equals("appender")) {
					// 获得appender节点的name属性
					String name = secondNode.getAttributes().getNamedItem(
							"name").getNodeValue();
					// 循环获得logger下面的第三级节点
					for (Node thirdNode = secondNode.getFirstChild(); thirdNode != null; thirdNode = thirdNode
							.getNextSibling()) {
						// 判断第三级节点是否为level节点
						if (thirdNode.getNodeType() == Node.ELEMENT_NODE
								&& thirdNode.getNodeName().equals("param")) {
							String paramfilename = thirdNode.getAttributes()
									.getNamedItem("name").getNodeValue();
							if (paramfilename.equals("File")) {
								// 获得param节点的value属性
								String value = thirdNode.getAttributes()
										.getNamedItem("value").getNodeValue();
								log.info("日志路径:" + value);
								System.out.println("日志路径:" + value);
								// 修改param节点的value属性
								if (logname.equals(name)) {
									thirdNode.getAttributes().getNamedItem(
											"value").setNodeValue(logpath+"/"+name+".log");
								}
								// thirdNode.getAttributes().getNamedItem("value").setNodeValue("rrrrrrrrrr");
								// String value3
								// =thirdNode.getAttributes().getNamedItem("value").getNodeValue();
								// System.out.println("修改日志路径:"+value3);
							}
						}
					}
				}
			}
		}

		// 修改class路径下面的log4j.xml文件
		doc2XmlFile(document, xmlfilename);
		appendMethodA(xmlfilename);
		// 修改项目相对路径下面的log4j.xml文件
		// doc2XmlFile(document,"resources/log4j.xml");
		// 修改内存的路径
		changeLogPath(logpath, logname);
		//动态加载日志文件
		configure(xmlfilename);
		log.info("日志路径修改完成");
	}

	private void changeLogPath(String path, String appenderName) {
		FileAppender fileAppender = (FileAppender) Logger.getRootLogger()
				.getAppender(appenderName);
		fileAppender.setFile(path + "/" + appenderName + ".log");
		System.out.println("PATH:" + path + "/" + appenderName + ".log");
		fileAppender.activateOptions();
	}

	// 修改文件日志文件大小
	public void setlogsize(String logname, String size) {
		log.info("修改日志大小");
		Document document = load(xmlfilename);
		Node root = document.getDocumentElement();
		// 获得第二级子节点的集合
		NodeList secondNodes = root.getChildNodes();
		if (secondNodes != null) {
			for (int i = 0; i < secondNodes.getLength(); i++) {
				// 循环获得第二级子节点
				Node secondNode = secondNodes.item(i);
				System.out.println("appender节点：" + secondNode.getNodeName());
				log.info("appender节点：" + secondNode.getNodeName());
				// 判断第二级节点是否为logger节点
				if (secondNode.getNodeType() == Node.ELEMENT_NODE
						&& secondNode.getNodeName().equals("appender")) {
					// 获得appender节点的name属性
					String name = secondNode.getAttributes().getNamedItem(
							"name").getNodeValue();
					// 循环获得logger下面的第三级节点
					for (Node thirdNode = secondNode.getFirstChild(); thirdNode != null; thirdNode = thirdNode
							.getNextSibling()) {
						// 判断第三级节点是否为level节点
						if (thirdNode.getNodeType() == Node.ELEMENT_NODE
								&& thirdNode.getNodeName().equals("param")) {
							String paramfilsize = thirdNode.getAttributes()
									.getNamedItem("name").getNodeValue();
							log.info("日志大小：" + paramfilsize);
							if (paramfilsize.equals("maxFileSize")) {
								// 获得param节点的value属性
								String value = thirdNode.getAttributes()
										.getNamedItem("value").getNodeValue();
								System.out.println("日志路径:" + value);
								// 修改param节点的value属性
								if (logname.equals(name)) {
									thirdNode.getAttributes().getNamedItem(
											"value").setNodeValue(size + "KB");
								}
							}
						}
					}
				}
			}
		}

		// 修改class路径下面的log4j.xml文件
		doc2XmlFile(document, xmlfilename);
		appendMethodA(xmlfilename);
		// 修改项目相对路径下面的log4j.xml文件
		// doc2XmlFile(document,"resources/log4j.xml");
		// appendMethodA("resources/log4j.xml");
		configure(xmlfilename);
		log.info("日志大小修改完成");
	}

	// 加载配置配置文件
	public Document load(String filename) {
		System.out.println(filename);
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File(filename));
			document.normalize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	// 将修改的document保存xml文件
	public void doc2XmlFile(Document document, String filename) {
		try {
			/** 将document中的内容写入文件中 */
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			/** 编码 */
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			log.info("保存log4j.xml文件出错" + mye);
			mye.printStackTrace();
		} catch (IOException exp) {
			log.info("保存log4j.xml文件出错" + exp);
			exp.printStackTrace();
		}

	}

	// 在配置文件中追加<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">
	public void appendMethodA(String fileName) {
		try {
			String content = "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">";
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
			// 将写文件指针移到文件尾。
			randomFile.seek(str.length());
			// randomFile.writeUTF(content+readFileByBytes(fileName));
			randomFile.writeBytes(content + readFileByBytes(fileName));
			randomFile.close();
		} catch (IOException e) {
			log.info("追加<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">错误"
					+ e);
			e.printStackTrace();
		}
	}

	/**
	 * 读取出源xml文件中<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">后面的部分
	 * 用于上面追加的时候补全被覆盖的部分
	 * 
	 * @param fileName
	 * @return
	 */
	public String readFileByBytes(String fileName) {
		String content = "";
		String Start = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
		String Ending = "</log4j:configuration>";
		InputStream in = null;
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			// 一次读多个字节
			byte[] tempbytes = new byte[10000];
			int byteread = 0;
			in = new FileInputStream(fileName);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
				content += new String(tempbytes);
			}
		} catch (FileNotFoundException e) {
			log.info("读取log4j.xml文件出错" + e);
			e.printStackTrace();
		} catch (IOException e) {
			log.info("读取log4j.xml文件出错" + e);
			e.printStackTrace();
		} finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				log.info("读取log4j.xml文件出错" + e);
				e.printStackTrace();
			}
		}
		content = content.substring(Start.length(), content.length());
		String[] ss = content.split(Ending);
		content = ss[0] + "</log4j:configuration>";
		return content;

	}

	// 改变不同包下面的不同的级别
	public void changeLogLever(String logpackage, String logLevelmap) {
		Logger.getLogger(logpackage).setLevel(getLevel(logLevelmap));
	}

	// 将字符串转换成日志级别
	public Level getLevel(String level) {
		level = level.toUpperCase();
		System.out.println(level);
		if (level.equals("FATAL")) {
			return Level.FATAL;
		} else if (level.equals("ERROR")) {
			return Level.ERROR;
		} else if (level.equals("WARN")) {
			return Level.WARN;
		} else if (level.equals("INFO")) {
			return Level.INFO;
		} else {
			return Level.DEBUG;
		}
	}

	public static void main(String[] args) {
		SetLogUtils su = new SetLogUtils();
		su.configure(su.xmlfilename);
		su.setlogpath("org.zblog.zcw", "D://MyEclipse/logs/log.log");
		
	}
	//动态加载日志文件
	public void configure(String filename){
		DOMConfigurator.configure(filename);
	}
}


