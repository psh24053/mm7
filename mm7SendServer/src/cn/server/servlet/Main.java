package cn.server.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;

import cn.server.Handler.ActionHandler;


public class Main extends HttpServlet {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ActionHandler handler = null;
	public Main() {
		handler = ActionHandler.getInstance();
	}
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String requestStr = readUtf8RequestContent(request);
		try {
			new JSONObject(requestStr);
		} catch (JSONException e1) {
			requestStr = URLDecoder.decode(requestStr, "utf-8");
		}
		String responsejson = handler.allHandler(requestStr, request, response);
		response.setContentType("text/x-json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(responsejson);
			response.getWriter().close();
		} catch (Exception e) {
			
		}
		
	}
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		this.doPost(request, response);
	}
	public void init() throws ServletException {
		String path = this.getServletConfig().getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"classes"+File.separator+"log4j.properties";
		System.out.println(path);
		PropertyConfigurator.configure(path);
	}
	
	public void destroy() {
		
	}
	public static String readUtf8RequestContent (HttpServletRequest request) {

		char content[] = new char[request.getContentLength()];

		try {
		request.setCharacterEncoding("utf8");
		BufferedReader in = request.getReader();
		in.read(content);
		in.close();
		} 
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
		}

		return new String(content);
		}

}
