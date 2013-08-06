package cn.panshihao.mm7send;

import java.util.HashMap;
import java.util.List;

import com.huawei.smproxy.SGIPSMProxy;
import com.huawei.smproxy.comm.sgip.message.SGIPMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPSubmitMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPSubmitRepMessage;

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
	/**
	 * 执行发送请求，这可能是一个长时间的操作，成功发送返回true，发送失败返回false
	 * @return
	 */
	public boolean send(){
		
//		SGIPSubmitMessage sm = new SGIPSubmitMessage(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20)
		args = new HashMap<String, String>();
		args.put("login-name", "333");
		args.put("login-pass", "0555");
		
		SGIPSMProxy proxy = new SGIPSMProxy(args);
		
//		SGIPSubmitMessage submitMsg = 
//				new SGIPSubmitMessage(
//						"SP代码",   // sp代码
//					      "付费号码", //付费代码
//					      new String[]{"8618684012650"},
//					      "99999", // 企业代码
//					      "99999", //业务代码
//					      1,  //计费类型
//					      "1", //短信的收费值
//					      "1", // 赠送用户的话费
//					      int AgentFlag,  // 代收费标志
//					      int MorelatetoMTFlag,
//					      int Priority,
//					      Date ExpireTime,
//					      Date ScheduleTime,
//					      int ReportFlag,
//					      int TP_pid,
//					      int TP_udhi,
//					      int MessageCoding,
//					      int MessageType,
//					      int MessageLen,
//					      byte[] MessageContent,
//					      String reserve
//				  );
//				            SGIPSubmitRepMessage submitRepMsg = (SGIPSubmitRepMessage) proxy.send(submitMsg);
//				            // 可以增加处理响应消息submitRepMsg的代码
//				           
//				          //查询SMSGIPProxy与ISMG的TCP连接状态
//				          String stateDesc = proxy.getConnState();
//				          
//				         //退出
//				          proxy.close();             
		
		
		return true;
	}
	
	private HashMap<String, String> args;
	
	public static void main(String[] args) {
		SendSMC send = new SendSMC(null, null, null);
		
		send.send();
	}
	
}
