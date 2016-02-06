package mine.allen.util.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 检测用户的一些方法
 * @author  	: allen
 * @Version 	: 1.00
 * @Date    	: 2016-1-21 上午09:55:19 
 */
public class UserAgent {
	
	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),  
	// 字符串在编译时会被转码一次,所以是 "\\b"  
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)  
	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"  
	        +"|windows (phone|ce)|blackberry"  
	        +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"  
	        +"|laystation portable)|nokia|fennec|htc[-_]"  
	        +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";  
	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"  
	        +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";  
	
	//移动设备正则匹配：手机端、平板
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);  
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);  
	
	static Pattern patternLocation = Pattern.compile("<LOCATION>(.+{1,})</LOCATION>");    
    private static final String IPURL = "http://www.ip.cn/index.php?ip=";    
    private static final String IDURL = "http://qq.ip138.com/idsearch/index.asp?action=idcard&B1=%B2%E9+%D1%AF&userid=";    
    private static final String MOBILEURL = "http://www.ip.cn/db.php?num=";    
	  
	/**
	 * 检测是否是移动设备访问
	 * 
	 * @Title: check
	 * @Date : 2016-1-21 上午09:55:19 
	 * @param userAgent 浏览器标识
	 * @return true:移动设备接入，false:pc端接入
	 */
	public static boolean check(String userAgent){  
	    if(null == userAgent){  
	        userAgent = "";  
	    }  
	    // 匹配  
	    Matcher matcherPhone = phonePat.matcher(userAgent);  
	    Matcher matcherTable = tablePat.matcher(userAgent);  
	    if(matcherPhone.find() || matcherTable.find()){  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}
	
	/** 
	 * 检查访问方式是否为移动端 
	 *  
	 * @Title: checkUserAgent 
	 * @Date : 2016-1-21 上午09:55:19 
	 * @param request 
	 * @param response
	 * @throws IOException  
	 */  
	public boolean checkUserAgent(HttpServletRequest request,HttpServletResponse response) throws IOException{  
	    boolean isFromMobile=false;  
	      
	    HttpSession session= request.getSession();  
	   //检查是否已经记录访问方式（移动端或pc端）  
	    if(null==session.getAttribute("ua")){  
	        try{  
	            //获取ua，用来判断是否为移动端访问  
	            String userAgent = getUserAgent(request, response);
	            isFromMobile=UserAgent.check(userAgent);  
	            //判断是否为移动端访问  
	            if(isFromMobile){  
	                System.out.println("移动端访问");  
	                session.setAttribute("ua","mobile");  
	            } else {  
	                System.out.println("pc端访问");  
	                session.setAttribute("ua","pc");  
	            }  
	        }catch(Exception e){}  
	    }else{  
	        isFromMobile=session.getAttribute("ua").equals("mobile");  
	    }  
	      
	    return isFromMobile;  
	} 
	
	/** 
	 * 获取访问方式
	 *  
	 * @Title: getUserAgent 
	 * @Date: 2016-1-21 上午09:55:19 
	 * @param request 
	 * @param response
	 * @throws IOException  
	 */  
	public String getUserAgent(HttpServletRequest request,HttpServletResponse response) throws IOException{  
	    String userAgent = "";
	    try{  
            //获取ua
	    	userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();    
            if(null == userAgent){    
                userAgent = "";    
            }  
        }catch(Exception e){
        	e.printStackTrace();
        }  
	      
	    return userAgent;  
	} 
	
	private static String getLocationByIP(String ip) {    
        String address = "";    
        try {    
    
            URL url = new URL(IPURL + ip);    
            address = search(url);    
    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
        address = address.substring(address.indexOf("</code") + 13);    
        return address.substring(0, address    
                .indexOf("</p"));    
    }    
        
    private static String getLocationById(String id) {    
        String address = "";    
        try {    
    
            URL url = new URL(IDURL + id);    
            address = search(url,"gb2312");    
    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
        String sex = address.indexOf("<td class=\"tdc2\">男</td>")>0?"男":"女";    

        String birthday = address.substring(address.indexOf("出生日期：</td")+27);   
        address = address.substring(address.indexOf("发&nbsp;证&nbsp;地：") + 38);     
        birthday = birthday.substring(0,4)+"年"+birthday.substring(5,7)+"月"+birthday.substring(8,10)+"日";    
        return "地址："+address.substring(0, address    
                .indexOf("<br"))+" 性别："+sex+" 生日:"+birthday;    
    }    
        
    private static String getLocationByMobile(String mobile) {    
        String address = "";    
        try {    
    
            URL url = new URL(MOBILEURL + mobile);    
            address = search(url);    
    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
        address = address.substring(address.indexOf("</code") + 18);    
        return "该号码归属地为："+address.substring(0, address    
                .indexOf("<br"));    
    }    
    private static String search(URL url,String charset) throws IOException {    
    	String address;    
        HttpURLConnection connect = (HttpURLConnection) url    
                .openConnection();    
        InputStream is = connect.getInputStream();    
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
        byte[] buff = new byte[256];    
        int rc = 0;    
        while ((rc = is.read(buff, 0, 256)) > 0) {    
            outStream.write(buff, 0, rc);    
        }    
        byte[] b = outStream.toByteArray();    
        //关闭    
        outStream.close();    
        is.close();    
        connect.disconnect();    
        address = new String(b,charset);    
        return address;    
    }
    private static String search(URL url) throws IOException {    
    	return search(url,"UTF-8");
    }    
    
    public static void main(String[] args) {    
        System.out.println(getLocationByIP("221.226.177.158"));    
        System.out.println(getLocationByMobile("13007627782"));
        System.out.println(getLocationById("140726199403097622"));
    }    
	
}

