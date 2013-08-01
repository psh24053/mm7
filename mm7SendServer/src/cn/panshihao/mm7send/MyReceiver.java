package cn.panshihao.mm7send;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.service.MM7Receiver;
import com.cmcc.mm7.vasp.service.MM7Sender;

public class MyReceiver extends MM7Receiver {

	// 定义一个MM7DeliverReq，以便得到彩信网关发过来的Deliver消息。（必备）
	public static MM7DeliverReq deliverReq = new MM7DeliverReq();

	// Main方法
	public static void main(String[] args) throws Exception {
		
		// 初始化VASP
		MM7Config mm7Config = new MM7Config(MyReceiver.class.getClassLoader().getResource("mm7Config.xml").getPath());
//		// 设置ConnConfig.xml文件的路径
		mm7Config.setConnConfigName(MyReceiver.class.getClassLoader().getResource("ConnConfig.xml").getPath()); // 必备
		// 构造MyReceiver
		MyReceiver receiver = new MyReceiver();
		receiver.setConfig(mm7Config); // 必备
		// 创建MM7消息发送接口
		MM7Sender mm7Sender = new MM7Sender(mm7Config);
		MM7SubmitReq submitReq = new MM7SubmitReq();
		
		submitReq.setTransactionID("100001");
		submitReq.setVASPID("71565");
		submitReq.setVASID("10628973");
		submitReq.setServiceCode("3181011200");
		submitReq.setSenderAddress("10628973");
		
		submitReq.addTo("18581864897");
		submitReq.addTo("18684012650");
		
		submitReq.setDeliveryReport(false);
//		submitReq.setSubject("测试");
		
		MMContent content = new MMContent();
		
		content.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		
		MMContent text = MMContent.createFromString("西瓜哥超神了...");
		text.setContentType(MMConstants.ContentType. TEXT); //一定要设置
		text.setContentID("1.txt");
		content.addSubContent(text);
		
		MMContent img= MMContent.createFromFile("e:\\logo.png");
		img.setContentType(MMConstants.ContentType.PNG);
		img.setContentID("logo.png");
		content.addSubContent(img);
		
		submitReq.setContent(content);
		
		MM7RSRes res = mm7Sender.send(submitReq);
		System.out.println("res.statuscode=" + res.getStatusCode() +
				";res.statusText=" + res.getStatusText());
		
		// 启动接收器
//		receiver.start();
	}

	public MM7VASPRes doDeliver(MM7DeliverReq request) {
		/*
		 * 接收从彩信网关发过来的传送消息，以便取出其中的部分值构造提交消息或得到一些有用 　　信息，如彩信网关的标识符等
		 */
		deliverReq = request;
		System.out.println("收到手机" + request.getSender() + "提交的消息，标题为："
				+ request.getSubject());
		// 企业需要进行一些处理，例如构建MM7DeliverRes消息，设置ServiceCode或StatusCode
		MM7DeliverRes mm7DeliverRes = new MM7DeliverRes();
		mm7DeliverRes.setServiceCode("服务代码"); // 设置ServiceCode，可选
		mm7DeliverRes.setStatusCode(MMConstants.RequestStatus.SUCCESS);/*
																		 * 设置请求完成状态，
																		 * 必备，
																		 * 以便表明企业已经接收到传送消息
																		 * 。
																		 * 一般设1000
																		 * 。
																		 */
		mm7DeliverRes.setStatusText("所用状态文本说明"); /*
												 * 设置所用状态的文本说明， 　　应限定请求状态，可选
												 */
		// 返回给MM7 API，以便API将MM7DeliverRes返回给彩信网关。
		return (MM7VASPRes) mm7DeliverRes;
	}

}
