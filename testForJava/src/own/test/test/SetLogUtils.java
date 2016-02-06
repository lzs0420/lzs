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
import javax.xml.parsers.DocumentBuilderFactory; //������Ҫ��org.xml.sax������
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
		log.info("�޸���־����");
		Document document = load(xmlfilename);
		Node root = document.getDocumentElement();
		// ��õڶ����ӽڵ�ļ���
		NodeList secondNodes = root.getChildNodes();
		if (secondNodes != null) {
			for (int i = 0; i < secondNodes.getLength(); i++) {
				// ѭ����õڶ����ӽڵ�
				Node secondNode = secondNodes.item(i);
				System.out.println(secondNode.getNodeName());
				log.info(secondNode.getNodeName());
				// �жϵڶ����ڵ��Ƿ�Ϊlogger�ڵ�
				if (secondNode.getNodeType() == Node.ELEMENT_NODE
						&& secondNode.getNodeName().equals("logger")) {
					// ���logger�ڵ��name����
					String name = secondNode.getAttributes().getNamedItem(
							"name").getNodeValue();
					System.out.println(name);
					log.info(name);
					// ѭ�����logger����ĵ������ڵ�
					for (Node thirdNode = secondNode.getFirstChild(); thirdNode != null; thirdNode = thirdNode
							.getNextSibling()) {
						// �жϵ������ڵ��Ƿ�Ϊlevel�ڵ�
						if (thirdNode.getNodeType() == Node.ELEMENT_NODE
								&& thirdNode.getNodeName().equals("level")) {
							// ���level�ڵ��value����
							String value = thirdNode.getAttributes()
									.getNamedItem("value").getNodeValue();
							// �޸�level�ڵ��value����
							if (logpackage.equals(name)) {
								thirdNode.getAttributes().getNamedItem("value")
										.setNodeValue(loglevel);
							}
						}
					}
				}
			}
		}
		// �޸�class·�������log4j.xml�ļ�
		doc2XmlFile(document, xmlfilename);
		// ��xml�����ļ���׷��<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">
		appendMethodA(xmlfilename);
		// ���ڴ��иı���־����
		changeLogLever(logpackage, loglevel);
		//��̬������־�ļ�
		configure(xmlfilename);
		log.info("�޸����!!");
	}

	// �޸��ļ���־·��
	public void setlogpath(String logname, String logpath) {
		log.info("�޸���־·��");
		Document document = load(xmlfilename);
		Node root = document.getDocumentElement();
		// ��õڶ����ӽڵ�ļ���
		NodeList secondNodes = root.getChildNodes();
		if (secondNodes != null) {
			for (int i = 0; i < secondNodes.getLength(); i++) {
				// ѭ����õڶ����ӽڵ�
				Node secondNode = secondNodes.item(i);
				System.out.println("appender�ڵ㣺" + secondNode.getNodeName());
				log.info("appender�ڵ㣺" + secondNode.getNodeName());
				// �жϵڶ����ڵ��Ƿ�Ϊlogger�ڵ�
				if (secondNode.getNodeType() == Node.ELEMENT_NODE
						&& secondNode.getNodeName().equals("appender")) {
					// ���appender�ڵ��name����
					String name = secondNode.getAttributes().getNamedItem(
							"name").getNodeValue();
					// ѭ�����logger����ĵ������ڵ�
					for (Node thirdNode = secondNode.getFirstChild(); thirdNode != null; thirdNode = thirdNode
							.getNextSibling()) {
						// �жϵ������ڵ��Ƿ�Ϊlevel�ڵ�
						if (thirdNode.getNodeType() == Node.ELEMENT_NODE
								&& thirdNode.getNodeName().equals("param")) {
							String paramfilename = thirdNode.getAttributes()
									.getNamedItem("name").getNodeValue();
							if (paramfilename.equals("File")) {
								// ���param�ڵ��value����
								String value = thirdNode.getAttributes()
										.getNamedItem("value").getNodeValue();
								log.info("��־·��:" + value);
								System.out.println("��־·��:" + value);
								// �޸�param�ڵ��value����
								if (logname.equals(name)) {
									thirdNode.getAttributes().getNamedItem(
											"value").setNodeValue(logpath+"/"+name+".log");
								}
								// thirdNode.getAttributes().getNamedItem("value").setNodeValue("rrrrrrrrrr");
								// String value3
								// =thirdNode.getAttributes().getNamedItem("value").getNodeValue();
								// System.out.println("�޸���־·��:"+value3);
							}
						}
					}
				}
			}
		}

		// �޸�class·�������log4j.xml�ļ�
		doc2XmlFile(document, xmlfilename);
		appendMethodA(xmlfilename);
		// �޸���Ŀ���·�������log4j.xml�ļ�
		// doc2XmlFile(document,"resources/log4j.xml");
		// �޸��ڴ��·��
		changeLogPath(logpath, logname);
		//��̬������־�ļ�
		configure(xmlfilename);
		log.info("��־·���޸����");
	}

	private void changeLogPath(String path, String appenderName) {
		FileAppender fileAppender = (FileAppender) Logger.getRootLogger()
				.getAppender(appenderName);
		fileAppender.setFile(path + "/" + appenderName + ".log");
		System.out.println("PATH:" + path + "/" + appenderName + ".log");
		fileAppender.activateOptions();
	}

	// �޸��ļ���־�ļ���С
	public void setlogsize(String logname, String size) {
		log.info("�޸���־��С");
		Document document = load(xmlfilename);
		Node root = document.getDocumentElement();
		// ��õڶ����ӽڵ�ļ���
		NodeList secondNodes = root.getChildNodes();
		if (secondNodes != null) {
			for (int i = 0; i < secondNodes.getLength(); i++) {
				// ѭ����õڶ����ӽڵ�
				Node secondNode = secondNodes.item(i);
				System.out.println("appender�ڵ㣺" + secondNode.getNodeName());
				log.info("appender�ڵ㣺" + secondNode.getNodeName());
				// �жϵڶ����ڵ��Ƿ�Ϊlogger�ڵ�
				if (secondNode.getNodeType() == Node.ELEMENT_NODE
						&& secondNode.getNodeName().equals("appender")) {
					// ���appender�ڵ��name����
					String name = secondNode.getAttributes().getNamedItem(
							"name").getNodeValue();
					// ѭ�����logger����ĵ������ڵ�
					for (Node thirdNode = secondNode.getFirstChild(); thirdNode != null; thirdNode = thirdNode
							.getNextSibling()) {
						// �жϵ������ڵ��Ƿ�Ϊlevel�ڵ�
						if (thirdNode.getNodeType() == Node.ELEMENT_NODE
								&& thirdNode.getNodeName().equals("param")) {
							String paramfilsize = thirdNode.getAttributes()
									.getNamedItem("name").getNodeValue();
							log.info("��־��С��" + paramfilsize);
							if (paramfilsize.equals("maxFileSize")) {
								// ���param�ڵ��value����
								String value = thirdNode.getAttributes()
										.getNamedItem("value").getNodeValue();
								System.out.println("��־·��:" + value);
								// �޸�param�ڵ��value����
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

		// �޸�class·�������log4j.xml�ļ�
		doc2XmlFile(document, xmlfilename);
		appendMethodA(xmlfilename);
		// �޸���Ŀ���·�������log4j.xml�ļ�
		// doc2XmlFile(document,"resources/log4j.xml");
		// appendMethodA("resources/log4j.xml");
		configure(xmlfilename);
		log.info("��־��С�޸����");
	}

	// �������������ļ�
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

	// ���޸ĵ�document����xml�ļ�
	public void doc2XmlFile(Document document, String filename) {
		try {
			/** ��document�е�����д���ļ��� */
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			/** ���� */
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerException mye) {
			log.info("����log4j.xml�ļ�����" + mye);
			mye.printStackTrace();
		} catch (IOException exp) {
			log.info("����log4j.xml�ļ�����" + exp);
			exp.printStackTrace();
		}

	}

	// �������ļ���׷��<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">
	public void appendMethodA(String fileName) {
		try {
			String content = "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">";
			// ��һ����������ļ���������д��ʽ
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// �ļ����ȣ��ֽ���
			String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
			// ��д�ļ�ָ���Ƶ��ļ�β��
			randomFile.seek(str.length());
			// randomFile.writeUTF(content+readFileByBytes(fileName));
			randomFile.writeBytes(content + readFileByBytes(fileName));
			randomFile.close();
		} catch (IOException e) {
			log.info("׷��<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">����"
					+ e);
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��Դxml�ļ���<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">����Ĳ���
	 * ��������׷�ӵ�ʱ��ȫ�����ǵĲ���
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
			System.out.println("���ֽ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�");
			// һ�ζ�����ֽ�
			byte[] tempbytes = new byte[10000];
			int byteread = 0;
			in = new FileInputStream(fileName);
			// �������ֽڵ��ֽ������У�bytereadΪһ�ζ�����ֽ���
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
				content += new String(tempbytes);
			}
		} catch (FileNotFoundException e) {
			log.info("��ȡlog4j.xml�ļ�����" + e);
			e.printStackTrace();
		} catch (IOException e) {
			log.info("��ȡlog4j.xml�ļ�����" + e);
			e.printStackTrace();
		} finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				log.info("��ȡlog4j.xml�ļ�����" + e);
				e.printStackTrace();
			}
		}
		content = content.substring(Start.length(), content.length());
		String[] ss = content.split(Ending);
		content = ss[0] + "</log4j:configuration>";
		return content;

	}

	// �ı䲻ͬ������Ĳ�ͬ�ļ���
	public void changeLogLever(String logpackage, String logLevelmap) {
		Logger.getLogger(logpackage).setLevel(getLevel(logLevelmap));
	}

	// ���ַ���ת������־����
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
	//��̬������־�ļ�
	public void configure(String filename){
		DOMConfigurator.configure(filename);
	}
}


