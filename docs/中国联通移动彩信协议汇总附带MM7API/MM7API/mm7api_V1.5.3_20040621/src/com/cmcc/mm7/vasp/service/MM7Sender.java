/**File Name:MM7Sender.java
 * Company:  中国移动集团公司
 * Date  :   2004-1-17
 * */

package com.cmcc.mm7.vasp.service;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import sun.misc.*;
import org.jdom.*;
import org.jdom.input.*;
import java.security.*;
import com.cmcc.mm7.vasp.common.*;
import com.cmcc.mm7.vasp.conf.*;
import com.cmcc.mm7.vasp.message.*;

public class MM7Sender
{
  private MM7Config mm7Config;
  private BufferedOutputStream sender;
  private BufferedInputStream receiver;
  private StringBuffer sb;
  private StringBuffer beforAuth;
  private String AuthInfor;
  private String DigestInfor;
  private StringBuffer afterAuth;
  private StringBuffer entityBody;
  private MM7RSRes res;
  private ByteArrayOutputStream baos;
  private ConnectionPool pool;
  private ConnectionWrap connWrap;
  private int ResendCount;
  private Socket client;
  private int ReadTimeOutCount = 0;
  private ByteArrayOutputStream sendBaos;
  private StringBuffer SevereBuffer;
  private StringBuffer InfoBuffer;
  private StringBuffer FinerBuffer;
  private ByteArrayOutputStream Severebaos;
  private ByteArrayOutputStream Infobaos;
  private ByteArrayOutputStream Finerbaos;
  private long LogTimeBZ;
  private long SameMinuteTime;
  private int N;
  private SimpleDateFormat sdf;
  private DecimalFormat df;
  private String logFileName;
  private byte[] TimeOutbCount;
  private ConnectionWrap TimeOutWrap;
  private boolean TimeOutFlag;
  public int tempnum = 0;

  public MM7Sender()  //构造方法
  {
    reset();
  }
  private void reset()
  {
    File f;
    mm7Config = new MM7Config();
    sender = null;
    receiver = null;
    AuthInfor = "";
    DigestInfor = "";
    sb = new StringBuffer();
    beforAuth = new StringBuffer();
    afterAuth = new StringBuffer();
    entityBody = new StringBuffer();
    res = new MM7RSRes();
    baos = new ByteArrayOutputStream();
    ResendCount = 0;
    connWrap = null;
    //Modify by hudm 2004-06-09
    //pool = ConnectionPool.getInstance();
    pool = new ConnectionPool();
    ///end Modify by hudm 2004-06-09
    client = null;
    sendBaos = new ByteArrayOutputStream();
    SevereBuffer = new StringBuffer();
    InfoBuffer = new StringBuffer();
    FinerBuffer = new StringBuffer();
    Severebaos = new ByteArrayOutputStream();
    Infobaos = new ByteArrayOutputStream();
    Finerbaos = new ByteArrayOutputStream();
    LogTimeBZ = System.currentTimeMillis();
    SameMinuteTime = System.currentTimeMillis();
    N = 1;
    sdf = new SimpleDateFormat("yyyyMMddHHmm");
    df = new DecimalFormat();
    df.applyLocalizedPattern("0000");
    logFileName = "";
    TimeOutbCount = null;
    TimeOutWrap = null;
    TimeOutFlag = false;
  }
  /**构造方法*/
  public MM7Sender(MM7Config config)  throws Exception
  {
    reset();
    mm7Config = config;
    pool.setConfig(mm7Config);
  }
  public void setConfig(MM7Config config)  //设置MM7Config
  {
    mm7Config = config;
    pool.setConfig(mm7Config);
  }
  public MM7Config getConfig()  //获得MM7Config
  {
    return(mm7Config);
  }

  private void setSameMinuteTime(long time)
  {
    SameMinuteTime = time;
  }
  private long getSameMinuteTime()
  {
    return SameMinuteTime;
  }

