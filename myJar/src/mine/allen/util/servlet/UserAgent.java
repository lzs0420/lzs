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
 * ����û���һЩ����
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
	
	static Pattern patternLocation = Pattern.compile("<LOCATION>(.+{1,})</LOCATION>");    
    private static final String IPURL = "http://www.ip.cn/index.php?ip=";    
    private static final String IDURL = "http://qq.ip138.com/idsearch/index.asp?action=idcard&B1=%B2%E9+%D1%AF&userid=";    
    private static final String MOBILEURL = "http://www.ip.cn/db.php?num=";    
	  
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
        String sex = address.indexOf("<td class=\"tdc2\">��</td>")>0?"��":"Ů";    

        String birthday = address.substring(address.indexOf("�������ڣ�</td")+27);   
        address = address.substring(address.indexOf("��&nbsp;֤&nbsp;�أ�") + 38);     
        birthday = birthday.substring(0,4)+"��"+birthday.substring(5,7)+"��"+birthday.substring(8,10)+"��";    
        return "��ַ��"+address.substring(0, address    
                .indexOf("<br"))+" �Ա�"+sex+" ����:"+birthday;    
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
        return "�ú��������Ϊ��"+address.substring(0, address    
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
        //�ر�    
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

