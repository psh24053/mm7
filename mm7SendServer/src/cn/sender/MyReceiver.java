package cn.sender;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.service.MM7Receiver;
import com.cmcc.mm7.vasp.service.MM7Sender;

public class MyReceiver extends MM7Receiver {
	
	// 定义一个MM7DeliverReq，以便得到彩信网关发过来的Deliver消息。（必备）
	public static MM7DeliverReq deliverReq = new MM7DeliverReq();
	
	public void sender(List<String> phoneNum,JSONArray contents)throws Exception{

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
		
//		submitReq.addTo("18581864897");
//		submitReq.addTo("18684012650");
		for (int i = 0; i < phoneNum.size(); i++) {
			submitReq.addTo(phoneNum.get(i));
		}
		
		submitReq.setDeliveryReport(false);

		
		for (int i = 1; i < contents.length()+1; i++) {
			MMContent content = new MMContent();
			JSONObject contentjson = null;
			content.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
			for (int j = 0; j < contents.length(); j++) {
				JSONObject temp = contents.getJSONObject(j);
				//根据短信内容权重进行遍历，确保顺序正确
				if(temp.getInt("sort") == i){
					contentjson = temp;
				}
			}
			//区分短信内容类型，1为文本，2为多媒体文件
			if(contentjson.getInt("Type")==1){
				MMContent text = MMContent.createFromString(contentjson.getString("Text"));
				text.setContentType(MMConstants.ContentType.TEXT); //一定要设置
				content.addSubContent(text);
			}else if (contentjson.getInt("Type")==2){
				
				String filepath = contentjson.getString("FilePath");
				MMContent fileContent = MMContent.createFromFile(filepath);
				String fileSuffix = filepath.substring(filepath.lastIndexOf(".")+1);
				if("gif".equals(fileSuffix.toLowerCase())){
					fileContent.setContentType(MMConstants.ContentType.GIF);
				}else if("png".equals(fileSuffix.toLowerCase())){
					fileContent.setContentType(MMConstants.ContentType.PNG);
				}
				fileContent.setContentID(new File(filepath).getName());
				content.addSubContent(fileContent);
			}
			submitReq.setContent(content);
		}
		
		MM7RSRes res = mm7Sender.send(submitReq);
		System.out.println("res.statuscode=" + res.getStatusCode() +
				";res.statusText=" + res.getStatusText());
		
		// 启动接收器
		receiver.start();
	}

}
