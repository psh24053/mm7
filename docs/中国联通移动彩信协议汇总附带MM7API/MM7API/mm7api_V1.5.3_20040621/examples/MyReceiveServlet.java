import javax.servlet.*;
import java.net.*;
import java.io.*;
import java.util.*;
import org.jdom.*;
import javax.xml.parsers.*;
import sun.misc.*;
import java.net.*;
import java.security.*;
import com.cmcc.mm7.vasp.service.*;
import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.conf.*;
import com.cmcc.mm7.vasp.common.*;

public class MyReceiveServlet extends MM7ReceiveServlet
{
  //处理到VASP的传送（deliver）多媒体消息
  public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq) {
    MM7DeliverRes res = new MM7DeliverRes();
    int i = 0;
    
    System.out.println("transactionid=" + mm7DeliverReq.getTransactionID());
    
/*    if(mm7DeliverReq.isToExist())
    {
    	List to = mm7DeliverReq.getTo();
    	for(i = 0;i < to.size();i ++)
    	{
    		System.out.println("to="+to.get(i));
    	}
    }

    if(mm7DeliverReq.isCcExist())
    {
     	List cc = mm7DeliverReq.getCc();
    	for(i = 0;i < cc.size();i ++)
    	{
    		System.out.println("cc="+cc.get(i));
    	}
   }

    if(mm7DeliverReq.isBccExist())
    {
     	List bcc = mm7DeliverReq.getBcc();
    	for(i = 0;i < bcc.size();i ++)
    	{
    		System.out.println("bcc="+bcc.get(i));
    	}
    }
    
    if(mm7DeliverReq.isLinkedIDExist())
    {
    	System.out.println("linkedid="+mm7DeliverReq.getLinkedID());
    }
    
    if(mm7DeliverReq.isMMSRelayServerIDExist())
    {
    	System.out.println("mmsrelayserverid="+mm7DeliverReq.getMMSRelayServerID());
    }
    
    if(mm7DeliverReq.isPriorityExist())
    {
    	System.out.println("priority="+mm7DeliverReq.getPriority());
    }
    
    if(mm7DeliverReq.isReplyChargingIDExist())
    {
    	System.out.println("replycharging="+mm7DeliverReq.getReplyChargingID());
    }
    
    if(mm7DeliverReq.isSenderExist())
    {
    	System.out.println("sender="+mm7DeliverReq.getSender());
    }
    
    if(mm7DeliverReq.isSubjectExist())
    {
    	try{
	    	System.out.println("subject="+mm7DeliverReq.getSubject());
    	}catch(Exception e)
    	{
    		System.err.println(e);
    	}
    }
    
    if(mm7DeliverReq.isTimeStampExist())
    {
    	System.out.println("timestamp="+mm7DeliverReq.getTimeStamp());
    }
    
	if(mm7DeliverReq.isContentExist())
	{
		System.out.println("exist");
	    MMContent parentContent = mm7DeliverReq.getContent();
	    if(parentContent.isMultipart())
	    {
	    	System.out.println("multipart");
		    List contentList = parentContent.getSubContents();
		    System.out.println("i="+contentList.size());
		    for(i = 0;i < contentList.size();i ++)
		    {
		    	MMContent mmContent = (MMContent)contentList.get(i);
		    	String contentID = mmContent.getContentID();
		    	if(contentID == null || contentID.length() == 0)
		    		contentID = "zxme" + i + ".";
		    	MMContentType mmContentType = mmContent.getContentType();
		    	System.out.println("contenttype="+mmContentType.getPrimaryType()+"/"+mmContentType.getSubType());
		    	if(mmContentType.getSubType().equalsIgnoreCase("jpeg"))
		    		contentID = contentID + "jpg";
		    	else if(mmContentType.getSubType().equalsIgnoreCase("gif"))
		    		contentID = contentID + "gif";
		    	else if(mmContentType.getSubType().equalsIgnoreCase("midi"))
		    		contentID = contentID + "mid";
		    	else if(mmContentType.getSubType().equalsIgnoreCase("png"))
		    		contentID = contentID + "png";
		    	else if(mmContentType.getPrimaryType().equalsIgnoreCase("text"))
		    		contentID = contentID + "txt";
		    		
		    	System.out.println("contentID="+contentID);
		    	byte content[] = mmContent.getContent();
		    	try
		    	{
			    	FileOutputStream fileStream = new FileOutputStream("c:\\temp\\"+contentID);
			    	fileStream.write(content);
			    	fileStream.close();
			    }
			    catch(IOException e)
			    {
			    	System.err.println(e);
			    }
		    }
		}
		else
		{
			System.out.println("singlepart");
	    	String contentID = parentContent.getContentID();
	    	System.out.println("contentID="+contentID);
	    	if(contentID == null || contentID.length() == 0)
	    		contentID = "zxme.";
	    	MMContentType mmContentType = parentContent.getContentType();
	    	if(mmContentType.getSubType().equalsIgnoreCase("jpeg"))
	    		contentID = contentID + "jpg";
	    	else if(mmContentType.getSubType().equalsIgnoreCase("gif"))
	    		contentID = contentID + "gif";
	    	else if(mmContentType.getSubType().equalsIgnoreCase("mid"))
	    		contentID = contentID + "mid";
	    	else if(mmContentType.getSubType().equalsIgnoreCase("png"))
	    		contentID = contentID + "png";
	    	else if(mmContentType.getPrimaryType().equalsIgnoreCase("txt"))
	    		contentID = contentID + "txt";

	    	byte content[] = parentContent.getContent();
	    	try
	    	{
		    	FileOutputStream fileStream = new FileOutputStream("c:\\temp\\"+contentID);
		    	fileStream.write(content);
		    	fileStream.close();
		    }
		    catch(IOException e)
		    {
		    	System.err.println(e);
		    }
		}
	}
	System.out.println("over");*/
    res.setTransactionID(mm7DeliverReq.getTransactionID());
    res.setStatusCode(1000);
    return res;
  }

  //处理到VASP的发送报告
  public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq) 
  {
    System.out.println("transactionid="+mm7DeliveryReportReq.getTransactionID());
    MM7DeliveryReportRes res = new MM7DeliveryReportRes();
    res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
    res.setStatusCode(1000);
    return res;
  }

  //处理到VASP的读后回复报告
  public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
  {
    System.out.println("transactionid="+mm7ReadReplyReq.getTransactionID());
    MM7ReadReplyRes res = new MM7ReadReplyRes();
    res.setTransactionID(mm7ReadReplyReq.getTransactionID());
    res.setStatusCode(1000);
    return res;
  }

  public MyReceiveServlet()
  {
	  Config = new MM7Config("E:\\kane\\jakarta-tomcat-5.0.19\\webapps\\servlets-examples\\config\\mm7Config.xml");
  }

}
