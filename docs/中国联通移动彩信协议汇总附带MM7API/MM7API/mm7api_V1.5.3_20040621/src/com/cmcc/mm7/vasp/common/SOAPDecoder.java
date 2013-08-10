/**File Name:SOAPDecoder.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-12
 * */

package com.cmcc.mm7.vasp.common;

import java.io.*;
import java.util.*;
import java.text.*;
import org.jdom.*;
import org.jdom.input.*;
import sun.misc.BASE64Decoder;
import com.cmcc.mm7.vasp.message.*;

public class SOAPDecoder
{
  private MM7RSReq mm7rsReq;
  private boolean bDecoder;
  private boolean bMessageExist;
  private OutputStream OutputMessage;
  private String MessageName;
  private String strEnvelope;
  private ByteArrayOutputStream SoapStream;

  /**默认构造方法*/
  public SOAPDecoder()
  {
    reset();
  }
  public void reset()
  {
    bDecoder = false;
    bMessageExist = false;
    OutputMessage = null;
    MessageName = "";
    mm7rsReq = new MM7RSReq();
    strEnvelope = "";
    SoapStream = new ByteArrayOutputStream();
  }
  private void setEnvelope(String env)
  {
    strEnvelope = env;
  }
  public String getEnvelope()
  {
    return strEnvelope;
  }
  private void setSoapStream(ByteArrayOutputStream soapstream)
  {
    SoapStream = soapstream;
  }
  public ByteArrayOutputStream getSoapStream()
  {
    return SoapStream;
  }

  /**传入的是OutputStream*/
  public void setMessage(OutputStream output)
  {
    OutputMessage = output;
    bMessageExist = true;
  }

  /**得到封装好的MM7RSReq消息*/
  public MM7RSReq getMessage()
  {
    if(bDecoder)
      return mm7rsReq;
    else
      return null;
  }

  public String getMessageName()
  {
    return MessageName;
  }

