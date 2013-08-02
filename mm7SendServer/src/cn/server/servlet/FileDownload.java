package cn.server.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

	public class FileDownload extends HttpServlet {
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
	
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			//PrintWriter out = response.getWriter();
			//response.reset();
			
			String filename=request.getParameter("filename"); 
			System.out.println("filename  "+filename); 
			/**通过response.setHeader("content-Type", "application/x-msdownload")这个头, 
			 * Web服务器需要告诉浏览器其所输出的内容的类型不是普通的文本文件或 HTML 文件，而是一个要保存到本地的下载文件
			 */
			response.setContentType("application/x-msdownload"); 
			/** 该报头指定了接收程序处理数据内容的方式,在HTTP应用中只有attachment 是标准方式， 
			 * attachment 表示要求用户干预。
			 * 在 attachment 后面还可以指定 filename 参数，该参数是服务器建议浏览器将实体内容保存到文件中的文件名称。 
			 * 在设置 Content-Dispostion 之前一定要指定 Content-Type.
			 */
			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(filename,"utf-8")); 
			//获取下载文件的真实路径 
			String realPath=this.getServletContext().getRealPath("/upload"); 
			filename=realPath+"/"+filename; 
			//创建文件输入流 
			FileInputStream fis=new FileInputStream(filename); 
			//创建缓冲输入流
			BufferedInputStream bis=new BufferedInputStream(fis); 
			//获取响应的输出流 
			OutputStream  os=response.getOutputStream(); 
			//创建缓冲输出流 
			BufferedOutputStream bos=new BufferedOutputStream(os); 
			//把输入流的数据写入到输出流 
			byte[] b=new byte[1024]; 
			int len=0; 
			while((len=bis.read(b))!=-1){ 
				bos.write(b, 0, len); 
			}
			bos.close();  
			bis.close(); 
		}
	
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			//PrintWriter out = response.getWriter();
			this.doGet(request, response);
		}
}
