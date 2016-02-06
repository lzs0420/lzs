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
 * 关于日志的一些操作
 * @author Allen
 * @version 1.0 For All Projects 
 */
public class LogL {
	
	static{
		loadConfig("etc/LogL.properties");
	}
	
	/**单例模式，双重锁，实例*/
	private static LogL logl=null;
	/**Logger对象*/
	private static Logger logger;
	/**Log对象*/
	private static Log log;
	
	/**单例模式，双重锁，初始化方法*/
    private LogL(){
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
     * 返回Logger对象
     * @return Logger
     */
    public Logger getLogger(String logName) {
		return Logger.getLogger(logName);
	}

    /**
     * 返回Log对象
     * @return Log
     */
    public Log getLog() {
		return log;
	}
    
    /**
     * 返回Log对象
     * @return Log
     */
    public Log getLog(String logName) {
		return LogFactory.getLog(logName);
	}

	/**单例模式，双重锁，获取实例方法*/
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
    
    /**以config文件路径初始化log<br>不存在以默认配置初始化
     * */
    public static void loadConfig(String config){
    	//DOMConfigurator.configure("E:/study/log4j/log4j.xml");//加载.xml文件 
    	if (FileL.isFileExist(config)) {  
            System.out.println(StringL.getLogTodayNow()+" ******************** 使用config: " + config+"初始化日志设置信息   ********************");  
            PropertyConfigurator.configure(config);  
        }else{
        	String filePath = FileL.getConfigAbsolutepath();
        	filePath = FileL.getFullName(filePath, config);
        	
        	if (FileL.isFileExist(filePath)) {  
        		System.out.println(StringL.getLogTodayNow()+" ******************** 使用configpath+config: " 
        				+ filePath+"初始化日志设置信息  ********************");  
	            PropertyConfigurator.configure(filePath);  
	        }else {  
	            System.err.println(StringL.getLogTodayNow()+" *************** " + config + " config文件没有找到，" + filePath 
	            		+ " configpath+config文件没有找到, 所以使用 BasicConfigurator初始化   ***************");  
	            BasicConfigurator.configure();  
	        }  
        }
    }
    
    /**替换初始化文件config，同时写入config
     * @param replacetype 0：替换整个value 1：替换相同字符串 默认1
     * @param iswrite 是否把更改写入文件
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
        		System.out.println(StringL.getLogTodayNow()+" ********************读取配置文件成功！********************"); 
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
    							+ entryvalue+"替换"+entrykey2+" " + entryvalue2+"成功！********************"); 
        				
        			}
        		}
        		if(iswrite){
        			FileOutputStream outStream = new FileOutputStream(filePath); 
            		prop.store(outStream, "Copyright (c) Allen\r"+StringL.getLogTodayNow()); 
            		System.out.println(StringL.getLogTodayNow()+" ********************替换配置文件成功！********************"); 
            		outStream.close();
        		}
    		} catch (Exception e) {
    			throw new RuntimeException("读取配置文件异常",e);
    		}
        }else {  
            System.err.println(StringL.getLogTodayNow()+" ******************** " + config + " config文件没有找到, " 
            		+ filePath + " configpath+config文件没有找到，无法替换"); 
        }  
    	return prop;
    }
    
    /**替换初始化文件config,同时load进log4J
     * */
    public static void replaceAndLoadConfig(String config ,HashMap<String, String> hashMap,int replacetype,boolean iswrite){
    	PropertyConfigurator.configure(replaceConfig(config, hashMap, replacetype, iswrite));
    	System.out.println(StringL.getLogTodayNow()+" 替换****************** 使用config: " + config+"替换并初始化日志设置信息   ********************");  
    }
    
    /**
     * 使用输出流输出到指定文件
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
