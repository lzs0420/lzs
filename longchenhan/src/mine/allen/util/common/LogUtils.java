package mine.allen.util.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * ������־��һЩ����
 * @author Allen
 * @version 1.0
 */
public class LogUtils {
	
	/**����ģʽ��˫������ʵ��*/
	private static LogUtils Logutils=null;
	/**Logger����*/
	private static Logger logger;
	/**Log����*/
	private static Log log;
	
	/**����ģʽ��˫��������ʼ������*/
    private LogUtils(){
        //do something
    	logger = Logger.getLogger(""); 
		log = LogFactory.getLog("");
    }

    /**
     * ����Logger����
     * @return Logger
     */
    public Logger getLogger() {
		return logger;
	}

    /**
     * ����Log����
     * @return Log
     */
    public Log getLog() {
		return log;
	}

	/**����ģʽ��˫��������ȡʵ������*/
    public static LogUtils getInstance(){
        if(Logutils==null){
            synchronized(LogUtils.class){
                if(null==Logutils){
                	Logutils=new LogUtils();
                }
            }
        }
        return Logutils;
    }
    
    /**��config�ļ�·����ʼ��log<br>��������Ĭ�����ó�ʼ��
     * */
    public static void loadConfig(String config){
    	String filePath = FileUtils.getConfigAbsolutepath();
    	filePath = FileUtils.getFullName(filePath, config);
    	
    	//DOMConfigurator.configure("E:/study/log4j/log4j.xml");//����.xml�ļ� 
    	if (FileUtils.isFileExist(config)) {  
            System.out.println("ʹ��: " + config+"��ʼ����־������Ϣ");  
            PropertyConfigurator.configure(config);  
        }else if (FileUtils.isFileExist(filePath)) {  
            System.out.println("ʹ��: " + filePath+"��ʼ����־������Ϣ");  
            PropertyConfigurator.configure(filePath);  
        }else {  
            System.err.println("*** " + config + " �ļ�û���ҵ���*** " + filePath + " �ļ�û���ҵ�, ����ʹ�� BasicConfigurator��ʼ��");  
            BasicConfigurator.configure();  
        }  
    }
    
    /**�滻��ʼ���ļ�config��ͬʱд��config
     * */
    public static void replaceAndWriteConfig(String config ,HashMap<String, String> hashMap){
    	String filePath = FileUtils.getConfigAbsolutepath();
    	filePath = FileUtils.getFullName(filePath, config);
    	if (FileUtils.isFileExist(config)) {  
    		filePath = config;
        }
    	if (FileUtils.isFileExist(filePath)) {  
    		try {
    			FileInputStream inStream = new FileInputStream(filePath);
        		Properties prop = new Properties();
        		prop.load(inStream);
        		inStream.close();
        		System.out.println("===================��ȡ�����ļ��ɹ���======================"); 
        		Iterator<Entry<Object, Object>> iterator = prop.entrySet().iterator();
        		while(iterator.hasNext()){
        			Entry<Object, Object> entry = iterator.next();
        			
        			Iterator<Entry<String, String>> iterator2 = hashMap.entrySet().iterator();
        			while(iterator2.hasNext()){
        				Entry<String, String> entry2 = iterator2.next();
        				String entryvalue = StringL.toString(entry.getValue());
        				String entrykey2 = StringL.toString(entry2.getKey());
        				if(StringUtils.indexOf(entryvalue, entrykey2)>-1){
        					prop.setProperty(StringL.toString(entry.getKey()), StringL.replace(entryvalue,
        							StringL.toString(entry2.getKey()),entrykey2));
                			System.out.println("===================�滻"+entry.getKey()+" " + entry.getValue()+"�ɹ���======================"); 
        				}
        			}
        		}
        		FileOutputStream outStream = new FileOutputStream(filePath); 
        		prop.store(outStream, "Copyright (c) Allen"); 
        		System.out.println("===================�滻�����ļ��ɹ���======================"); 
        		outStream.close();
    		} catch (Exception e) {
    			throw new RuntimeException("��ȡ�����ļ��쳣",e);
    		}
        }else {  
            System.err.println("*** " + config + " �ļ�û���ҵ���*** " + filePath + " �ļ�û���ҵ����޷��滻"); 
        }  
    }
    
