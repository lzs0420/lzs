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
 * 关于日志的一些操作
 * @author Allen
 * @version 1.0
 */
public class LogUtils {
	
	/**单例模式，双重锁，实例*/
	private static LogUtils Logutils=null;
	/**Logger对象*/
	private static Logger logger;
	/**Log对象*/
	private static Log log;
	
	/**单例模式，双重锁，初始化方法*/
    private LogUtils(){
        //do something
    	logger = Logger.getLogger(""); 
		log = LogFactory.getLog("");
    }

    /**
     * 返回Logger对象
     * @return Logger
     */
    public Logger getLogger() {
		return logger;
	}

    /**
     * 返回Log对象
     * @return Log
     */
    public Log getLog() {
		return log;
	}

	/**单例模式，双重锁，获取实例方法*/
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
    
    /**以config文件路径初始化log<br>不存在以默认配置初始化
     * */
    public static void loadConfig(String config){
    	String filePath = FileUtils.getConfigAbsolutepath();
    	filePath = FileUtils.getFullName(filePath, config);
    	
    	//DOMConfigurator.configure("E:/study/log4j/log4j.xml");//加载.xml文件 
    	if (FileUtils.isFileExist(config)) {  
            System.out.println("使用: " + config+"初始化日志设置信息");  
            PropertyConfigurator.configure(config);  
        }else if (FileUtils.isFileExist(filePath)) {  
            System.out.println("使用: " + filePath+"初始化日志设置信息");  
            PropertyConfigurator.configure(filePath);  
        }else {  
            System.err.println("*** " + config + " 文件没有找到，*** " + filePath + " 文件没有找到, 所以使用 BasicConfigurator初始化");  
            BasicConfigurator.configure();  
        }  
    }
    
    /**替换初始化文件config，同时写入config
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
        		System.out.println("===================读取配置文件成功！======================"); 
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
                			System.out.println("===================替换"+entry.getKey()+" " + entry.getValue()+"成功！======================"); 
        				}
        			}
        		}
        		FileOutputStream outStream = new FileOutputStream(filePath); 
        		prop.store(outStream, "Copyright (c) Allen"); 
        		System.out.println("===================替换配置文件成功！======================"); 
        		outStream.close();
    		} catch (Exception e) {
    			throw new RuntimeException("读取配置文件异常",e);
    		}
        }else {  
            System.err.println("*** " + config + " 文件没有找到，*** " + filePath + " 文件没有找到，无法替换"); 
        }  
    }
    
    /**替换初始化文件config，但不写入config
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
        		System.out.println("===================读取配置文件成功！======================"); 
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
                			System.out.println("===================替换"+entry.getKey()+" " + entry.getValue()+"成功！======================"); 
        				}
        			}
        		}
    		} catch (Exception e) {
    			throw new RuntimeException("读取配置文件异常",e);
    		}
        }else {  
            System.err.println("*** " + config + " 文件没有找到，*** " + filePath + " 文件没有找到，无法替换");
        }  
    	return prop;
    }
    
    /**
     * 使用输出流输出到指定文件
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
	/**info输出*/
	public static void infoPrint(String message) {
		logPrint(message,"info",null);
	}
	/**debug输出*/
	public static void debugPrint(String message) {
		logPrint(message,"debug",null);
	}
	/**error输出*/
	public static void errorPrint(String message) {
		logPrint(message,"error",null);
	}
	/**info输出*/
	public static void infoPrint(String message,Throwable t) {
		logPrint(message,"info",t);
	}
	/**debug输出*/
	public static void debugPrint(String message,Throwable t) {
		logPrint(message,"debug",t);
	}
	/**error输出*/
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
