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
    	/*String web_inf = getServletContext().getRealPath("/WEB-INF/"); //获取到WEB-INF 的地址
    	//classes下面的配置文件
    	Thread.currentThread().getContextClassLoader().getResource("/").getPath();  */
    	
    	
        System.out.println("InitServlet 正在初始化 log4j日志设置信息");  
        String log4jLocation = config.getInitParameter("log4j-properties-location");  

        ServletContext sc = config.getServletContext();  

        if (log4jLocation == null) {  
            System.err.println("*** 没有 log4j-properties-location 初始化的文件, 所以使用 BasicConfigurator初始化");  
            BasicConfigurator.configure();  
        } else {  
            String webAppPath = sc.getRealPath("/");  
            String log4jProp = webAppPath + log4jLocation;  
            File yoMamaYesThisSaysYoMama = new File(log4jProp);  
            if (yoMamaYesThisSaysYoMama.exists()) {  
                System.out.println("使用: " + log4jProp+"初始化日志设置信息");  
                PropertyConfigurator.configure(log4jProp);  
            } else {  
                System.err.println("*** " + log4jProp + " 文件没有找到， 所以使用 BasicConfigurator初始化");  
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
