package mine.allen.util;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyServletFilter implements Filter {

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
	
	}

	@Override
	public void doFilter(ServletRequest paramServletRequest,
			ServletResponse paramServletResponse, FilterChain paramFilterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)paramServletRequest;
		HttpServletResponse response = (HttpServletResponse)paramServletResponse;
//		String path = request.getServletPath();
//		if(path.startsWith("/error")&&paramServletRequest.getDispatcherType()!=DispatcherType.REQUEST
//				||path.startsWith("/MyServletJsp")&&paramServletRequest.getDispatcherType()!=DispatcherType.REQUEST){
//			paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
//		}else{
			request.getRequestDispatcher("/error/403.jsp").forward(request, response);
	}

	@Override
	public void destroy() {

	}

}
