package mine.allen.util.lang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * ������־��һЩ����
 * @author Allen
 * @version 1.0 For All Projects 
 */
public class LogL {
	
	static{
		loadConfig("etc/LogL.properties");
	}
	
	/**����ģʽ��˫������ʵ��*/
	private static LogL logl=null;
	/**Logger����*/
	private static Logger logger;
	/**Log����*/
	private static Log log;
	
	/**����ģʽ��˫��������ʼ������*/
    private LogL(){
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
     * ����Logger����
     * @return Logger
     */
    public Logger getLogger(String logName) {
		return Logger.getLogger(logName);
	}

    /**
     * ����Log����
     * @return Log
     */
    public Log getLog() {
		return log;
	}
    
    /**
     * ����Log����
     * @return Log
     */
    public Log getLog(String logName) {
		return LogFactory.getLog(logName);
	}

	/**����ģʽ��˫��������ȡʵ������*/
    public static LogL getInstance(){
        if(logl==null){
            synchronized(LogL.class){
                if(null==logl){
                	logl=new LogL();
                }
            }
        }
        return logl;
    }
    
    /**��config�ļ�·����ʼ��log<br>��������Ĭ�����ó�ʼ��
     * */
    public static void loadConfig(String config){
    	//DOMConfigurator.configure("E:/study/log4j/log4j.xml");//����.xml�ļ� 
    	if (FileL.isFileExist(config)) {  
            System.out.println(StringL.getLogTodayNow()+" ******************** ʹ��config: " + config+"��ʼ����־������Ϣ   ********************");  
            PropertyConfigurator.configure(config);  
        }else{
        	String filePath = FileL.getConfigAbsolutepath();
        	filePath = FileL.getFullName(filePath, config);
        	
        	if (FileL.isFileExist(filePath)) {  
        		System.out.println(StringL.getLogTodayNow()+" ******************** ʹ��configpath+config: " 
        				+ filePath+"��ʼ����־������Ϣ  ********************");  
	            PropertyConfigurator.configure(filePath);  
	        }else {  
	            System.err.println(StringL.getLogTodayNow()+" *************** " + config + " config�ļ�û���ҵ���" + filePath 
	            		+ " configpath+config�ļ�û���ҵ�, ����ʹ�� BasicConfigurator��ʼ��   ***************");  
	            BasicConfigurator.configure();  
	        }  
        }
    }
    
    /**�滻��ʼ���ļ�config��ͬʱд��config
     * @param replacetype 0���滻����value 1���滻��ͬ�ַ��� Ĭ��1
     * @param iswrite �Ƿ�Ѹ���д���ļ�
     * */
    public static Properties replaceConfig(String config ,HashMap<String, String> hashMap ,int replacetype,boolean iswrite){
    	String filePath = "";
    	Properties prop = null;
    	if (FileL.isFileExist(config)) {  
    		filePath = config;
        }else{
        	filePath = FileL.getConfigAbsolutepath();
        	filePath = FileL.getFullName(filePath, config);
        }
    	if (FileL.isFileExist(filePath)) {  
    		try {
    			FileInputStream inStream = new FileInputStream(filePath);
        		prop = new Properties();
        		prop.load(inStream);
        		inStream.close();
        		System.out.println(StringL.getLogTodayNow()+" ********************��ȡ�����ļ��ɹ���********************"); 
        		Iterator<Entry<Object, Object>> iterator = prop.entrySet().iterator();
        		while(iterator.hasNext()){
        			Entry<Object, Object> entry = iterator.next();
        			
        			Iterator<Entry<String, String>> iterator2 = hashMap.entrySet().iterator();
        			while(iterator2.hasNext()){
        				Entry<String, String> entry2 = iterator2.next();
        				String entrykey = StringL.toString(entry.getKey());
        				String entryvalue = StringL.toString(entry.getValue());
        				String entrykey2 = StringL.toString(entry2.getKey());
        				String entryvalue2 = StringL.toString(entry2.getValue());
        				switch (replacetype){
        				case 0:
        					if(entrykey.equals(entrykey2)){
            					prop.setProperty(entrykey,entryvalue2);
            				}
        					break;
        				case 1:
        					if(StringL.indexOf(entryvalue, entrykey2)>-1){
            					prop.setProperty(entrykey, StringL.replace(entryvalue,entrykey2,entryvalue2));
            				}
        					break;
        				default:
        					if(StringL.indexOf(entryvalue, entrykey2)>-1){
        						prop.setProperty(entrykey, StringL.replace(entryvalue,entrykey2,entryvalue2));
            				}
        					break;
        				}
            			System.out.println(StringL.getLogTodayNow()+" ********************"+entrykey+" " 
    							+ entryvalue+"�滻"+entrykey2+" " + entryvalue2+"�ɹ���********************"); 
        				
        			}
        		}
        		if(iswrite){
        			FileOutputStream outStream = new FileOutputStream(filePath); 
            		prop.store(outStream, "Copyright (c) Allen\r"+StringL.getLogTodayNow()); 
            		System.out.println(StringL.getLogTodayNow()+" ********************�滻�����ļ��ɹ���********************"); 
            		outStream.close();
        		}
    		} catch (Exception e) {
    			throw new RuntimeException("��ȡ�����ļ��쳣",e);
    		}
        }else {  
            System.err.println(StringL.getLogTodayNow()+" ******************** " + config + " config�ļ�û���ҵ�, " 
            		+ filePath + " configpath+config�ļ�û���ҵ����޷��滻"); 
        }  
    	return prop;
    }
    
    /**�滻��ʼ���ļ�config,ͬʱload��log4J
     * */
    public static void replaceAndLoadConfig(String config ,HashMap<String, String> hashMap,int replacetype,boolean iswrite){
    	PropertyConfigurator.configure(replaceConfig(config, hashMap, replacetype, iswrite));
    	System.out.println(StringL.getLogTodayNow()+" �滻****************** ʹ��config: " + config+"�滻����ʼ����־������Ϣ   ********************");  
    }
    
    /**
     * ʹ������������ָ���ļ�
     * @param message
     * @param type
     */
	private static void logPrint(String message,String type,Throwable t) {
		String logTime = StringL.getToday("-");
		String fileUrlPath = FileL.getConfigAbsolutepath();
		if(type.equals("error")) fileUrlPath = fileUrlPath+"/logs/"+logTime+"_error.log";
	    else fileUrlPath = fileUrlPath+"/logs/"+logTime+"_log.log";
	    PrintWriter out = null;
	    try {
	    	StackTraceElement[] threadste = Thread.currentThread().getStackTrace();
	    	message = StringL.getTodayNow() + "["+type+"] " + 
	    			threadste[threadste.length-1].toString() +" "+ (message==null?"":message);
	    	if(!FileL.isFileExist(fileUrlPath)){
	    		FileL.createFile(fileUrlPath);
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
	
	
	public static void main(String[] args) {
//		PropertyConfigurator.configure("E:/GitHub/lzs/longchenhan/WebRoot/WEB-INF/etc/log4j.properties"); 
		
//		LogL.getInstance().getLog().debug("11");
//		LogL.getInstance().getLog().info("22");
//		infoPrint("111",new Exception("213123"));
//		loadConfig("E:/GitHub/lzs/longchenhan/WebRoot/aa/log4j.properties");
//		HashMap<String,String> hm = new HashMap<String,String>();
//		hm.put("MyEclipse", "111");
//		Properties prop = replaceConfig("E:/GitHub/lzs/longchenhan/WebRoot/WEB-INF/etc/log4j.properties",hm);
//		System.err.println(prop.getProperty("log4j.appender.E.File"));
	}
}