  public MM7RSRes send(MM7VASPReq mm7VASPReq)  //发送消息
  {
    tempnum++;
    sb = new StringBuffer();
    beforAuth = new StringBuffer();
    afterAuth = new StringBuffer();
    entityBody = new StringBuffer();
    sendBaos = new ByteArrayOutputStream();
    SevereBuffer = new StringBuffer();
    InfoBuffer = new StringBuffer();
    FinerBuffer = new StringBuffer();

    this.Severebaos = new ByteArrayOutputStream();
    this.Finerbaos = new ByteArrayOutputStream();
    this.Infobaos = new ByteArrayOutputStream();

    sender = null;
    receiver = null;
    //reset();

    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    if(mm7VASPReq == null)
    {
      MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
      ErrorRes.setStatusCode(-105);
      ErrorRes.setStatusText("待发送的消息为空!");
      SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
      SevereBuffer.append("[Comments={"+ErrorRes.getStatusCode());
      SevereBuffer.append(";"+ErrorRes.getStatusText()+"}]");
      return ErrorRes;
    }
    try
    {
      String mmscURL = mm7Config.getMMSCURL();
      int httpindex = -1;
      int index = -1;
      if(mmscURL == null)
        mmscURL = "";
      beforAuth.append("POST " + mmscURL + " HTTP/1.1\r\n");
      /**为了当有多个MMSCIP时，可以进行平均分配*/
      if(pool.getIPCount()<mm7Config.getMMSCIP().size())
      {
        beforAuth.append("Host:" + (String) mm7Config.getMMSCIP().get(
            pool.getIPCount()) + "\r\n");
        pool.setIPCount(pool.getIPCount()+1);
      }
      else
      {
        pool.setIPCount(0);
        beforAuth.append("Host:"+(String)mm7Config.getMMSCIP().get(0)+"\r\n");
        pool.setIPCount(pool.getIPCount()+1);
      }
      //设置Host结束
      /**设置ContentType，不带附件为text/xml;带附件为multipart/related*/
      if(mm7VASPReq instanceof MM7SubmitReq)
      {
        MM7SubmitReq submitReq = (MM7SubmitReq)mm7VASPReq;
        InfoBuffer.append("[TransactionID="+submitReq.getTransactionID()+"]");
        InfoBuffer.append("[Message_Type=MM7SubmitReq]");
        InfoBuffer.append("[Sender_Address="+submitReq.getSenderAddress()+"]");
        InfoBuffer.append("[Recipient_Address={");
        if(submitReq.isToExist())
        {
          InfoBuffer.append("To={");
          List to = new ArrayList();
          to = submitReq.getTo();
          for(int i=0;i<to.size();i++)
          {
            InfoBuffer.append( (String) to.get(i) + ",");
          }
          InfoBuffer.append("}");
        }
        if(submitReq.isCcExist())
        {
           InfoBuffer.append("Cc={");
           List cc = new ArrayList();
           cc = submitReq.getCc();
           for(int i=0;i<cc.size();i++)
           {
             InfoBuffer.append( (String) cc.get(i) + ",");
           }
           InfoBuffer.append("}");
        }
        if(submitReq.isBccExist())
        {
           InfoBuffer.append("Bcc={");
           List bcc = new ArrayList();
           bcc = submitReq.getBcc();
           for(int i=0;i<bcc.size();i++)
           {
             InfoBuffer.append( (String) bcc.get(i) + ",");
           }
           InfoBuffer.append("}");
        }
        InfoBuffer.append("}]\r\n");
        if(submitReq.isContentExist())
          beforAuth.append("Content-Type:multipart/related; boundary=\"--NextPart_0_2817_24856\";"+
                           "type=\"text/xml\";start=\"</tnn-200102/mm7-vasp>\""+"\r\n");
        else
          beforAuth.append("Content-Type:text/xml;charset=\""+mm7Config.getCharSet()+"\""+"\r\n");
      }
      else if(mm7VASPReq instanceof MM7ReplaceReq)
      {
        MM7ReplaceReq replaceReq = (MM7ReplaceReq)mm7VASPReq;
        InfoBuffer.append("[TransactionID="+replaceReq.getTransactionID()+"]");
        InfoBuffer.append("[Message_Type=MM7ReplaceReq]\r\n");
        if(replaceReq.isContentExist())
          beforAuth.append("Content-Type:multipart/related; boundary=\"--NextPart_0_2817_24856\";"+"\r\n");
        else
          beforAuth.append("Content-Type:text/xml;charset=\""+mm7Config.getCharSet()+"\""+"\r\n");
      }
      else if(mm7VASPReq instanceof MM7CancelReq)
      {
        MM7CancelReq cancelReq = (MM7CancelReq)mm7VASPReq;
        InfoBuffer.append("[TransactionID="+cancelReq.getTransactionID()+"]");
        InfoBuffer.append("[Message_Type=MM7CancelReq]\r\n");
        beforAuth.append("Content-Type:text/xml;charset=\""+mm7Config.getCharSet()+"\""+"\r\n");
      }
      else
      {
        MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
        ErrorRes.setStatusCode( -106);
        ErrorRes.setStatusText("没有匹配的消息，请确认要发送的消息是否正确！");
        SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        SevereBuffer.append("[Comments={"+ErrorRes.getStatusCode());
        SevereBuffer.append(";"+ErrorRes.getStatusText()+"}]");
        return ErrorRes;
      }
      //设置ContentType结束
      //设置Content-Trans-Encoding
      beforAuth.append("Content-Transfer-Encoding:8bit" + "\r\n");
       AuthInfor = "Authorization:Basic " +
              getBASE64(mm7Config.getUserName() + ":" +
                        mm7Config.getPassword()) + "\r\n";

      afterAuth.append("SOAPAction:\"\""+"\r\n");
      RetriveApiVersion apiver = new RetriveApiVersion();
      afterAuth.append("MM7APIVersion:"+apiver.getApiVersion()+"\r\n");
      /**判断是否是长连接，若是长连接，则将Connection设为keep-alive，
       * 否则设为close，以告诉服务器端客户端是长连接还是短连接*/
      if(pool.getKeepAlive().equals("on"))
      {
        afterAuth.append("Connection: Keep-Alive" + "\r\n");
      }
      else
      {
        afterAuth.append("Connection:Close" + "\r\n");
      }

      byte[] bcontent = getContent(mm7VASPReq);
      if(bcontent.length>mm7Config.getMaxMsgSize())
      {
        MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
        ErrorRes.setStatusCode( -113);
        ErrorRes.setStatusText("消息内容的尺寸超出允许发送的大小！");
        SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
        SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
        return ErrorRes;
      }
      this.TimeOutbCount = bcontent;
      String env = "";
      try{
        ByteArrayOutputStream tempbaos = new ByteArrayOutputStream();
        tempbaos.write(bcontent);
        int envbeg = tempbaos.toString().indexOf(MMConstants.BEGINXMLFLAG);
        int envend = tempbaos.toString().indexOf("</env:Envelope>");
        env = tempbaos.toString().substring(envbeg,envend);
        env = env + "</env:Envelope>";
      }catch(IOException ioe)
      {
        ioe.printStackTrace();
      }
      finally{
        //设置消息体长度
        afterAuth.append("Content-Length:" + bcontent.length + "\r\n");
        afterAuth.append("Mime-Version:1.0" + "\r\n");
        afterAuth.append("\r\n");
        entityBody.append(new String(bcontent));
        sendBaos = getSendMessage(bcontent);
        String time = "[" + simple.format(new Date(System.currentTimeMillis())) +
            "]";
        if (sendBaos != null) {
          res = SendandReceiveMessage(sendBaos);
        }
        else {
          MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
          ErrorRes.setStatusCode( -104);
          ErrorRes.setStatusText("Socket不通！");
          SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
          SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
          SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
          return ErrorRes;
        }

        FinerBuffer.append(InfoBuffer);
        InfoBuffer.append(env);
        SevereBuffer.insert(0, "\r\n\r\n" + time + "[1]");
        InfoBuffer.insert(0, "\r\n\r\n" + time + "[3]");
        FinerBuffer.insert(0, "\r\n\r\n" + time + "[6]");

        Severebaos.write(SevereBuffer.toString().getBytes());
        Infobaos.write(SevereBuffer.toString().getBytes());
        Infobaos.write(InfoBuffer.toString().getBytes());
        Finerbaos.write(SevereBuffer.toString().getBytes());
        Finerbaos.write(InfoBuffer.toString().getBytes());
        Finerbaos.write(FinerBuffer.toString().getBytes());
        Finerbaos.write(bcontent);

        /*************/
        InfoBuffer = new StringBuffer();
        FinerBuffer = new StringBuffer();
        time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";
        InfoBuffer.append("\r\n\r\n" + time + "[3]");
        FinerBuffer.append("\r\n\r\n" + time + "[6]");
        InfoBuffer.append("[TransactionID=" + res.getTransactionID() + "]");
        FinerBuffer.append("[TransactionID=" + res.getTransactionID() + "]");
        if (res instanceof MM7SubmitRes) {
          InfoBuffer.append("[Message_Type=MM7SubmitRes]");
          FinerBuffer.append("[Message_Type=MM7SubmitRes]");
        }
        else if (res instanceof MM7CancelRes) {
          InfoBuffer.append("[Message_Type=MM7CancelRes]");
          FinerBuffer.append("[Message_Type=MM7CancelRes]");
        }
        else if (res instanceof MM7ReplaceRes) {
          InfoBuffer.append("[Message_Type=MM7ReplaceRes]");
          FinerBuffer.append("[Message_Type=MM7ReplaceRes]");
        }
        else if (res instanceof MM7RSErrorRes) {
          InfoBuffer.append("[Message_Type=MM7RSErrorRes]");
          FinerBuffer.append("[Message_Type=MM7RSErrorRes]");
        }
        InfoBuffer.append("[Comments={" + res.getStatusCode() + ";" +
                          res.getStatusText() + "}]\r\n");
        FinerBuffer.append("[Comments={" + res.getStatusCode() + ";" +
                           res.getStatusText() + "}]\r\n");
        int envbeg = baos.toString().indexOf(MMConstants.BEGINXMLFLAG);
        int envend = baos.toString().indexOf("</env:Envelope>");
        if (envbeg > 0 && envend > 0)
          env = baos.toString().substring(envbeg);
        InfoBuffer.append(env);
        FinerBuffer.append(env);

        Infobaos.write(InfoBuffer.toString().getBytes());
        Finerbaos.write(InfoBuffer.toString().getBytes());
        Finerbaos.write(FinerBuffer.toString().getBytes());
        /*************/

        int LogLevel = mm7Config.getLogLevel();
        if (LogLevel > 0) {
          String LogPath = mm7Config.getLogPath();
          int LogNum = mm7Config.getLogNum();
          int LogInterval = mm7Config.getLogInterval();
          int LogSize = mm7Config.getLogSize();
          long Interval = System.currentTimeMillis() - LogTimeBZ;
          String sTimeNow = sdf.format(new Date(System.currentTimeMillis()));
          long timeNow, timeFile = 0;
          timeNow = sdf.parse(sTimeNow).getTime();
          if (logFileName.length() > 0) {
            File logFile = new File(logFileName);
            int index1 = logFileName.indexOf(mm7Config.getMmscId() + "_");
            int index11 = index1 + mm7Config.getMmscId().length() + 1;
            int index2 = logFileName.indexOf(".", index11);
            String sTimeFile = logFileName.substring(index1 +
                mm7Config.getMmscId().length() + 1, index2);
            timeFile = sdf.parse(sTimeFile).getTime();

            if (timeNow - timeFile > (long) LogInterval * 60 * 1000) {
              N = 1;
              this.deleteFile(LogPath, LogNum, mm7Config.getMmscId());
              logFileName = LogPath + "/" + mm7Config.getMmscId() + "_" +
                  sTimeNow + "." + df.format(N) + ".log";
            }
            else {
              if (logFile.length() > LogSize * 1024) {
                if (N < LogNum)
                  N++;
                else
                  N = 1;
                this.deleteFile(LogPath, LogNum, mm7Config.getMmscId());
                logFileName = LogPath + "/" + mm7Config.getMmscId() + "_" +
                    sTimeFile + "." + df.format(N) + ".log";
              }
            }
          }
          else {
            N = 1;
            this.deleteFile(LogPath, LogNum, mm7Config.getMmscId());
            logFileName = LogPath + "/" + mm7Config.getMmscId() + "_" +
                sTimeNow +
                "." + df.format(N) + ".log";
          }
          switch (LogLevel) {
            case 1:
              try {
                FileOutputStream fos = new FileOutputStream(logFileName, true);
                fos.write(Severebaos.toByteArray());
                fos.close();
                SevereBuffer = new StringBuffer();
              }
              catch (IOException ioe) {
                ioe.printStackTrace();
              }
              break;
            case 2:
              break;
            case 3:
              try {
                FileOutputStream fos = new FileOutputStream(logFileName, true);
                fos.write(Infobaos.toByteArray());
                fos.close();
                Infobaos.reset();
              }
              catch (IOException ioe) {
                ioe.printStackTrace();
              }
              break;
            case 4:
              break;
            case 6:
              try {
                FileOutputStream fos = new FileOutputStream(logFileName, true);
                fos.write(Finerbaos.toByteArray());
                fos.close();
                Finerbaos = new ByteArrayOutputStream();
              }
              catch (IOException ioe) {
                ioe.printStackTrace();
              }
              break;
            case 7:
              break;
            default:
              break;
          }
        }
      }
      return res;
    }
    catch(Exception e){
      MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
      e.printStackTrace();
      ErrorRes.setStatusCode(-100);
      ErrorRes.setStatusText("系统错误！原因："+e);
      return ErrorRes;
    }
  }

