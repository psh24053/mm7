package cn.server.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;


import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.service.MM7ReceiveServlet;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;

public class MM7Servlet extends MM7ReceiveServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MM7VASPRes doDeliver(MM7DeliverReq request){
		return null;
	}
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq) {
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		return null;
	}	
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq) {
		 MM7ReadReplyRes res = new MM7ReadReplyRes();
		 return res;
	}
	public void init( ServletConfig servletConfig ) throws ServletException
    {
		// 初始化VASP
//		MM7Config mm7Config = new MM7Config(MyReceiver.class.getClassLoader().getResource("/mm7Config.xml").getPath());
////		// 设置ConnConfig.xml文件的路径
//		mm7Config.setConnConfigName(MyReceiver.class.getClassLoader().getResource("/ConnConfig.xml").getPath()); // 必备

		
//		 Config = mm7Config;
        //MM7Config mm7Config = new MM7Config("F: \\mm7Config.xml");
    }


}
