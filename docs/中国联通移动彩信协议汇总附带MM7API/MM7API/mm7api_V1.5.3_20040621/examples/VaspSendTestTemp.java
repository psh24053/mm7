import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.service.MM7Sender;
import org.apache.commons.lang.*;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import com.cmcc.mm7.vasp.common.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import javax.xml.soap.MimeHeader;
import java.text.*;
import com.cmcc.mm7.vasp.conf.*;

public class VaspSendTestTemp
{
	public static void main(String[] args)
	{

		final MM7Config mm7Config = new MM7Config("d:/mm7Config.xml");
		mm7Config.setConnConfigName("d:/ConnConfig.xml");

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			MM7SubmitReq submit = new MM7SubmitReq();
			submit.setTransactionID("1000000");
			//submit.addTo("13680000048");
			//submit.addTo("13980000035");
			//submit.addTo("13980000033");
			//submit.addTo("13980000051");
			//submit.addTo("13980000053");
			//submit.addTo("13980000058");
			submit.addTo("18684012650");
			submit.setVASID("10628973");
			submit.setVASPID("71565");
			submit.setServiceCode("31810112");
			submit.setSenderAddress("10628973"+"31810112");
			submit.setSubject("测试");
			submit.setChargedPartyID("13980000035");
			submit.setChargedParty((byte)4);
			submit.setDeliveryReport(true);
			//submit.setReplyCharging(true);
			//submit.setReplyChargingSize(100);
			//submit.setReplyDeadline(sdf.parse("2005-03-23T15:35:00"));
			//submit.setMessageClass("Personal");
			//submit.setReadReply(true);
			//submit.setEarliestDeliveryTime(sdf.parse("2005-03-21T21:35:00"));
			//submit.setExpiryDate(sdf.parse("2004-03-11T17:00:00"));
			//submit.setPriority((byte)2);
			//submit.setMessageClass("Personal");
			//submit.setReplyDeadline(sdf.parse("2004-03-03T23:59:59"));

			MMContent content = new MMContent();
			content.setContentType(MMConstants.ContentType.MULTIPART_RELATED);
			content.setContentID("mm7Test");
/*			MMContent sub1 = MMContent.createFromFile("./ccc.jpg");
			sub1.setContentID("1.jpg");
			sub1.setContentType(MMConstants.ContentType.JPEG);
			content.addSubContent(sub1);
			MMContent sub3 = MMContent.createFromFile("./bbb.mid");
			sub3.setContentID("1.mid");
			sub3.setContentType(MMConstants.ContentType.MIDI);
			content.addSubContent(sub3);*/
			String ss = "测试测试";
			byte[] bb = null;
			bb = ss.getBytes();
			InputStream input = new ByteArrayInputStream(bb);
			MMContent sub2 = MMContent.createFromStream(input);
			sub2.setContentID("2.txt");
                        //�������ó�sub2.setContentType("text/plain");Ҳ���������淽������
			sub2.setContentType(MMConstants.ContentType.TEXT);
			content.addSubContent(sub2);
/*			byte[] bb = null;
			bb = "��ѧ��".getBytes("UTF-8");
			MMContent sub2 = MMContent.createFromBytes(bb);
			sub2.setContentID("2.txt");
			sub2.setContentType(MMConstants.ContentType.TEXT);
			content.addSubContent(sub2);*/
			
/*			MMContent sub4 = MMContent.createFromFile("./aaa.gif");
			sub4.setContentID("1.gif");
			sub4.setContentType(MMConstants.ContentType.GIF);
			content.addSubContent(sub4);*/

			submit.setContent(content);
			
			MM7Sender mm7Sender = new MM7Sender(mm7Config);
			MM7RSRes rsRes;			
			rsRes = (MM7RSRes)mm7Sender.send(submit);
			if(rsRes instanceof MM7SubmitRes)
			{
			MM7SubmitRes submitRes = (MM7SubmitRes)rsRes;
			System.out.println(rsRes.getTransactionID());
			System.out.println("after!!submitRes.statuscode=" + rsRes.getStatusCode() + ";submitRes.statusText=" + rsRes.getStatusText());
			}else{
			  System.out.println("不正确消息！Res.statuscode="+rsRes.getStatusCode() + ";rsRes.statusText=" + rsRes.getStatusText());
			}
/*
			MM7CancelReq cancelReq = new MM7CancelReq();
			cancelReq.setTransactionID(submitRes.getTransactionID());
			cancelReq.setMessageID(submitRes.getMessageID());
			cancelReq.setVASID("4444");
			cancelReq.setVASPID("804444");
			cancelReq.setSenderAddress("4444");
			rsRes = mm7Sender.send(cancelReq);
			if(rsRes instanceof MM7CancelRes)
			{
			MM7CancelRes cancelRes = (MM7CancelRes)rsRes;
			System.out.println("-----------------------");
			System.out.println(cancelRes.toString());
			System.out.println("cancelRes!statuscode="+cancelRes.getStatusCode()+";statustext="+cancelRes.getStatusText());
			}else{
			  System.out.println("����ȷ��Ϣ��rsRes.statuscode="+rsRes.getStatusCode() + ";rsRes.statusText=" + rsRes.getStatusText());
			}
*/
/*
			MM7ReplaceReq replaceReq = new MM7ReplaceReq();
			replaceReq.setTransactionID(submitRes.getTransactionID());
			replaceReq.setMessageID(submitRes.getMessageID());
			replaceReq.setVASID("4444");
			replaceReq.setVASPID("804444");
			replaceReq.setServiceCode("123");
			content = new MMContent();
			content.setContentType(MMConstants.ContentType.MULTIPART_RELATED);
			content.setContentID("mm7Test");
			ss = "����һ��Replace MMS";
			bb = null;
			bb = ss.getBytes();
			input = new ByteArrayInputStream(bb);
			sub2 = MMContent.createFromStream(input);
			sub2.setContentID("2.txt");
			sub2.setContentType(MMConstants.ContentType.TEXT);
			content.addSubContent(sub2);
			replaceReq.setContent(content);
			rsRes = mm7Sender.send(replaceReq);
			if(rsRes instanceof MM7ReplaceRes)
			{
			MM7ReplaceRes replaceRes = (MM7ReplaceRes)rsRes;
			System.out.println("Replace!res.statuscode="+replaceRes.getStatusCode()+";res.statustext="+replaceRes.getStatusText());
			}else{
			  System.out.println("����ȷ��Ϣ��rsRes.statuscode="+rsRes.getStatusCode() + ";rsRes.statusText=" + rsRes.getStatusText());
			}
*/
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}