  private void deleteFile(String logpath,int lognum,String MMSCID)
  {
    File parfile = new File(logpath);
    if(parfile.isDirectory())
    {
      File[] subfile = parfile.listFiles();
      List list = new ArrayList();
      for (int i = 0; i < subfile.length; i++) {
        String name = subfile[i].getName();
        if (name.indexOf(MMSCID) >= 0) {
          list.add(name);
        }
      }
      if(list.size() >= lognum)
      {
        int deleteLength = list.size()-lognum+1;
        Comparator comp = Collections.reverseOrder();
        Collections.sort(list, comp);
        for (int i = list.size()-deleteLength; i < list.size(); i++) {
          String strfile = (String) list.get(i);
          File ff = new File(logpath + "/" + strfile);
          ff.delete();
        }
      }
    }
  }

  private byte[] getContent(MM7VASPReq mm7VASPReq)
  {
    byte[] b = null;
    SOAPEncoder encoder = new SOAPEncoder(mm7Config);
    encoder.setMessage(mm7VASPReq);
    try{
      encoder.encodeMessage();
    }catch(Exception e){
      System.err.println(e);
    }
    b = encoder.getMessage();
    return(b);
  }
  /**进行BASE64编码*/
  public static String getBASE64(String value)
  {
    if(value == null)
      return null;
    BASE64Encoder BaseEncode = new BASE64Encoder();
    return(BaseEncode.encode(value.getBytes()));
  }

