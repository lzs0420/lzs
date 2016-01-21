package mine.allen.util.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

public class LogUtils {

	public static void logPrint(String message) {
	    PrintWriter out = null;
	    try {
		    out = new PrintWriter(new FileOutputStream("d:log.txt", true));
		    out.println(message);
		    out.close();
	    } catch (FileNotFoundException e){
	    	e.printStackTrace();
	    }
	}
	
	public void logServlet(HttpServletRequest request,String message){
		 request.getServletContext().log(message);
	}
}
