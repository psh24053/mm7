package cn.panshihao.mm7send;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import sun.misc.Compare;

import cn.server.bean.Content;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.service.MM7Sender;

/**
 * 发送 彩信专用类
 * @author Panshihao
 *
 */
public class SendMM7 {

	// 接收号码list
	private List<String> toList;
	// 内容List
	private List<Content> contentList;
	// 短信主题
	private String subject;
	
	/**
	 * 构造方法
	 * @param toList 接收号码List
	 * @param contentList 内容LIST
	 * @param subject 彩信主题
	 */
	public SendMM7(List<String> toList, List<Content> contentList, String subject){
		this.toList = toList;
		this.contentList = contentList;
		this.subject = subject;
	}
	/**
	 * 执行发送请求，这可能是一个长时间的操作，成功发送返回true，发送失败返回false
	 * @return
	 */
	public boolean send(){
		// 初始化VASP
		MM7Config mm7Config = new MM7Config(SendMM7.class.getResource("/mm7Config.xml").getPath());
		// 设置ConnConfig.xml文件的路径
		mm7Config.setConnConfigName(SendMM7.class.getResource("/ConnConfig.xml").getPath()); // 必备
		
		// 创建MM7消息发送接口
		MM7Sender mm7Sender = null;
		try {
			mm7Sender = new MM7Sender(mm7Config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MM7SubmitReq submitReq = new MM7SubmitReq();
		
		submitReq.setTransactionID("100001");
		submitReq.setVASPID("71565");
		submitReq.setVASID("10628973");
		submitReq.setServiceCode("3181011200");
		submitReq.setSenderAddress("10628973");
		
		// 设置接收号码
		submitReq.setTo(toList);
		// 测试
		submitReq.addTo("18684012650");
		
		// 目前不需要状态报告
		submitReq.setDeliveryReport(false);
		// 设置彩信主题
		submitReq.setSubject(subject);
		
		MMContent content = new MMContent();
		content.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		
		// 设置权重
		Collections.sort(contentList, new Comparator<Content>() {

			@Override
			public int compare(Content o1, Content o2) {
				// TODO Auto-generated method stub
				if(o1.getSort() > o2.getSort()){
					return 1;
				}else if(o1.getSort() < o2.getSort()){
					return -1;
				}
				
				return 0;
			}
		});
		
		
		
		// 解析内容
		for (int i = 0; i < contentList.size(); i++) {
			Content item = contentList.get(i);
			
			
			//区分短信内容类型，1为文本，2为多媒体文件
			if(item.getContentType() == 1){
				MMContent text = MMContent.createFromString(item.getContentByte());
				text.setContentType(MMConstants.ContentType.TEXT); //一定要设置
				text.setContentID((i+1)+".text");
				content.addSubContent(text);
			}else if (item.getContentType() == 2){
				
				String filepath = item.getContentByte();
				MMContent fileContent = MMContent.createFromFile(filepath);
				String fileSuffix = filepath.substring(filepath.lastIndexOf(".")+1);
				if("gif".equals(fileSuffix.toLowerCase())){
					fileContent.setContentType(MMConstants.ContentType.GIF);
					fileContent.setContentID((i+1)+".gif");
				}else if("png".equals(fileSuffix.toLowerCase())){
					fileContent.setContentType(MMConstants.ContentType.PNG);
					fileContent.setContentID((i+1)+".png");
				}
				content.addSubContent(fileContent);
			}
		}
		submitReq.setContent(content);
		
		
		
		MM7RSRes res = mm7Sender.send(submitReq);
		
		if(res == null){
			return false;
		}
		
		System.out.println("res.statuscode=" + res.getStatusCode() +
				";res.statusText=" + res.getStatusText());
		
		
		return true;
	}
	
}