  private MM7RSRes parseXML()
  {
    SAXBuilder sax = new SAXBuilder();
    SevereBuffer.append("recv="+baos.toString()+"\r\n");
    try
    {
      if(baos.toString()== null || baos.toString().equals(""))
      {
        MM7RSErrorRes errRes = new MM7RSErrorRes();
        errRes.setStatusCode(-107);
        errRes.setStatusText("错误！接收到的消息为空！");
        SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        SevereBuffer.append("[Comments={-107;错误！接收到的消息为空！}]");
        return errRes;
      }
      else
      {
        //System.out.println("baos="+baos.toString());
        int index = -1;
        int xmlend = -1;
        index = baos.toString().indexOf(MMConstants.BEGINXMLFLAG);
        if(index == -1)
        {
          int httpindex = -1;
          httpindex = baos.toString().indexOf("HTTP1.1");
          String strstat = "";
          if(httpindex>=0)
          {
            int index11 = baos.toString().indexOf("\r\n");
            strstat = baos.toString().substring(httpindex+7,index11);
          }
          MM7RSErrorRes err = new MM7RSErrorRes();
          err.setStatusCode(-108);
          if(!strstat.equals(""))
            err.setStatusText(strstat);
          else
            err.setStatusText("Bad Request");
          SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
          SevereBuffer.append("[Comments={"+err.getStatusCode()+";"+err.getStatusText()+"}]");
          return err;
        }
        String xmlContent = baos.toString().substring(index,baos.toString().length());
        String xmlContentTemp = "";
        byte[] byteXML = baos.toByteArray();
        int index1 = xmlContent.indexOf("encoding=\"UTF-8\"");
        if(index1 > 0)
        {
          xmlContentTemp = new String(byteXML,"UTF-8");
          int xmlind = xmlContentTemp.indexOf(MMConstants.BEGINXMLFLAG);
          int xmlindend = xmlContentTemp.lastIndexOf("Envelope>");
          if(xmlindend >0)
          {
            xmlContentTemp = xmlContentTemp.substring(xmlind,xmlindend+9);
            String xml = xmlContentTemp.substring(0, index1) +
                "encoding=\"GB2312\"" +
                xmlContentTemp.substring(index1 + "encoding=\"UTF-8\"".length());
            xmlContent = xml;
          }
        }
        SevereBuffer.append("!xmlContent="+xmlContent+"!\r\n");
        ByteArrayInputStream in = new ByteArrayInputStream(xmlContent.getBytes());
        Document doc = sax.build(in);
        Element root = doc.getRootElement();
        Element first = (Element) root.getChildren().get(0);
        if(first.getName().equalsIgnoreCase("body"))
        {
          Element Message = (Element) first.getChildren().get(0);
          if(Message.getName().equals("Fault"))
          {
            MM7RSErrorRes errRes = new MM7RSErrorRes();
            errRes.setStatusCode( -110);
            errRes.setStatusText("Server could not fulfill the request");
            SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
            SevereBuffer.append(
                "[Comments={-110;Server could not fulfill the request}]");
            return errRes;
          }
          else
          {
            MM7RSErrorRes errRes = new MM7RSErrorRes();
            errRes.setStatusCode( -111);
            errRes.setStatusText("Server error");
            SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
            SevereBuffer.append("[Comments={-111;Server error}]");
            return errRes;
          }
        }
        else{
          Element envBody = (Element) root.getChildren().get(1);
          Element Message = (Element) envBody.getChildren().get(0);
          Element envHeader = (Element) root.getChildren().get(0);
          Element transID = (Element) envHeader.getChildren().get(0);
          String transactionID = transID.getTextTrim();
          int size = Message.getChildren().size();
          SevereBuffer.append("\r\nMessage.getName()=" + Message.getName() +
                              "\r\n");
          if (Message.getName().equals("SubmitRsp")) {
            MM7SubmitRes submitRes = new MM7SubmitRes();
            submitRes.setTransactionID(transactionID);
            for (int i = 0; i < size; i++) {
              Element ele = (Element) Message.getChildren().get(i);
              if (ele.getName().equals(MMConstants.STATUS)) {
                for (int j = 0; j < ele.getChildren().size(); j++) {
                  Element subEle = (Element) ele.getChildren().get(j);
                  if (subEle.getName().equals(MMConstants.STATUSCODE))
                    submitRes.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
                  else if (subEle.getName().equals(MMConstants.STATUSTEXT))
                    submitRes.setStatusText(subEle.getTextTrim());
                  else if (subEle.getName().equals(MMConstants.STATUSDETAIL))
                    submitRes.setStatusDetail(subEle.getTextTrim());
                }
              }
              else if (ele.getName().equals(MMConstants.MESSAGEID)) {
                submitRes.setMessageID(ele.getTextTrim());
              }
            }
            return submitRes;
          }
          else if (Message.getName().equals("CancelRsp")) {
            MM7CancelRes cancelRes = new MM7CancelRes();
            cancelRes.setTransactionID(transactionID);
            for (int i = 0; i < size; i++) {
              Element ele = (Element) Message.getChildren().get(i);
              if (ele.getName().equals(MMConstants.STATUS)) {
                for (int j = 0; j < ele.getChildren().size(); j++) {
                  Element subEle = (Element) ele.getChildren().get(j);
                  if (subEle.getName().equals(MMConstants.STATUSCODE))
                    cancelRes.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
                  else if (subEle.getName().equals(MMConstants.STATUSTEXT))
                    cancelRes.setStatusText(subEle.getTextTrim());
                  else if (subEle.getName().equals(MMConstants.STATUSDETAIL))
                    cancelRes.setStatusDetail(subEle.getTextTrim());
                }
              }
            }
            return cancelRes;
          }
          else if (Message.getName().equals("ReplaceRsp")) {
            MM7ReplaceRes replaceRes = new MM7ReplaceRes();
            replaceRes.setTransactionID(transactionID);
            for (int i = 0; i < size; i++) {
              Element ele = (Element) Message.getChildren().get(i);
              if (ele.getName().equals(MMConstants.STATUS)) {
                for (int j = 0; j < ele.getChildren().size(); j++) {
                  Element subEle = (Element) ele.getChildren().get(j);
                  if (subEle.getName().equals(MMConstants.STATUSCODE))
                    replaceRes.setStatusCode(Integer.parseInt(subEle.
                        getTextTrim()));
                  else if (subEle.getName().equals(MMConstants.STATUSTEXT))
                    replaceRes.setStatusText(subEle.getTextTrim());
                  else if (subEle.getName().equals(MMConstants.STATUSDETAIL))
                    replaceRes.setStatusDetail(subEle.getTextTrim());
                }
              }
            }
            return replaceRes;
          }
          else {
            MM7RSRes res = new MM7RSRes();
            res.setTransactionID(transactionID);
            for (int i = 0; i < size; i++) {
              Element ele = (Element) Message.getChildren().get(i);
              if (ele.getName().equals(MMConstants.STATUS)) {
                for (int j = 0; j < ele.getChildren().size(); j++) {
                  Element subEle = (Element) ele.getChildren().get(j);
                  if (subEle.getName().equals(MMConstants.STATUSCODE))
                    res.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
                  else if (subEle.getName().equals(MMConstants.STATUSTEXT))
                    res.setStatusText(subEle.getTextTrim());
                  else if (subEle.getName().equals(MMConstants.STATUSDETAIL))
                    res.setStatusDetail(subEle.getTextTrim());
                }
              }
            }
            return res;
          }
        }
      }
    }catch(JDOMException jdome)
    {
      MM7RSErrorRes error = new MM7RSErrorRes();
      System.err.print(jdome);
      error.setStatusCode(-109);
      error.setStatusText("XML解析错误！原因："+jdome);
      SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
      SevereBuffer.append("[Comments={-109;"+error.getStatusText()+"}]");
      return error;
    }
    catch(Exception e){
      e.printStackTrace();
      MM7RSErrorRes error = new MM7RSErrorRes();
      error.setStatusCode(-100);
      error.setStatusText("系统错误！原因："+e);
      return error;
    }
  }