  /**进行消息的解码*/
  public void decodeMessage() throws SOAPDecodeException
  {
    if (!bMessageExist)
      throw new SOAPDecodeException("No Multimedia Messages get in the decoder!");
    try
    {
      if (OutputMessage == null) {
        bDecoder = false;
      }
      else {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) OutputMessage;
        ByteArrayOutputStream attachbaos = new ByteArrayOutputStream();
        //得到整个输出流
        String message = baos.toString();
        if (message.indexOf("encoding=\"UTF-8\"") > 0) {
          byte byteXml[] = baos.toByteArray();
          message = new String(byteXml, "UTF-8");
        }
        else if (message.indexOf("encoding=\"GB2312\"") > 0) {
          byte byteXml[] = baos.toByteArray();
          message = new String(byteXml, "GB2312");
        }
        int xmlbeg = message.indexOf(MMConstants.BEGINXMLFLAG);
        //得到SOAP消息的Envelope部分
        //add by hudm 2004-03-21
        int index1 = message.indexOf(MMConstants.BOUNDARY);
        String strboundary = "";
        String xmlContent = "";
        int index2 = 0, index3 = 0, xmlend = 0;
        if (index1 > 0) {
          index2 = message.indexOf("\"", index1 + 1);
          index3 = message.indexOf("\"", index2 + 1);
          strboundary = message.substring(index2 + 1, index3);
          xmlend = message.indexOf("--" + strboundary, xmlbeg + 1);
          if (xmlend > 0)
            xmlContent = message.substring(xmlbeg, xmlend);
        }
        else
          xmlContent = message.substring(xmlbeg);
        setEnvelope(xmlContent);
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        bb.write(baos.toByteArray(), xmlbeg, (baos.size() - xmlbeg));
        setSoapStream(bb);
        //解析SOAPEnvelope部分，并返回MM7RSReq消息
        MM7RSReq req = new MM7RSReq();
        req = parseXML(xmlContent);
        if (req != null) {
          attachbaos.write(baos.toByteArray(), xmlend, (baos.size() - xmlend));
          /**若返回的消息类型为MM7DeliverReq，则进行附件的解析，
           * 返回MM7DeliverReq类型的消息*/
          if (req instanceof MM7DeliverReq) {
            MM7DeliverReq deliverReq = (MM7DeliverReq) req;
            req = parseAttachment(attachbaos, deliverReq);
          }
          bDecoder = true;
        }
        else {
          bDecoder = false;
        }
      }
    }
    catch (Exception e) {
      System.err.println(e);
    }
  }

  private MM7DeliverReq parseAttachment(ByteArrayOutputStream content,MM7DeliverReq deliverReq)
  {

    MMContent deliverContent = new MMContent();
    //add by hudm 2004-03-21
    String boundary = "";
    int bound_index1 = content.toString().indexOf(MMConstants.BOUNDARY);
    if(bound_index1>0)
    {
    	int bound_index2 = bound_index1 + MMConstants.BOUNDARY.length();
        char c = content.toString().charAt(bound_index2 + 1);
        if(c == '"')
          bound_index2 ++;
        int bound_index3 = bound_index2;
        while(1 == 1)
        {
          c = content.toString().charAt(bound_index3);
          if(c == ';' || c == '\r' || c == '\n')
            break;
          bound_index3 ++;
        }
        c = content.toString().charAt(bound_index3 - 1);
        if(c == '"')
          bound_index3 --;
    	boundary = content.toString().substring(bound_index2+1,bound_index3);
    }
    //end add by hudm 2004-03-21
    int length = boundary.length()+2;
    List bound = new ArrayList();
    //得到所有边界部分的index
    byte[] bContent = content.toByteArray();
    int index = 0;
    byte[] bBoundary = ("--"+boundary).getBytes();
    int m= 0;
    for(int i=0;i<bContent.length;i++)
    {
      if(bContent[i]==bBoundary[m]&&m<bBoundary.length)
      {
        if(m==bBoundary.length-1)
        {
          index = i-bBoundary.length+1;
          bound.add(Integer.toString(index));
          m = 0;
          continue;
        }
        index = i;
        m++;
        continue;
      }
      else
      {
        m = 0;
        continue;
      }
    }
    for(int j=1;j<bound.size();j++)
    {
      MMContent subContent = new MMContent();
      int bound1 = Integer.parseInt((String)bound.get(j-1));
      int bound2 = Integer.parseInt((String)bound.get(j));
      ByteArrayOutputStream attachment = new ByteArrayOutputStream();

      attachment.write(content.toByteArray(),bound1+length,(bound2-(bound1+length)));
      int type_index = attachment.toString().toLowerCase().indexOf(MMConstants.CONTENT_TYPE);
      int attach_index = 0;
      //若存在ContentType，则取得值并进行赋值
      if(type_index>0)
      {
        //得到附件的具体内容，并进行赋值
        int attindex = attachment.toString().indexOf("\r\n\r\n");
        if(attindex>0)
        {
          ByteArrayOutputStream att = new ByteArrayOutputStream();
          //add by hudm 2004-04-01
          //修改目的：判断内容是否进行了base64编码，若是则先进行base64解码
          String strEncoding = "Content-Transfer-Encoding";
          int encodingindex = attachment.toString().indexOf(strEncoding);
          if(encodingindex>=0)
          {
            int endEncoding = attachment.toString().indexOf("\r\n",encodingindex+1);
            String EncodingType = attachment.toString().substring(
                encodingindex+strEncoding.length()+1,endEncoding).trim();
            if(EncodingType.equalsIgnoreCase("base64"))
            {
              att = getFromBASE64(attachment.toString().substring(endEncoding,attachment.size()).trim());
            }
            else
            {
              att.write(attachment.toByteArray(), attindex + 4,
                      attachment.size() - attindex - 4);
            }
          }
          else
          {
            att.write(attachment.toByteArray(), attindex + 4,
                      attachment.size() - attindex - 4);
          }
          //end add by hudm 2004-04-01
          subContent = MMContent.createFromBytes(att.toByteArray());
        }
        int lineend = attachment.toString().indexOf("\r\n",type_index+1);
        attach_index = lineend;
        int linebeg = type_index+MMConstants.CONTENT_TYPE.length()+1;
        String contentType = attachment.toString().substring(linebeg,lineend).trim();
        if(contentType == null || contentType.equals(""))
          continue;
        subContent.setContentType(new MMContentType(contentType));
        int id_index = attachment.toString().toLowerCase().indexOf(MMConstants.CONTENT_ID);
        //若存在ContentID，则取得值并进行赋值
        if (id_index > 0) {
          lineend = attachment.toString().indexOf("\r", id_index + 1);
          attach_index = lineend;
          linebeg = id_index + MMConstants.CONTENT_ID.length() + 1;
          String contentID = attachment.toString().substring(linebeg, lineend).trim();
          subContent.setContentID(contentID);
        }

        int char_index1 = attachment.toString().indexOf(MMConstants.CHARSET);
        if(char_index1 > 0)
        {
          int char_index2 = char_index1 + MMConstants.CHARSET.length();
          char c = attachment.toString().charAt(char_index2 + 1);
          if (c == '"')
            char_index2++;
          int char_index3 = char_index2;
          while (1 == 1) {
            c = attachment.toString().charAt(char_index3);
            if (c == ';' || c == '\r' || c == '\n')
              break;
            char_index3++;
          }
          c = attachment.toString().charAt(char_index3 - 1);
          if (c == '"')
            char_index3--;
          String charset = attachment.toString().substring(char_index2 + 1,char_index3).trim();
          subContent.setCharset(charset);
        }

        int loc_index1 = attachment.toString().indexOf("Content-Location");
        if(loc_index1 > 0)
        {
          int loc_index2 = loc_index1 + "Content-Location".length();
          char c = attachment.toString().charAt(loc_index2 + 1);
          if (c == '"')
              loc_index2++;
          int loc_index3 = loc_index2;
          while(1 == 1){
            c = attachment.toString().charAt(loc_index3);
            if(c == ';' || c == '\r' || c == '\n')
              break;
            loc_index3++;
          }
          c = attachment.toString().charAt(loc_index3 -1);
          if(c == '"')
            loc_index3--;
          String location = attachment.toString().substring(loc_index2 + 1,loc_index3).trim();
          subContent.setContentLocation(location);
        }

        deliverContent.addSubContent(subContent);
      }
      else
        continue;
    }
    deliverReq.setContent(deliverContent);
    return deliverReq;
  }

  /**对SOAP的Envelope部分进行解码*/
  private MM7RSReq parseXML(String xmlContent)
  {
    String xml = "";
    int index1 = xmlContent.indexOf("encoding=\"UTF-8\"");
    if(index1 > 0)
    {
      xml = xmlContent.substring(0, index1) + "encoding=\"GB2312\"" +
          xmlContent.substring(index1 + "encoding=\"UTF-8\"".length());
      xmlContent = xml;
    }
    if(xmlContent.lastIndexOf("Envelope>")>0)
    {
      SAXBuilder sax = new SAXBuilder();
      try {
        ByteArrayInputStream in = new ByteArrayInputStream(xmlContent.getBytes(
            "GB2312"));
        Document doc = sax.build(in);
        Element root = doc.getRootElement();
        Element envBody = (Element) root.getChildren().get(1);
        Element Message = (Element) envBody.getChildren().get(0);
        mm7rsReq = DecodeBody(Message);
        Element envHeader = (Element) root.getChildren().get(0);
        Element transID = (Element) envHeader.getChildren().get(0);
        String transactionID = transID.getTextTrim();
        if (transactionID != null || !transactionID.equals(""))
          mm7rsReq.setTransactionID(transactionID);
        else
          System.err.println("TransactionID必须有。");
        return mm7rsReq;
      }
      catch (JDOMException jdome) {
        jdome.printStackTrace();
        return null;
      }
      catch (Exception e) {
        //System.out.println("e=" + e);
        return null;
      }
    }
    else
    {
      return null;
    }
  }

  /**对soap Envelope中的env:Body部分进行解码*/
  private MM7RSReq DecodeBody(Element message)
  {
    String messageType = message.getName();
    MessageName = messageType;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    int size = message.getChildren().size();
    try{
      if (messageType.equals("DeliverReq")) {
        MM7DeliverReq deliverReq = new MM7DeliverReq();
        for (int i = 0; i < size; i++) {
          Element child = (Element) message.getChildren().get(i);
          String name = child.getName();
          if (name.equals("Sender"))
          {
            //Element sub = (Element)child.getChildren().get(0);
            deliverReq.setSender(child.getTextTrim());
            //deliverReq.setSender(sub.getTextTrim());
            continue;
          }
          if (name.equals("Recipients")) {
            List To = new ArrayList();
            List Cc = new ArrayList();
            List Bcc = new ArrayList();
            int recsize = child.getChildren().size();
            for (int j = 0; j < recsize; j++) {
              Element rec = (Element) child.getChildren().get(j);
              if (rec.getName().equals("To")) {
                int tosize = rec.getChildren().size();
                for (int m = 0; m < tosize; m++) {
                  Element ele = (Element) rec.getChildren().get(m);
                  if (ele.getName().equals("Number")||ele.getName().equals("RFC2822Address")
                      || ele.getName().equals("ShortCode"))
                    To.add(ele.getTextTrim());
                }
                continue;
              }
              if (rec.getName().equals("Cc")) {
                int ccsize = rec.getChildren().size();
                for (int m = 0; m < ccsize; m++) {
                  Element ele = (Element) rec.getChildren().get(m);
                  if (ele.getName().equals("Number")||ele.getName().equals("RFC2822Address")
                      || ele.getName().equals("ShortCode"))
                    Cc.add(ele.getTextTrim());
                }
                continue;
              }
              if (rec.getName().equals("Bcc")) {
                int bccsize = rec.getChildren().size();
                for (int m = 0; m < bccsize; m++) {
                  Element ele = (Element) rec.getChildren().get(m);
                  if (ele.getName().equals("Number")||ele.getName().equals("RFC2822Address")
                      || ele.getName().equals("ShortCode"))
                    Bcc.add(ele.getTextTrim());
                }
                continue;
              }
            }
            if(!To.isEmpty())
              deliverReq.setTo(To);
            if(!Cc.isEmpty())
              deliverReq.setCc(Cc);
            if(!Bcc.isEmpty())
              deliverReq.setBcc(Bcc);
            continue;
          }
          if (name.equals("LinkedID")){
            deliverReq.setLinkedID(child.getTextTrim());
            continue; }
          if (name.equals("MMSRelayServerID")){
            deliverReq.setMMSRelayServerID(child.getTextTrim());
            continue; }
          if (name.equals("ReplyChargingID")){
            deliverReq.setReplyChargingID(child.getTextTrim());
            continue; }
          if (name.equals("Subject")){
            deliverReq.setSubject(child.getTextTrim());
            continue; }
          if (name.equals("Priority")){
            if(child.getTextTrim().equalsIgnoreCase("Low"))
              deliverReq.setPriority((byte)0);
            else if(child.getTextTrim().equalsIgnoreCase("Normal"))
              deliverReq.setPriority((byte)1);
            else if(child.getTextTrim().equalsIgnoreCase("High"))
              deliverReq.setPriority((byte)2);
            continue; }
          if (name.equals("TimeStamp")){
            String time = child.getTextTrim();
            if(time.length()>19)
            {
              time = time.substring(0, 19);
            }
            deliverReq.setTimeStamp(sdf.parse(time));
            continue; }
        }
        return deliverReq;
      }
      else if (messageType.equals("DeliveryReportReq")) {
        MM7DeliveryReportReq deliveryReportReq = new MM7DeliveryReportReq();
        for (int i = 0; i < size; i++) {
          Element child = (Element) message.getChildren().get(i);
          String name = child.getName();
          if(name.equals("MMSRelayServerID")){
            deliveryReportReq.setMMSRelayServerID(child.getTextTrim());
            continue; }
          if(name.equals("MessageID")){
            deliveryReportReq.setMessageID(child.getTextTrim());
            continue; }
          if (name.equals("Recipient")) {
            int recsize = child.getChildren().size();
            for (int j = 0; j < recsize; j++) {
              Element rec = (Element) child.getChildren().get(j);
              deliveryReportReq.setRecipient(rec.getTextTrim());
              continue;
            }
          }
          if(name.equals("Sender")){
            deliveryReportReq.setSender(child.getTextTrim());
            continue; }
          if(name.equals("TimeStamp")){
            String time = child.getTextTrim();
            if(time.length()>19)
            {
              time = time.substring(0, 19);
            }
            deliveryReportReq.setTimeStamp(sdf.parse(time));
            continue; }
          if(name.equals("MMStatus")){
            if(child.getTextTrim().equalsIgnoreCase("Expired"))
              deliveryReportReq.setMMStatus((byte)0);
            else if(child.getTextTrim().equalsIgnoreCase("Retrieved"))
              deliveryReportReq.setMMStatus((byte)1);
            else if(child.getTextTrim().equalsIgnoreCase("Rejected"))
              deliveryReportReq.setMMStatus((byte)2);
            else if(child.getTextTrim().equalsIgnoreCase("System Rejected"))
              deliveryReportReq.setMMStatus((byte)2);
            else if(child.getTextTrim().equalsIgnoreCase("Recipient Rejected"))
              deliveryReportReq.setMMStatus((byte)3);
            else if(child.getTextTrim().equalsIgnoreCase("Indeterminate"))
              deliveryReportReq.setMMStatus((byte)4);
            else if(child.getTextTrim().equalsIgnoreCase("Forwarded"))
              deliveryReportReq.setMMStatus((byte)5);
            else
              deliveryReportReq.setMMStatus((byte)4);
            continue;
          }
          if(name.equals("StatusText")){
            deliveryReportReq.setStatusText(child.getTextTrim());
            continue;}
        }
        mm7rsReq = deliveryReportReq;
      }
      else if (messageType.equals("ReadReplyReq")) {
        MM7ReadReplyReq readReplyReq = new MM7ReadReplyReq();
        for (int i = 0; i < size; i++) {
          Element child = (Element) message.getChildren().get(i);
          String name = child.getName();
          if(name.equals("MMSReplayServerID")){
            readReplyReq.setMMSRelayServerID(child.getTextTrim());
            continue; }
          if(name.equals("MessageID")){
            readReplyReq.setMessageID(child.getTextTrim());
            continue; }
          if (name.equals("Recipient")) {
            int recsize = child.getChildren().size();
            for (int j = 0; j < recsize; j++) {
              Element rec = (Element) child.getChildren().get(j);
              readReplyReq.setRecipient(rec.getTextTrim());
              continue;
            }
          }
          if(name.equals("Sender")){
            readReplyReq.setSender(child.getTextTrim());
            continue; }
          if(name.equals("TimeStamp")){
            String time = child.getTextTrim();
            if(time.length()>19)
            {
              time = time.substring(0, 19);
            }
            readReplyReq.setTimeStamp(sdf.parse(time));
            continue; }
          if(name.equals("MMStatus")){
            String strReadStatus = child.getTextTrim().trim();
            byte bReadStatus = 2;
            if(strReadStatus.equalsIgnoreCase("read"))
              bReadStatus = 0;
            else if(strReadStatus.equalsIgnoreCase("deleted"))
              bReadStatus = 1;
            else
              bReadStatus = 2;
            readReplyReq.setMMStatus(bReadStatus);
           // readReplyReq.setMMStatus(Byte.parseByte(child.getTextTrim()));
            continue; }
          if(name.equals("StatusText")){
            readReplyReq.setStatusText(child.getTextTrim());
            continue; }
        }
        mm7rsReq = readReplyReq;
      }
      else
        return null;
      return mm7rsReq;
    }catch(ParseException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**对内容进行BASE64解码*/
  // 将 BASE64 编码的字符串 s 进行解码
  public static ByteArrayOutputStream getFromBASE64(String s) {
    if (s == null)
      return null;
    BASE64Decoder decoder = new BASE64Decoder();
    ByteArrayOutputStream decodebaos = new ByteArrayOutputStream();
    try {
      byte[] b = decoder.decodeBuffer(s);
      decodebaos.write(b);
      return decodebaos;
    }
    catch (Exception e) {
      return null;
    }
  }
}

