package mine.allen.util.common;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ����Ƿ�Ϊ�ƶ����豸����
 * 
 * @author  	: allen
 * @Version 	: 1.00
 * @Date    	: 2016-1-21 ����09:55:19 
 */
public class UserAgent {
	
	// \b �ǵ��ʱ߽�(���ŵ�����(��ĸ�ַ� �� ����ĸ�ַ�) ֮����߼��ϵļ��),  
	// �ַ����ڱ���ʱ�ᱻת��һ��,������ "\\b"  
	// \B �ǵ����ڲ��߼����(���ŵ�������ĸ�ַ�֮����߼��ϵļ��)  
	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"  
	        +"|windows (phone|ce)|blackberry"  
	        +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"  
	        +"|laystation portable)|nokia|fennec|htc[-_]"  
	        +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";  
	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"  
	        +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";  
	
	//�ƶ��豸����ƥ�䣺�ֻ��ˡ�ƽ��
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);  
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);  
	  
	/**
	 * ����Ƿ����ƶ��豸����
	 * 
	 * @Title: check
	 * @Date : 2016-1-21 ����09:55:19 
	 * @param userAgent �������ʶ
	 * @return true:�ƶ��豸���룬false:pc�˽���
	 */
	public static boolean check(String userAgent){  
	    if(null == userAgent){  
	        userAgent = "";  
	    }  
	    // ƥ��  
	    Matcher matcherPhone = phonePat.matcher(userAgent);  
	    Matcher matcherTable = tablePat.matcher(userAgent);  
	    if(matcherPhone.find() || matcherTable.find()){  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}
	
	/** 
	 * �����ʷ�ʽ�Ƿ�Ϊ�ƶ��� 
	 *  
	 * @Title: checkUserAgent 
	 * @Date : 2016-1-21 ����09:55:19 
	 * @param request 
	 * @param response
	 * @throws IOException  
	 */  
	public boolean checkUserAgent(HttpServletRequest request,HttpServletResponse response) throws IOException{  
	    boolean isFromMobile=false;  
	      
	    HttpSession session= request.getSession();  
	   //����Ƿ��Ѿ���¼���ʷ�ʽ���ƶ��˻�pc�ˣ�  
	    if(null==session.getAttribute("ua")){  
	        try{  
	            //��ȡua�������ж��Ƿ�Ϊ�ƶ��˷���  
	            String userAgent = getUserAgent(request, response);
	            isFromMobile=UserAgent.check(userAgent);  
	            //�ж��Ƿ�Ϊ�ƶ��˷���  
	            if(isFromMobile){  
	                System.out.println("�ƶ��˷���");  
	                session.setAttribute("ua","mobile");  
	            } else {  
	                System.out.println("pc�˷���");  
	                session.setAttribute("ua","pc");  
	            }  
	        }catch(Exception e){}  
	    }else{  
	        isFromMobile=session.getAttribute("ua").equals("mobile");  
	    }  
	      
	    return isFromMobile;  
	} 
	
	/** 
	 * ��ȡ���ʷ�ʽ
	 *  
	 * @Title: getUserAgent 
	 * @Date: 2016-1-21 ����09:55:19 
	 * @param request 
	 * @param response
	 * @throws IOException  
	 */  
	public String getUserAgent(HttpServletRequest request,HttpServletResponse response) throws IOException{  
	    String userAgent = "";
	    try{  
            //��ȡua
	    	userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();    
            if(null == userAgent){    
                userAgent = "";    
            }  
        }catch(Exception e){
        	e.printStackTrace();
        }  
	      
	    return userAgent;  
	} 
}

