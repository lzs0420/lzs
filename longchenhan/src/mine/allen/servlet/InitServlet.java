package mine.allen.servlet;  

import java.io.File;  
import java.io.IOException;  
import javax.servlet.ServletConfig;  
import javax.servlet.ServletContext;  
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import org.apache.log4j.BasicConfigurator;  
import org.apache.log4j.PropertyConfigurator;  

/** 
 * Servlet implementation class Log4JInitServlet 
 */  

public class InitServlet extends HttpServlet {  
    private static final long serialVersionUID = 1L;  

    /** 
     * @see HttpServlet#HttpServlet() 
     */  
    public InitServlet() {  
        super();
    }  

    /** 
     * @see Servlet#init(ServletConfig) 
     */  
    public void init(ServletConfig config) throws ServletException {  
    	/*String web_inf = getServletContext().getRealPath("/WEB-INF/"); //��ȡ��WEB-INF �ĵ�ַ
    	//classes����������ļ�
    	Thread.currentThread().getContextClassLoader().getResource("/").getPath();  */
    	
    	
        System.out.println("InitServlet ���ڳ�ʼ�� log4j��־������Ϣ");  
        String log4jLocation = config.getInitParameter("log4j-properties-location");  

        ServletContext sc = config.getServletContext();  

        if (log4jLocation == null) {  
            System.err.println("*** û�� log4j-properties-location ��ʼ�����ļ�, ����ʹ�� BasicConfigurator��ʼ��");  
            BasicConfigurator.configure();  
        } else {  
            String webAppPath = sc.getRealPath("/");  
            String log4jProp = webAppPath + log4jLocation;  
            File yoMamaYesThisSaysYoMama = new File(log4jProp);  
            if (yoMamaYesThisSaysYoMama.exists()) {  
                System.out.println("ʹ��: " + log4jProp+"��ʼ����־������Ϣ");  
                PropertyConfigurator.configure(log4jProp);  
            } else {  
                System.err.println("*** " + log4jProp + " �ļ�û���ҵ��� ����ʹ�� BasicConfigurator��ʼ��");  
                BasicConfigurator.configure();  
            }  
        }  
        super.init(config);  
    }  

    /** 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    }  

    /** 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    }  

}