  private ByteArrayOutputStream getSendMessage(byte[] bcontent)
  {
    try{
      /**在此加以判断，如果是长连接，则得到以前的连接，否则新建连接
       * */
      if (pool.getKeepAlive().equals("on")) {
        if(this.TimeOutFlag==true)
        {
          SevereBuffer.append("TimeOutFlag=true\r\n");
          if(pool.deleteURL(TimeOutWrap))
          {
            SevereBuffer.append("  TimeOutWrap="+TimeOutWrap);
            SevereBuffer.append("  pool.deleteURL(TimeOutWrap)");
            ////Add by hudm 2004-06-07
			/*String MMSCIP = (String)mm7Config.getMMSCIP().get(0);
			int index = MMSCIP.indexOf(":");
			String ip;
			int port;
			if(index == -1)
			{
			  ip = MMSCIP;
			  port = 80;
			}
			else
			{
			  ip = MMSCIP.substring(0,index);
			  port = Integer.parseInt(MMSCIP.substring(index+1));
			}
			client = new Socket(ip,port);*/
            ////end add by hudm
            connWrap = pool.getConnWrap();
            this.TimeOutWrap = connWrap;
            if (connWrap != null) {
              SevereBuffer.append("  connWrap != null");
              client = connWrap.getSocket();
            }
            else
            {
              SevereBuffer.append("   client=null");
              client = null;
            }
	      }
	      else
	      {
	        SevereBuffer.append("deleteURL is false!");
	        return null;
	      }
        }
        else
        {
          //SevereBuffer.append("!767!TimeOutFlag==false");
          connWrap = pool.getConnWrap();
          this.TimeOutWrap = connWrap;
          if(connWrap != null)
          {
          //SevereBuffer.append("  connWrap != null");
		    client = connWrap.getSocket();
                    SevereBuffer.append("!!!!client="+client);
            /////Add by hudm 2004-06-07
			if (connWrap.getUserfulFlag() == false || client.isConnected() == false) {
			SevereBuffer.append("!771!connWrap.getUserfulFlag() == false || client.isConnected() == false");
				pool.deleteURL(connWrap);
				connWrap = pool.getConnWrap();
				this.TimeOutWrap = connWrap;
			}
			if (connWrap.getAuthFlag() == true) {
			   AuthInfor = connWrap.getDigestInfor();
			 }
            /////end add by hudm
          }
          else
            client = null;
        }
       /*(connWrap != null)
       {
         SevereBuffer.append("!768!connWrap != null");
         if (connWrap.getUserfulFlag() == false || client.isConnected() == false) {
           //从连接池中删除该条连接
           SevereBuffer.append("!771!connWrap.getUserfulFlag() == false || client.isConnected() == false");
           pool.deleteURL(connWrap);
           ////////
           connWrap = pool.getConnWrap();
           this.TimeOutWrap = connWrap;
           ///////
           //return null;
         }
         if (connWrap.getAuthFlag() == true) {
           AuthInfor = connWrap.getDigestInfor();
         }
       }
       else
       {
         SevereBuffer.append("!785!connWrap==null");
         return null;
       }*/
      }
      else {
        String MMSCIP = (String)mm7Config.getMMSCIP().get(0);
        int index = MMSCIP.indexOf(":");
        String ip;
        int port;
        if(index == -1)
        {
          ip = MMSCIP;
          port = 80;
        }
        else
        {
          ip = MMSCIP.substring(0,index);
          port = Integer.parseInt(MMSCIP.substring(index+1));
        }
        client = new Socket(ip,port);
      }
      if(client != null){
        SevereBuffer.append("!829!client != null and equals"+client);
        sender = new BufferedOutputStream(client.getOutputStream());
        receiver = new BufferedInputStream(client.getInputStream());
        client.setSoTimeout(mm7Config.getTimeOut());
        client.setKeepAlive(true);

        sb = new StringBuffer();
        sb.append(beforAuth);
        sb.append(AuthInfor);
        sb.append(afterAuth);
        try {
          sendBaos = new ByteArrayOutputStream();
          SevereBuffer.append("!part of send message is:"+sb.toString()+"!\r\n");
          sendBaos.write(sb.toString().getBytes());
          sendBaos.write(bcontent);
          return sendBaos;
        }
        catch (IOException e) {
          //System.out.println("IOException!原因："+e);
          SevereBuffer.append("IOException=" + e + "\r\n");
          return null;
        }
      }
      else
      {
        SevereBuffer.append("!853!client == null");
        return null;
      }
    }catch(UnknownHostException uhe)
    {
      //System.out.println("UnknownHostExcepion!原因："+uhe);
      SevereBuffer.append("UnknownHostExcepion="+uhe+"\r\n");
      return null;
    }
    catch(SocketException se)
    {
      //System.out.println("SocketException!原因："+se);
      SevereBuffer.append("SocketException="+se+"\r\n");
      return null;
    }
    /**超时后，被捕获，认为该次发送失败，重新进行发送，当发送超过一定次数后，
     * 即认为整个发送失败。返回失败信息。
     * */
    catch(InterruptedIOException iioe)
    {
      this.TimeOutFlag = true;
      for(int j=0;j<mm7Config.getReSendCount();j++)
      {
        sendBaos = getSendMessage(bcontent);
        if(sendBaos != null)
          res = this.SendandReceiveMessage(sendBaos);
      }
      res.setStatusCode(-101);
      res.setStatusText("超时发送失败！原因："+iioe);
      SevereBuffer.append("[Comments={-101;超时发送失败！原因："+iioe+"}]\r\n");
      return null;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      res.setStatusCode(-100);
      res.setStatusText("系统错误！原因："+e);
      SevereBuffer.append("[Comments={-100;系统错误！原因："+e+"}]\r\n");
      return null;
    }
  }

