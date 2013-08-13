package cn.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import cn.common.MD5Code;




public class FileUpload extends HttpServlet {
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();

		String idx = request.getParameter("idx");
		

		// 获得磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 获取文件需要上传到的路径
		String rootPath = getServletContext().getRealPath("/upload"); 

		// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
		// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		/**
		 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem
		 * 格式的 然后再将其真正写到 对应目录的硬盘上
		 */
		File uploadDir=new File(rootPath);
		if(!uploadDir.exists()){
			uploadDir.mkdirs();
		}
		factory.setRepository(uploadDir);
		// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024 * 1024);

		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// 可以上传多个文件
			List<FileItem> list = (List<FileItem>) upload.parseRequest((RequestContext) request);

			for (FileItem item : list) {
				// 获取表单的属性名字
				String fileName = item.getName();

				// 如果获取的 表单信息是普通的 文本 信息
				if (item.isFormField()) {
				}
				// 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
				else {

					
					String md5Name = MD5Code.getInstance().getCode(fileName+System.currentTimeMillis());
					String fiffux = fileName.substring(fileName.lastIndexOf("."));
					
					// 真正写到磁盘上
					// 它抛出的异常 用exception 捕捉
					File f = new File(rootPath+File.separatorChar, md5Name+fiffux);

					if (!f.exists()) {
						f.createNewFile();
					}
					item.write(f);// 第三方提供的
					

					JSONObject result=new JSONObject();
					JSONObject payload=new JSONObject();
					
					responseJavaScript(out, "parent.uploadImageComplete('"+idx+"','"+(md5Name+fiffux)+"')");

					out.close();
				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
  
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		this.doGet(request, response);
	}
	
	public void responseJavaScript(PrintWriter out, String js) {

		out.write("<script type='text/javascript'>");
		out.write(js);
		out.write("</script>");
		out.flush();

	}
}