    /**�滻��ʼ���ļ�config������д��config
     * */
    public static Properties replaceConfig(String config ,HashMap<String, String> hashMap){
    	Properties prop = null;
    	
    	String filePath = FileUtils.getConfigAbsolutepath();
    	filePath = FileUtils.getFullName(filePath, config);
    	if (FileUtils.isFileExist(config)) {  
    		filePath = config;
        }
    	if (FileUtils.isFileExist(filePath)) {  
    		try {
    			FileInputStream inStream = new FileInputStream(filePath);
        		prop = new Properties();
        		prop.load(inStream);
        		inStream.close();
        		System.out.println("===================��ȡ�����ļ��ɹ���======================"); 
        		Iterator<Entry<Object, Object>> iterator = prop.entrySet().iterator();
        		while(iterator.hasNext()){
        			Entry<Object, Object> entry = iterator.next();
        			
        			Iterator<Entry<String, String>> iterator2 = hashMap.entrySet().iterator();
        			while(iterator2.hasNext()){
        				Entry<String, String> entry2 = iterator2.next();
        				String entryvalue = StringL.toString(entry.getValue());
        				String entrykey2 = StringL.toString(entry2.getKey());
        				if(StringUtils.indexOf(entryvalue, entrykey2)>-1){
        					prop.setProperty(StringL.toString(entry.getKey()), StringL.replace(entryvalue,
        							StringL.toString(entry2.getKey()),entrykey2));
                			System.out.println("===================�滻"+entry.getKey()+" " + entry.getValue()+"�ɹ���======================"); 
        				}
        			}
        		}
    		} catch (Exception e) {
    			throw new RuntimeException("��ȡ�����ļ��쳣",e);
    		}
        }else {  
            System.err.println("*** " + config + " �ļ�û���ҵ���*** " + filePath + " �ļ�û���ҵ����޷��滻");
        }  
    	return prop;
    }
    
    /**
     * ʹ������������ָ���ļ�
     * @param message
     * @param type
     */
	private static void logPrint(String message,String type,Throwable t) {
		String logTime = StringL.getToday("-");
		String fileUrlPath = FileUtils.getConfigAbsolutepath();
		if(type.equals("error")) fileUrlPath = fileUrlPath+"/logs/"+logTime+"_error.log";
	    else fileUrlPath = fileUrlPath+"/logs/"+logTime+"_log.log";
	    PrintWriter out = null;
	    try {
	    	StackTraceElement[] threadste = Thread.currentThread().getStackTrace();
	    	message = StringL.getTodayNow() + "["+type+"] " + 
	    			threadste[threadste.length-1].toString() +" "+ (message==null?"":message);
	    	if(!FileUtils.isFileExist(fileUrlPath)){
	    		FileUtils.createFile(fileUrlPath);
	    	}
		    out = new PrintWriter(new FileOutputStream(fileUrlPath, true));
		    if(type.equals("error")) System.err.println(message);
		    else System.out.println(message);
		    if(t != null){
		    	StackTraceElement[] stackTraceElements = t.getStackTrace();
		    	if(type.equals("error")){
		    		System.err.println(t.toString());
		    		for (StackTraceElement ste : stackTraceElements) {
						System.err.println("\t" + ste);
					}
		    	}
			    else{
			    	System.out.println(t.toString());
			    	for (StackTraceElement ste : stackTraceElements) {
						System.out.println("\t" + ste);
					}
			    }
		    }
		    out.println(message);
		    if(t != null){
		    	StackTraceElement[] stackTraceElements = t.getStackTrace();
		    	out.println(t.toString());
			    for (StackTraceElement ste : stackTraceElements) {
			    	out.println("\t" + ste);
				}
		    }
		    out.close();
	    } catch (IOException e){
	    	e.printStackTrace();
	    }
	}
	/**info���*/
	public static void infoPrint(String message) {
		logPrint(message,"info",null);
	}
	/**debug���*/
	public static void debugPrint(String message) {
		logPrint(message,"debug",null);
	}
	/**error���*/
	public static void errorPrint(String message) {
		logPrint(message,"error",null);
	}
	/**info���*/
	public static void infoPrint(String message,Throwable t) {
		logPrint(message,"info",t);
	}
	/**debug���*/
	public static void debugPrint(String message,Throwable t) {
		logPrint(message,"debug",t);
	}
	/**error���*/
	public static void errorPrint(String message,Throwable t) {
		logPrint(message,"error",t);
	}
	
	public void logServletPrint(HttpServletRequest request,String message){
		String servletPath=request.getServletPath();
        String pathInfo=request.getPathInfo()==null?"":request.getPathInfo();
        String path=servletPath+pathInfo;
        System.out.println(StringL.getTodayNow() + "[log] " + path + message);
		request.getServletContext().log(path + message);
	}
	
	public static void main(String[] args) {
//		PropertyConfigurator.configure("E:/GitHub/lzs/longchenhan/WebRoot/WEB-INF/etc/log4j.properties"); 
//		Logger logger = Logger.getLogger(LogUtils.class); 
//		Log logger2 = LogFactory.getLog("");
//		logger.info("@@1!!23213");
//		logger2.debug("@@2!!4444");
		infoPrint("111",new Exception("213123"));
		System.out.println(Class.class.getResource("/"));
//		loadConfig("E:/GitHub/lzs/longchenhan/WebRoot/aa/log4j.properties");
//		HashMap<String,String> hm = new HashMap<String,String>();
//		hm.put("MyEclipse", "111");
//		Properties prop = replaceConfig("E:/GitHub/lzs/longchenhan/WebRoot/WEB-INF/etc/log4j.properties",hm);
//		System.err.println(prop.getProperty("log4j.appender.E.File"));
	}
}