  private MM7RSRes SendandReceiveMessage(ByteArrayOutputStream sendbaos)
  {
    try{
      /////////
      //SevereBuffer.append("sendbaos="+sendbaos.toString());
      ////////
      sender.write(sendbaos.toByteArray());
      sender.flush();
      if(this.receiveMessge())
      {
        res = parseXML();
        return res;
      }
      else
      {
        MM7RSErrorRes error = new MM7RSErrorRes();
        error.setStatusCode( -102);
        error.setStatusText("接收失败！");
        SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        SevereBuffer.append("[Comments={-102;"+error.getStatusText()+"}]");
        return error;
      }
    }catch(IOException ioe)
    {
      this.TimeOutFlag = true;
          sendBaos = getSendMessage(this.TimeOutbCount);
          if (sendBaos != null) {
            res = SendandReceiveMessage(sendBaos);
            if(res!=null)
            {
              this.TimeOutFlag = false;
              return res;
            }
            else{
              MM7RSErrorRes error = new MM7RSErrorRes();
              error.setStatusCode( -103);
              error.setStatusText("没有返回正确的消息");
              SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
              SevereBuffer.append("[Comments={-103;" + error.getStatusText() + "}]");
              return error;
            }
            /*if (res.getStatusCode() == MMConstants.RequestStatus.SUCCESS) {
              this.TimeOutFlag = false;
              return res;
            }
            else
            {
              MM7RSErrorRes error = new MM7RSErrorRes();
              error.setStatusCode( -110);
              error.setStatusText("原因:" + ioe);
              SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
              SevereBuffer.append("[Comments={-103;" + error.getStatusText() + "}]");
              return error;
            }*/
          }
          else
          {
            MM7RSErrorRes error = new MM7RSErrorRes();
            error.setStatusCode( -104);
            //error.setStatusText("Socket不通！原因:" + ioe);
			error.setStatusText(" " + ioe);
            SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
            SevereBuffer.append("[Comments={-104;" + error.getStatusText() + "}]");
            return error;
          }
    }
  }

