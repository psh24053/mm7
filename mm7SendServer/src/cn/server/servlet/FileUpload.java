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
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;



public class FileUpload extends HttpServlet {
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			//判断上传的类型是否是 enctype="multipart/form-data"  true是  false表示不是 
			boolean flag=ServletFileUpload.isMultipartContent(request);
			if(flag){
				/**
				 * 创建生产FileItem的DiskFileItemFactory工厂的实例 
				 * 设置内存缓冲区的大小 
				 * 设置文件存放的临时路径 
				 */
				DiskFileItemFactory facoty = new DiskFileItemFactory();
				//设置内存缓冲区的大小 
				int sizeThreshold=3*1024*1024;
				facoty.setSizeThreshold(sizeThreshold);
				//设置文件存放的临时路径 
				facoty.setRepository(new File("D:/tem"));
				//创建ServletFileUpload实例,工厂作为参数 
				ServletFileUpload upload = new ServletFileUpload(facoty);
				//设置上传文件的大小,如果上传文件大于设置的大小，则要手动抛出SizeLimitExceededException 异常 
				long sizeMax = 83*1024*1024;
				upload.setSizeMax(sizeMax);
				
				//解析request请求,返回集合[集合中存放FileItem] 
				List<FileItem> items = upload.parseRequest(request);
				Iterator<FileItem> it = items.iterator();
				while(it.hasNext()){
					FileItem item = it.next();
					
					//如果是普通的表单域 
					if(item.isFormField()){
						//普通的表单域name属性的值
						String fieldName = item.getFieldName();
						//获得文本域中的内容时要注意转码，中文会出现中文乱码                 
						// String value=new String(item.getString().getBytes("utf-8");
						//普通的表单域value属性的值
						String filedValue = item.getString();
						System.out.println(fieldName+"   "+filedValue);
					}else{
						//如果是文件域  <input type="file"
						//文件域name属性的值 
						String fieldName = item.getFieldName();
						//文件域value属性的值 
						String fileName = item.getName();
						//上传文件的类型 
						String contentType = item.getContentType();
						//true:来自内存,false:来自内存和硬盘 
						boolean isInMemory = item.isInMemory();
						//上传文件的字节数
						long size = item.getSize();
						System.out.println(fieldName +"  "+fileName +"  "+contentType+"    "+isInMemory+"    "+size);
						
						//获取文件的后缀名(如果没有后缀名lastIndexOf(".")=-1 )
						if(fileName.lastIndexOf(".")!=-1){
							String ext = fileName.substring(fileName.lastIndexOf(".")+1);
							//设置允许上传的文件格式
							String allowExt[]={"jpg","zip","doc","jpeg","png","gif","html","txt","xlsx","docx"}; 
							//检测扩展名是否在数组中
							List<String> list = Arrays.asList(allowExt);
							if(!list.contains(ext)){
								throw new RuntimeException("您上传的文件扩展名不允许");
							}
						}else{
							throw new RuntimeException("您上传的文件没有扩展名,不允许上传"); 
						}
						//获取保存上传文件的路径 
						String realpath=this.getServletContext().getRealPath("/upload");
						System.out.println(realpath);
						//方法一获取文件名:
						//fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
						//方法二获取文件名:
						File file=new File(fileName); 
						fileName=file.getName(); 
						System.out.println("截取后的文件名  "+fileName);
						//保存上传文件
						item.write(new File(realpath+File.separator+fileName));
					}
				}
			}
		}catch(SizeLimitExceededException e){
			e.printStackTrace();
			throw new RuntimeException("上传文件大小不能超过3M");
		}catch(FileUploadException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
  
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		this.doGet(request, response);
	}
}
