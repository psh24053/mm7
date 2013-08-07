package cn.panshihao.mm7send;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import spApi.Bind;
import spApi.BindResp;
import spApi.Deliver;
import spApi.DeliverResp;
import spApi.Report;
import spApi.ReportResp;
import spApi.SGIP_Command;
import spApi.SGIP_Exception;
import spApi.Submit;
import spApi.SubmitResp;
import spApi.Unbind;
import spApi.UnbindResp;


import cn.server.bean.Content;

/**
 * 发送 短信专用类
 * @author Panshihao
 *
 */
public class SendSMC {

	// 接收号码list
	private List<String> toList;
	// 短信主题
	private String subject;
	// 短信内容
	private String content;
	
	/**
	 * 构造方法
	 * @param toList 接收号码List
	 * @param subject 短信主题
	 * @param content 短信内容
	 */
	public SendSMC(List<String> toList, String subject, String content){
		this.toList = toList;
		this.subject = subject;
		this.content = content;
	}
	
	public void send(){
		
	}
	
	public static void main(String[] args) {
		SendSMC send = new SendSMC(null, null, null);
		
		
	}
	
}