  public boolean receiveMessge() throws IOException
  {
    try {
      baos.reset();
      boolean bReceive = false;
      byte[] data = new byte[MMConstants.ARRAYLIMIT];
      int word = -1;
      int readline = -1;
      int totalline = 0;
      boolean flag = false;
      boolean bHead = false;
      int readlineOut = 1;
      int envelopecount = 0;
      while (1 > 0) {
        if (flag)
          break;
          readline = receiver.read(data);
        if (readline != -1) {
          baos.write(data, 0, readline);
          //System.out.println("baos==="+baos.toString());
          totalline = totalline + readline;
          if (baos.toString().indexOf("\r\n\r\n") < 0)
            continue;
          if (bHead == false) {
            int httpindex = baos.toString().indexOf("HTTP/1.1");
            if(httpindex != -1){
              String httpCode = baos.toString().substring(httpindex + 8,
                  httpindex + 12).trim();
              int httpsepindex = baos.toString().indexOf("\r\n\r\n");
              if (httpCode.equals("401")) {
                if (baos.toString().indexOf("Digest") != -1) {
                  if (ResendCount < mm7Config.getReSendCount()) {
                    ResendCount = ResendCount + 1;
                    pool.setInitNonceCount();
                    String clientAuthInfor = "";
                    String authInfor = "";
                    int authindex = baos.toString().indexOf(MMConstants.
                        AUTHENTICATION);
                    if (authindex > 0) {
                      int lineend = baos.toString().indexOf("\r", authindex + 1);
                      int linebeg = authindex +
                          MMConstants.AUTHENTICATION.length() +
                          1;
                      authInfor = baos.toString().substring(linebeg, lineend);
                      clientAuthInfor = setDigestAuth(authInfor);
                    }
                    int connectionindex = baos.toString().toLowerCase().indexOf(
                        MMConstants.CONNECTION);
                    int connlength = connectionindex +
                        MMConstants.CONNECTION.length() + 1;
                    int connectionend = baos.toString().indexOf("\r\n",
                        connectionindex);
                    String ConnectionFlag = "";
                    if (connectionindex != -1 && connectionend != -1)
                      ConnectionFlag = baos.toString().substring(connlength,
                          connectionend);
                    sb = new StringBuffer();
                    sb.append(beforAuth);
                    sb.append(clientAuthInfor);
                    sb.append(afterAuth);
                    sb.append(entityBody);
                    sender.write(sb.toString().getBytes());
                    sender.flush();
                    baos = new ByteArrayOutputStream();
                    data = new byte[MMConstants.ARRAYLIMIT];
                    int resline = -1;
                    int totalresline = 0;
                    boolean excuteFlag = false;
                    int httpseper = -1;
                    int contlen3 = -1;
                    while (1 > 0) {
                      resline = receiver.read(data);
                      if (resline == -1)
                        break;
                      baos.write(data, 0, resline);
                      totalresline += resline;
                      if (baos.toString().indexOf("\r\n\r\n") < 0)
                        continue;
                      if (excuteFlag == false) {
                        httpindex = baos.toString().indexOf("HTTP/1.1");
                        httpCode = baos.toString().substring(httpindex + 8,
                            httpindex + 12).trim();
                        int conlen1 = baos.toString().toLowerCase().indexOf(
                            MMConstants.CONTENT_LENGTH);
                        int conlen2 = baos.toString().indexOf("\r", conlen1);
                        contlen3 = Integer.parseInt(baos.toString().substring(
                            (conlen1 + MMConstants.CONTENT_LENGTH.length() + 1),
                            conlen2).trim());
                        httpseper = baos.toString().indexOf("\r\n\r\n");
                        if (httpCode.equals("200")) {
                          //还要再加判断客户端是否是长连接
                          ResendCount = 0;
                          excuteFlag = true;
                          if (ConnectionFlag.trim().toLowerCase().equals(
                              "keep-alive")) {
                            pool.setNonceCount(Integer.toString(Integer.
                                parseInt(
                                pool.
                                getNonceCount(), 8) + 1));
                            connWrap.setDigestInfor(setDigestAuth(authInfor));
                            continue;
                          }
                        }
                      }
                      //开始接收http体
                      if (totalresline == httpseper + contlen3 + 4) {
                      	/*
                      	if(this.TimeOutFlag==true){
                      		SevereBuffer.append("baos.tostring()=="+baos.toString());
                      	}*/
                        if (pool.getKeepAlive().equals("off")) {
                          sender.close();
                          receiver.close();
                          client.close();
                        }
                        else {
                          connWrap.setUserfulFlag(true);
                          connWrap.setFree(true);
                        }
                        flag = true;
                        bReceive = true;
                        break;
                      }
                    }
                  }
                  else if (baos.toString().indexOf("Basic") != -1) {
                    if (ResendCount < mm7Config.getReSendCount()) {
                      ResendCount = ResendCount + 1;
                      receiveMessge();
                    }
                    else {
                      bReceive = false;
                      break;
                    }
                  }
                }
                else {
                  bReceive = false;
                  break;
                }
              }
              else {
                int index1 = baos.toString().toLowerCase().indexOf(MMConstants.
                    CONTENT_LENGTH);
                int index2 = baos.toString().indexOf("\r", index1);
                int contlength = 0;
                if (index1 == -1 || index2 == -1) {
                  int encodingindex = baos.toString().toLowerCase().indexOf(
                      "transfer-encoding:");
                  if (encodingindex >= 0) {
                    int encodingend = baos.toString().indexOf("\r\n",
                        encodingindex);
                    if (encodingend >= 0) {
                      String strenc = baos.toString().substring(encodingindex +
                          "transfer-encoding:".length(), encodingend).trim();
                      if (strenc.equalsIgnoreCase("chunked")) {
                        ////在这里可以增加chunked的处理

                        int endencindex2 = baos.toString().indexOf("\r\n",
                            encodingend + 1);
                        if (endencindex2 >= 0) {
                          int xmlbeg = baos.toString().indexOf(MMConstants.
                              BEGINXMLFLAG, endencindex2 + 1);
                          if (xmlbeg > 0) {
                            String strTempContLen = baos.toString().substring(
                                endencindex2, xmlbeg).trim();
                            contlength = Integer.parseInt(strTempContLen, 16);
                          }
                        }
                      }
                      else {
                        bReceive = false;
                        break;
                      }
                    }
                    else {
                      bReceive = false;
                      break;
                    }
                  }
                  else {
                    continue;
                  }

                  if (totalline >= httpsepindex + contlength + 8) {
                  	SevereBuffer.append("receive end");
                  	SevereBuffer.append("baos.toString()=="+baos.toString());
                    if (pool.getKeepAlive().equals("off")) {
                      sender.close();
                      receiver.close();
                      client.close();
                    }
                    else {
                      connWrap.setUserfulFlag(true);
                      connWrap.setFree(true);
                    }
                    bReceive = true;
                    break;
                  }

                }
                else {
                  contlength = Integer.parseInt(baos.toString().substring(
                      (index1 + MMConstants.CONTENT_LENGTH.length() + 1),
                      index2).
                                                trim());
                  //开始接收http体
                  if (totalline == httpsepindex + contlength + 4) {
                    //System.out.println("正常接收结束");
                    //add by hudm to test 104 problem 2004-06-09
                    if(this.TimeOutFlag==true)
                      SevereBuffer.append("baos.tostring()=="+baos.toString());
                    //end add by hudm
                    if (pool.getKeepAlive().equals("off")) {
                      sender.close();
                      receiver.close();
                      client.close();
                    }
                    else {
                      connWrap.setUserfulFlag(true);
                      connWrap.setFree(true);
                    }
                    bReceive = true;
                    break;
                  }
                }
              }
            }
            else{
              bReceive = false;
              break;
            }

          }
        }
        /*else
        {
          //System.out.println("第1083行。");
          this.TimeOutFlag = true;
          sendBaos = getSendMessage(this.TimeOutbCount);
          if (sendBaos != null) {
            //System.out.println("第1087行。");
            res = SendandReceiveMessage(sendBaos);
            if(res != null)
            {
              this.TimeOutFlag = false;
              return true;
            }
         }
         else
           return false;
        }*/
      }
      return bReceive;
    }
   catch (SocketTimeoutException ste) {
     //System.out.println("第1100行。");
     this.TimeOutFlag = true;
      ReadTimeOutCount++;
    if(ReadTimeOutCount< mm7Config.getReSendCount()) {
       sendBaos = getSendMessage(this.TimeOutbCount);
       if(sendBaos != null)
       {
         res = SendandReceiveMessage(sendBaos);
         if(res != null){
           this.TimeOutFlag = false;
           return true;
         }
         /*if (res.getStatusCode() == MMConstants.RequestStatus.SUCCESS) {
           this.TimeOutFlag = false;
           return true;
         }*/
       }
       else
         return false;
     }
     this.TimeOutFlag = false;
     res.setStatusCode( -101);
     res.setStatusText("超时发送失败！");
     SevereBuffer.append("[Comments={-101;"+res.getStatusText()+"}]");
     ReadTimeOutCount = 0;
     return false;
   }
  }


