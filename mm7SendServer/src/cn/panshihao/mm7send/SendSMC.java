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
import java.util.ArrayList;
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
 * 
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
	
	// 成功号码
	private List<String> successList;
	// 失败号码
	private List<String> failList;

	/**
	 * 构造方法
	 * 
	 * @param toList
	 *            接收号码List
	 * @param subject
	 *            短信主题
	 * @param content
	 *            短信内容
	 */
	public SendSMC(List<String> toList, String subject, String content) {
		this.toList = toList;
		this.subject = subject;
		this.content = content;
		this.successList = new ArrayList<String>();
		this.failList = new ArrayList<String>();
		
	}

	/**
	 * 执行发送命令
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public boolean Submit() {
		SGIP_Command command = new SGIP_Command(SPINFO.SGIP_NODEID);
		SGIP_Command temp = null;
		OutputStream out = null;
		InputStream input = null;
		Socket socket = null;
		int response = 0;

		try {
			socket = new Socket(SPINFO.SGIP_HOST, SPINFO.SGIP_PORT);
			out = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 首先指定bind操作
		Bind bind = new Bind(SPINFO.SGIP_NODEID);
		bind.SetLoginName(SPINFO.SGIP_NAME);
		bind.SetLoginPassword(SPINFO.SGIP_PASSWORD);
		bind.SetLoginType(1);

		System.out.println("SGIP Send -> SGIP_BIND");
		System.out.println("--------------------------------------------");
		response = bind.write(out);
		if (response != 0) {
			System.out.println("write error");
		}

		// 读取内容
		try {
			temp = command.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 判断返回内容是否是bind_resp
		if (temp.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
			System.out.println("SGIP Send -> Receiver -> SGIP_BIND_RESP");

			BindResp bindResp = (BindResp) temp;
			bindResp.readbody();

			if (bindResp.GetResult() == 0) {
				System.out.println("Bind Success");
			} else {
				System.out.println("Bind Fail, result -> "
						+ bindResp.GetResult());
				return false;
			}

			System.out.println("SGIP Send -> Receiver -> SEQNUMBER -> "
					+ temp.getSeqno_1() + "," + temp.getSeqno_2() + ","
					+ temp.getSeqno_3());
			
			System.out.println("--------------------------------------------");
		}
		byte[] byte_content = new byte[140];

		// 循环 submit
		for (int i = 0; i < toList.size(); i++) {
			String toNumber = toList.get(i);

			Submit submit = new Submit(SPINFO.SGIP_NODEID);// 节点号
			
			Date nowtime = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
			String tmpTime = dateFormat.format(nowtime);

			submit.setSPNumber(SPINFO.SGIP_SPNUMBER);// 接入号
			submit.setChargeNumber("000000000000000000000");

			submit.setServiceType("");// 服务类型
			submit.setCorpId(SPINFO.SGIP_CORPID);// 企业号
			submit.setFeeType(1);
			submit.setFeeValue("0");
			submit.setGivenValue("0");
			submit.setAgentFlag(0);
			submit.setMOrelatetoMTFlag(0);
			submit.setExpireTime(tmpTime);
			submit.setScheduleTime(tmpTime);// 不同的短信接口对时间的要求也不一样子。这里一定要
			submit.setPriority(0);
			submit.setReportFlag(1);
			submit.setTP_pid(0);
			submit.setTP_udhi(0);
			submit.setMessageType(0);
			submit.setBinContent(byte_content.length, byte_content);
			try {
				submit.setUserNumber("86" + toNumber);// 手机号不过一定要加86
				submit.setContent(content.length(), content);
			} catch (SGIP_Exception e) {
				e.printStackTrace();
			}
			response = submit.write(out);
			if (response != 0) {
				System.out.println("submit error");
			}

			try {
				temp = command.read(input);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 判断接收到的是否是submit resp
			if (temp.getCommandID() == SGIP_Command.ID_SGIP_SUBMIT_RESP) {
				System.out.println("SGIP Send -> Receiver -> SGIP_SUBMIT_RESP");

				SubmitResp submitResp = (SubmitResp) temp;
				submitResp.readbody();

				if (submitResp.getResult() == 0) {
					System.out.println(toNumber+" -> success");
					successList.add(toNumber);
				} else {
					System.out.println(toNumber+" -> fail");
					System.out.println(toNumber + " "+submitResp.getResult());
					failList.add(toNumber);
				}

				System.out.println("SGIP Send -> Receiver -> SEQNUMBER -> "
						+ temp.getSeqno_1() + "," + temp.getSeqno_2() + ","
						+ temp.getSeqno_3());

				System.out.println("--------------------------------------------");
			}

		}

		
		// submit执行完成  执行unbind
		
		Unbind unbind = new Unbind(SPINFO.SGIP_NODEID);
		unbind.write(out);
		
		
		try {
			temp = command.read(input);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if(temp.getCommandID() == SGIP_Command.ID_SGIP_UNBIND_RESP){
			System.out.println("unbind success");
		}else{
			System.out.println("unbind error");
		}
		
		
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;

	}

	
	
	public List<String> getToList() {
		return toList;
	}

	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getSuccessList() {
		return successList;
	}

	public void setSuccessList(List<String> successList) {
		this.successList = successList;
	}

	public List<String> getFailList() {
		return failList;
	}

	public void setFailList(List<String> failList) {
		this.failList = failList;
	}

	public static void main(String[] args) {
		
		List<String> tolist = new ArrayList<String>();
		tolist.add("18684012650");
		
		SendSMC send = new SendSMC(tolist, "subject", "contentasdasd");
		
		send.Submit();
	}

}