  /**输入string，输出经过MD5编码的String*/
  public String calcMD5(String str) {
    try {
      MessageDigest alga = MessageDigest.getInstance("MD5");
      alga.update(str.getBytes());
      byte[] digesta = alga.digest();
      return byte2hex(digesta);
    }
    catch (NoSuchAlgorithmException ex) {
      System.out.println("出错了！！没有这种算法！");
    }
    return "NULL";
  }

  //byte[]数组转成字符串
  public String byte2hex(byte[] b) {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = (Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else
        hs = hs + stmp;
      if (n < b.length - 1)
        hs = hs + "";
    }
    return hs;
  }

  private String setDigestAuth(String authinfor)
  {
    String auth = authinfor +"\r\n";
    int equal = -1;
    int comma = -1;
    StringBuffer authBuffer = new StringBuffer();
    authBuffer.append("Authorization: Digest ");
    String qopValue;
    String MD5A2;
    String algValue;
    String cnonceValue= "";
    String ncValue;
    String responseValue;
    String uri = mm7Config.getMMSCURL();
    if(uri == null)
        uri = "";
    String username = mm7Config.getUserName();
    authBuffer.append("uri=\""+uri+"\",");
    authBuffer.append("username=\""+username+"\",");

    String passwd = mm7Config.getPassword();
    // 获得realm的值
    int realmindex = auth.indexOf(MMConstants.REALM);
    equal = auth.indexOf("=", realmindex + 1);
    comma = auth.indexOf("\"", equal+2);
    String realmValue = auth.substring(equal+1,comma);
    if(realmValue.startsWith("\""))
      realmValue = realmValue.substring(1,realmValue.length());
    authBuffer.append("realm=\""+realmValue+"\",");
    String MD5A1;
    //取得nonce的值
    int nonceindex = auth.indexOf(MMConstants.NONCE);
    equal = auth.indexOf("=",nonceindex+1);
    comma = auth.indexOf("\"",equal+2);
    String nonceValue = auth.substring(equal+1,comma);
    if(nonceValue.startsWith("\""))
      nonceValue = nonceValue.substring(1,nonceValue.length());
    authBuffer.append("nonce=\""+nonceValue+"\",");
    //判断有没有opaque，若有，则原封不动地返回给服务器
    int opaqueindex = auth.indexOf(MMConstants.OPAQUE);
    if(opaqueindex >0)
    {
      equal = auth.indexOf("=",opaqueindex+1);
      comma = auth.indexOf("\"",equal+2);
      authBuffer.append("opaque="+auth.substring(equal+1,comma+1));
    }
    //取得algorithm的值
    int algindex = auth.indexOf(MMConstants.ALGORITHM);
    if(algindex>0)
    {
      equal = auth.indexOf("=", algindex);
      comma = auth.indexOf(",", equal+2);
      if(comma >=0)
      {
        algValue = auth.substring(equal + 1, comma);
        if (algValue.startsWith("\""))
          algValue = algValue.substring(1, algValue.length() - 1);
      }
      else
      {
        comma = auth.indexOf("/r",equal);
        algValue = auth.substring(equal + 1, comma);
        if (algValue.startsWith("\""))
          algValue = algValue.substring(1, algValue.length());
      }
    }
    else
    {
      algValue = "MD5";
    }
    //取得qop的值
    int qopindex = auth.indexOf(MMConstants.QOP);
    if(algValue.equals("MD5") ||algValue.equals("MD5-sess"))
    {
      MD5A1 = calcMD5(username + ":" + realmValue + ":" + passwd);
      //服务器存在qop这个字段
      if (qopindex > 0) {
        equal = auth.indexOf("=", qopindex);
        comma = auth.indexOf("\"", equal+2);
        qopValue = auth.substring(equal + 1, comma);
        if (qopValue.startsWith("\""))
          qopValue = qopValue.substring(1, qopValue.length());
          /**表明服务器给出了两种qop，为auth和auth-int，
           * 这是应该是客户端自己选择采用哪种方式进行鉴权
           * */
        if (qopValue.indexOf(",") > 0) {
          if (mm7Config.getDigest().equals("auth-int")) {
            MD5A2 = calcMD5("POST" + ":" + uri + ":" +
                            calcMD5(entityBody.toString()));
          }
          else {
            MD5A2 = calcMD5("POST" + ":" + uri);
          }
          ncValue = String.valueOf(pool.getNonceCount());
          cnonceValue = getBASE64(ncValue);
          responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + ncValue +
                                  ":" + cnonceValue + ":" + qopValue + ":" + MD5A2);
          authBuffer.append("qop=\""+mm7Config.getDigest()+"\",");
          authBuffer.append("nc="+ncValue+",");
          authBuffer.append("cnonce=\""+cnonceValue+"\",");
          authBuffer.append("response=\""+responseValue+"\",");
        }
        //服务器端只有一种qop方式。
        else
        {
          if (qopValue.equals("auth-int")) {
            MD5A2 = calcMD5("POST" + ":" + uri + ":" +
                            calcMD5(entityBody.toString()));
          }
          else
            MD5A2 = calcMD5("POST" + ":" + uri);
          ncValue = String.valueOf(pool.getNonceCount());
          cnonceValue = getBASE64(ncValue);
          responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + ncValue +
                                  ":" + cnonceValue + ":" + qopValue + ":" + MD5A2);
          authBuffer.append("qop=\""+qopValue+"\",");
          authBuffer.append("nc="+ncValue+",");
          authBuffer.append("cnonce=\""+cnonceValue+"\",");
          authBuffer.append("response=\""+responseValue+"\",");
        }
      }
      //服务器端不存在对qop的方式的选择
      else {
        MD5A2 = calcMD5("POST" + ":" + uri);
        responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + MD5A2);
        authBuffer.append("response=\""+responseValue+"\",");
      }
    }
    //去掉最后一个逗号
    int lastcommaindex = authBuffer.lastIndexOf(",");
    authBuffer.delete(lastcommaindex,lastcommaindex+1);
    authBuffer.append("\r\n");
    return authBuffer.toString();
  }
}