/**File Name:MM7ReceiverServlet.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-17
 * */

package com.cmcc.mm7.vasp.service;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.*;
import java.util.*;
import sun.misc.*;
import java.security.*;
import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.conf.*;
import com.cmcc.mm7.vasp.common.*;

public class MM7ReceiveServlet extends HttpServlet implements MM7AbstractReceiver
{
  protected MM7Config Config = null;
  private String logFileName;
  private String strEnvelope;
  private int N;
  private DecimalFormat df;
  private ByteArrayOutputStream Finerbaos;
  private StringBuffer TempBuffer;
  private StringBuffer SevereBuffer;
  private StringBuffer InfoBuffer;
  private StringBuffer FinerBuffer;
  private SimpleDateFormat sdf;
  private SimpleDateFormat Recordsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private long LogTimeBZ;
  private long SameMinuteTime;
  private int SameMMSCID;
  private String MMSCID;

  private void reset()
  {
    Finerbaos = new ByteArrayOutputStream();
    TempBuffer = new StringBuffer();
    SevereBuffer = new StringBuffer();
    InfoBuffer = new StringBuffer();
    FinerBuffer = new StringBuffer();
    LogTimeBZ = System.currentTimeMillis();
    SameMinuteTime = System.currentTimeMillis();
    sdf = new SimpleDateFormat("yyyyMMddHHmm");
    df = new DecimalFormat("0000");
    N = 0;
    SameMMSCID = 0;
    logFileName = "";
    strEnvelope = "";
    Recordsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  public MM7ReceiveServlet()  //构造方法
  {
    reset();
  }

  //处理到VASP的传送（deliver）多媒体消息
  public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq) {
    MM7DeliverRes res = new MM7DeliverRes();
    res.setTransactionID(mm7DeliverReq.getTransactionID());
    res.setStatusCode(1000);
    return res;
  }

  //处理到VASP的发送报告
  public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq
                                               mm7DeliveryReportReq) {
    MM7DeliveryReportRes res = new MM7DeliveryReportRes();
    return res;
  }

  //处理到VASP的读后回复报告
  public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq) {
    MM7ReadReplyRes res = new MM7ReadReplyRes();
    return res;
  }

  private void WriteLog(String MMSCID)
  {
    int LogLevel = Config.getLogLevel();
    String LogPath = Config.getLogPath();
    int LogNum = Config.getLogNum();
    int LogInterval = Config.getLogInterval();
    int LogSize = Config.getLogSize();
    FileOutputStream fos = null;
    SimpleDateFormat simpledf = new SimpleDateFormat("yyyyMMddHHmm");
    String temptime = simpledf.format(new Date(System.currentTimeMillis()));
    long timeNow = 0;
    try{
      timeNow = simpledf.parse(temptime).getTime();
    }catch(ParseException pe){
      pe.printStackTrace();
    }
    if(logFileName.length() > 0)
    {
      File logFile = new File(logFileName);
      int index1 = logFileName.indexOf(MMSCID+"_");
      int index11 = index1 + MMSCID.length()+1;
      int index2 = logFileName.indexOf(".",index11+1);
      String strtimeFile = logFileName.substring(index11,index2);
      long timeFile = 0;
      try{
        timeFile = simpledf.parse(strtimeFile).getTime();
      }catch(ParseException pe){
        pe.printStackTrace();
      }
      if((timeNow-timeFile) > (long)LogInterval * 60 * 1000)
      {
        N = 1;
        this.deleteFile(LogPath, LogNum,MMSCID);
        logFileName = LogPath + "/" + MMSCID + "_" + temptime + "." +df.format(N) + ".log";
        try{
          fos = new FileOutputStream(logFileName);
        }catch(FileNotFoundException fnfe){
          fnfe.printStackTrace();
        }
      }
      else
      {
        if (logFile.length() >= LogSize * 1024) {
          if (N < LogNum)
            N++;
          else
            N = 1;
          this.deleteFile(LogPath, LogNum, MMSCID);
          logFileName = LogPath + "/" + MMSCID + "_" + temptime + "." +
              df.format(N) + ".log";
          try {
            fos = new FileOutputStream(logFileName);
          }
          catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
          }
        }
        else {
          this.deleteFile(LogPath, LogNum, MMSCID);
          try {
            fos = new FileOutputStream(logFileName, true);
          }
          catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
          }
        }
      }
    }
    else
    {
      N = 1;
      this.deleteFile(LogPath, LogNum,MMSCID);
      logFileName = LogPath + "/" + MMSCID + "_" + temptime + "." +df.format(N) + ".log";
      try{
        File logFile = new File(logFileName);
        fos = new FileOutputStream(logFile);
      }catch(FileNotFoundException fnfe){
        fnfe.printStackTrace();
      }
    }
    try{
      switch (LogLevel) {
        case 1: {
          if(SevereBuffer.length() > 0)
            fos.write(SevereBuffer.toString().getBytes());
          fos.close();
          SevereBuffer = new StringBuffer();
          break;
        }
        case 2:
          break;
        case 3: {
          if(SevereBuffer.length() > 0)
            fos.write(SevereBuffer.toString().getBytes());
          if(InfoBuffer.length() > 0)
            fos.write(InfoBuffer.toString().getBytes());
          fos.close();
          SevereBuffer = new StringBuffer();
          InfoBuffer = new StringBuffer();
          break;
        }
        case 4:
          break;
        case 5:
          break;
        case 6: {
          if(SevereBuffer.length() > 0)
            fos.write(SevereBuffer.toString().getBytes());
          if(InfoBuffer.length() > 0)
            fos.write(InfoBuffer.toString().getBytes());
          if(Finerbaos.size() > 0)
            fos.write(Finerbaos.toByteArray());
          fos.close();
          SevereBuffer = new StringBuffer();
          InfoBuffer = new StringBuffer();
          Finerbaos = new ByteArrayOutputStream();
          break;
        }
        case 7:
          break;
        default:
          break;
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
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


  protected void service(HttpServletRequest req, HttpServletResponse rsp) throws
      IOException, ServletException
  {
    ServletInputStream in = req.getInputStream();
    int length = 0;
    String sLength = null;
    String sContentType = null;
    SevereBuffer = new StringBuffer();
    InfoBuffer = new StringBuffer();
    FinerBuffer = new StringBuffer();
    Finerbaos = new ByteArrayOutputStream();

    if(Config == null)
    {
      //System.out.println("读取默认配置文件失败");
      SevereBuffer.append("read Config file failure");
      return;
    }


    if((sLength = req.getHeader("content-length")) == null)
    {
      rsp.sendError(404);
      return;
    }
    length = Integer.parseInt(sLength);

    if((sContentType = req.getHeader("Content-Type")) == null)
    {
      rsp.sendError(404);
      return;
    }
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] b = new byte[1024];
      int readlen = 0;
      int totallen = 0;
      int nc = 1;

      baos.write(sContentType.getBytes());
      baos.write("\r\n".getBytes());
      while(totallen < length)   //word = in.read()
      {
        //System.out.println("开始");
        readlen = in.read(b);
        baos.write(b,0,readlen);
        totallen = totallen + readlen;

        //System.out.println("totallen="+totallen);
      }
   //   System.out.println("-----receive http body-----");
   SevereBuffer.append("-----receive http body-----");
//      System.out.println(baos.toString());

      if (Config.getAuthenticationMode() == 1)
      {
        if (basicAuth(rsp, baos) == false)
        {
          return;
        }
      }
      else if (Config.getAuthenticationMode() == 2)
      {
        if (digestAuth(rsp, baos, nc) == false)
        {
          return;
        }
      }

      SOAPDecoder soapDecoder = null;
      soapDecoder = new SOAPDecoder();
      soapDecoder.setMessage(baos);
      try {
        soapDecoder.decodeMessage();
        MM7RSReq rsReq = soapDecoder.getMessage();
        if (rsReq == null) {
          rsp.sendError(404);
          return;
        }
        if (soapDecoder.getMessageName().equals(MMConstants.DELIVERREQ)) {
          //System.out.println("DeliverReq");
          MM7DeliverReq deliverReq = (MM7DeliverReq) rsReq;
          InfoBuffer.append("\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][3]");
          TempBuffer.append("[TransactionID="+deliverReq.getTransactionID()+"]");
          TempBuffer.append("[Message_Type=MM7DeliverReq]");
          TempBuffer.append("[Sender_Address="+deliverReq.getSender()+"]");
          TempBuffer.append("[Recipient_Address={");
          if (deliverReq.isToExist()) {
            TempBuffer.append("To={");
            List to = new ArrayList();
            to = deliverReq.getTo();
            for (int i = 0; i < to.size(); i++)
            TempBuffer.append( (String) to.get(i) + ",");
            TempBuffer.append("}");
          }
          if (deliverReq.isCcExist()) {
            TempBuffer.append("Cc={");
            List cc = new ArrayList();
            cc = deliverReq.getCc();
            for (int i = 0; i < cc.size(); i++)
              TempBuffer.append( (String) cc.get(i) + ",");
            TempBuffer.append("}");
          }
          if (deliverReq.isBccExist()) {
            TempBuffer.append("Bcc={");
            List bcc = new ArrayList();
            bcc = deliverReq.getBcc();
            for (int i = 0; i < bcc.size(); i++)
              TempBuffer.append( (String) bcc.get(i) + ",");
            TempBuffer.append("}");
          }
          TempBuffer.append("}]");
          InfoBuffer.append(TempBuffer);
          InfoBuffer.append("\r\n"+soapDecoder.getEnvelope());  //Envelope消息
          String timelevel = "\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][6]";
          Finerbaos.write(timelevel.getBytes());
          Finerbaos.write(TempBuffer.toString().getBytes());
          Finerbaos.write("\r\n".getBytes());
          Finerbaos.write(soapDecoder.getSoapStream().toByteArray());
          TempBuffer = new StringBuffer();

          MM7DeliverRes deliverRes = (MM7DeliverRes) doDeliver(deliverReq);
          InfoBuffer.append("\r\n\r\n"+"["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"]"+"[3]");
          String tempres = "\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][6]";
          Finerbaos.write(tempres.getBytes());
          TempBuffer.append("[Message_Type=MM7DeliverRes]");
          TempBuffer.append("[Comments={" + deliverRes.getStatusCode() +
                    ";"+deliverRes.getStatusText()+"}]\r\n");
          InfoBuffer.append(TempBuffer);
          Finerbaos.write(TempBuffer.toString().getBytes());
          TempBuffer = new StringBuffer();
          Finerbaos.write("\r\n".getBytes());
          send(rsp, deliverRes);
          //System.out.println("发送DeliverRes完毕");
          SevereBuffer.append("发送DeliverRes完毕");
        }
        else if (soapDecoder.getMessageName().equals(MMConstants.
            DELIVERYREPORTREQ)) {
          //System.out.println("DeliverReportReq");
          MM7DeliveryReportReq deliverReportReq = (MM7DeliveryReportReq) rsReq;
          InfoBuffer.append("\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][3]");
          TempBuffer.append("[TransactionID="+deliverReportReq.getTransactionID()+"]");
          TempBuffer.append("[Message_Type=MM7DeliveryReportReq]");
          TempBuffer.append("[Sender_Address="+deliverReportReq.getSender()+"]");
          InfoBuffer.append(TempBuffer);
          InfoBuffer.append("\r\n"+soapDecoder.getEnvelope());  //Envelope消息
          String timelevel = "\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][6]";
          Finerbaos.write(timelevel.getBytes());
          Finerbaos.write(TempBuffer.toString().getBytes());
          Finerbaos.write("\r\n".getBytes());
          Finerbaos.write(soapDecoder.getSoapStream().toByteArray());
          TempBuffer = new StringBuffer();

          MM7DeliveryReportRes deliverReportRes = (MM7DeliveryReportRes)
              doDeliveryReport(deliverReportReq);
          InfoBuffer.append("\r\n\r\n"+"["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"]"+"[3]");
          String tempres = "\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][6]";
          Finerbaos.write(tempres.getBytes());
          TempBuffer.append("[Message_Type=MM7DeliverRes]");
          TempBuffer.append("[Comments={" + deliverReportRes.getStatusCode() +
                    ";"+deliverReportRes.getStatusText()+"}]\r\n");
          InfoBuffer.append(TempBuffer);
          Finerbaos.write(TempBuffer.toString().getBytes());
          TempBuffer = new StringBuffer();
          Finerbaos.write("\r\n".getBytes());

          send(rsp, deliverReportRes);
          //System.out.println("发送deliveryReportRes完毕");
          SevereBuffer.append("发送deliveryReportRes完毕");
        }
        else if (soapDecoder.getMessageName().equals(MMConstants.READREPLYREQ)) {
          MM7ReadReplyReq readReplyReq = (MM7ReadReplyReq) rsReq;
          InfoBuffer.append("\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][3]");
          TempBuffer.append("[TransactionID="+readReplyReq.getTransactionID()+"]");
          TempBuffer.append("[Message_Type=MM7ReadReplyReq]");
          TempBuffer.append("[Sender_Address="+readReplyReq.getSender()+"]");
          InfoBuffer.append(TempBuffer);
          InfoBuffer.append("\r\n"+soapDecoder.getEnvelope());  //Envelope消息
          String timelevel = "\r\n\r\n["+Recordsdf.format(new Date(
              System.currentTimeMillis()))+"][6]";
          Finerbaos.write(timelevel.getBytes());
          Finerbaos.write(TempBuffer.toString().getBytes());
          Finerbaos.write("\r\n".getBytes());
          Finerbaos.write(soapDecoder.getSoapStream().toByteArray());
          TempBuffer = new StringBuffer();

          MM7ReadReplyRes readReplyRes = (MM7ReadReplyRes) doReadReply(
              readReplyReq);
          InfoBuffer.append("\r\n\r\n"+"["+Recordsdf.format(new Date(
             System.currentTimeMillis()))+"]"+"[3]");
         String tempres = "\r\n\r\n[" + Recordsdf.format(new Date(
             System.currentTimeMillis())) + "][6]";
         Finerbaos.write(tempres.getBytes());
         TempBuffer.append("[Message_Type=MM7DeliverRes]");
         TempBuffer.append("[Comments={" + readReplyRes.getStatusCode() +
                           ";" + readReplyRes.getStatusText() + "}]\r\n");
         InfoBuffer.append(TempBuffer);
         Finerbaos.write(TempBuffer.toString().getBytes());
         TempBuffer = new StringBuffer();
         Finerbaos.write("\r\n".getBytes());
         send(rsp, readReplyRes);
        }
        InfoBuffer.append(strEnvelope);
        Finerbaos.write(strEnvelope.getBytes());
        String time = "[" + Recordsdf.format(new Date(System.currentTimeMillis())) + "]";
        SevereBuffer.insert(0, "\r\n\r\n" +time + "[1]");  //
        MMSCID = Config.getMmscId();
        if(Config.getLogLevel()>0)
          WriteLog(MMSCID);
      }
      catch (SOAPDecodeException e) {
        System.err.println(e);
      }
    }
    catch (IOException ioe) {
      //System.out.println("Error in SimpleWebServer: " + ioe);
      SevereBuffer.append("Error in SimpleWebServer: " + ioe);
    }
  }

  public void send(HttpServletResponse rsp,MM7VASPRes mm7VASPRes)
  {
    StringBuffer sb = new StringBuffer();
    StringBuffer entityBody = new StringBuffer();
    ServletOutputStream sender = null;
    try
    {
      sender = rsp.getOutputStream();
      //rsp.addHeader("Connection","Keep-Alive");
      rsp.addHeader("Content-Type","text/xml;charset=\""+Config.getCharSet()+"\"");
//      rsp.addHeader("Content-Type","text/xml;charset=\"US-ASCII\"");
      //设置鉴权
      byte[] bcontent = getContent(mm7VASPRes);
      //设置消息体长度
      rsp.addHeader("Content-Length",Integer.toString(bcontent.length));
      //rsp.addHeader("Mime-Version","1.0");
      //rsp.addHeader("SOAPAction","");
      entityBody.append(new String(bcontent));
      sb.append(entityBody);
//      System.out.println("**********send res *********");
//      System.out.println(sb.toString());
      sender.write(sb.toString().getBytes());
      sender.flush();
      SevereBuffer.append("end sending ack!\r\n");
      return;
    }
    catch(InterruptedIOException iioe)
    {
      return;
    }
    catch(Exception e){
      return;
    }
  }

  private byte[] getContent(MM7VASPRes mm7VASPRes)
  {
    byte[] b = null;

    b = encodeMessage(mm7VASPRes);
    String strMessage = new String(b);
    int index = strMessage.indexOf(MMConstants.BEGINXMLFLAG);
    strEnvelope = strMessage.substring(index);
    return b;
  }

  private byte[] encodeMessage(MM7VASPRes mm7VASPRes)
  {
    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    StringBuffer sb = new StringBuffer();

    sb.append("<?xml version=\"1.0\"?><env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"><env:Header>");//
    if(mm7VASPRes.isTransactionIDExist())  //
      sb.append("<mm7:TransactionID xmlns:mm7=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">"+mm7VASPRes.getTransactionID()+"</mm7:TransactionID>");
    else
      System.err.println("TransactionID 不许为空！");
    sb.append("</env:Header><env:Body>");
    /*if(mm7VaspReq.isMM7VersionExist())
      sb.append("<MM7Version>"+mm7VaspReq.getMM7Version()+"</MM7Version>");*/

    /**
     * 若发送的消息为MM7SubmitReq
     * */
    if(mm7VASPRes instanceof MM7DeliverRes){
      MM7DeliverRes deliverRes = (MM7DeliverRes) mm7VASPRes;
      sb.append("<DeliverRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
      if(deliverRes.isMM7VersionExist()){
        sb.append("<MM7Version>"+deliverRes.getMM7Version()+"</MM7Version>");
      }else{
        System.err.println("MM7Version 不许为空！");
      }

      if(deliverRes.isServiceCodeExist()){
         sb.append("<ServiceCode>" + deliverRes.getServiceCode() + "</ServiceCode>");
      }

      sb.append("<Status>");
      if(deliverRes.isStatusCodeExist()){
        sb.append("<StatusCode>" + deliverRes.getStatusCode() + "</StatusCode>");
      }else{
        System.err.println("StatusCode 不许为空");
      }

      if(deliverRes.isStatusTextExist()){
        sb.append("<StatusText>" + deliverRes.getStatusText() + "</StatusText>");
      }
      if(deliverRes.isStatusDetailExist()){
        sb.append("<Details>" + deliverRes.getStatusDetail() + "</Details>");
      }
      sb.append("</Status>");

      sb.append("</DeliverRsp>");
      sb.append("</env:Body></env:Envelope>");
      sb.append("\r\n");
    }else if(mm7VASPRes instanceof MM7DeliveryReportRes){
      MM7DeliveryReportRes deliveryReportRes = (MM7DeliveryReportRes)mm7VASPRes;
      sb.append("<DeliveryReportRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
      if(deliveryReportRes.isMM7VersionExist()){
        sb.append("<MM7Version>"+deliveryReportRes.getMM7Version()+"</MM7Version>");
      }else{
        System.err.println("MM7Version 不许为空！");
      }

      sb.append("<Status>");
      if(deliveryReportRes.isStatusCodeExist()){
        sb.append("<StatusCode>" + deliveryReportRes.getStatusCode() + "</StatusCode>");
      }else{
        System.err.println("StatusCode 不许为空");
      }

      if(deliveryReportRes.isStatusTextExist()){
        sb.append("<StatusText>" + deliveryReportRes.getStatusText() + "</StatusText>");
      }
      if(deliveryReportRes.isStatusDetailExist()){
        sb.append("<Details>" + deliveryReportRes.getStatusDetail() + "</Details>");
      }
      sb.append("</Status>");

      sb.append("</DeliveryReportRsp>");
      sb.append("</env:Body></env:Envelope>");
      sb.append("\r\n");
    }else if(mm7VASPRes instanceof MM7ReadReplyRes){
      MM7ReadReplyRes readReplyRes = (MM7ReadReplyRes) mm7VASPRes;
      sb.append("<ReadReplyRsp xmlns=\"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0\">");
      if(readReplyRes.isMM7VersionExist()){
        sb.append("<MM7Version>"+readReplyRes.getMM7Version()+"</MM7Version>");
      }else{
        System.err.println("MM7Version 不许为空！");
      }

      sb.append("<Status>");
      if(readReplyRes.isStatusCodeExist()){
        sb.append("<StatusCode>" + readReplyRes.getStatusCode() + "</StatusCode>");
      }else{
        System.err.println("StatusCode 不许为空");
      }

      if(readReplyRes.isStatusTextExist()){
        sb.append("<StatusText>" + readReplyRes.getStatusText() + "</StatusText>");
      }
      if(readReplyRes.isStatusDetailExist()){
        sb.append("<Details>" + readReplyRes.getStatusDetail() + "</Details>");
      }
      sb.append("</Status>");

      sb.append("</ReadReplyRsp>");
      sb.append("</env:Body></env:Envelope>");
      sb.append("\r\n");
    }
    try{
      byteOutput.write(sb.toString().getBytes());
    }catch(Exception e){
      System.err.println(e);
    }

    return byteOutput.toByteArray();
  }

  private boolean basicAuth(HttpServletResponse rsp,ByteArrayOutputStream baos)
  {
    int index1 = 0,index2 = 0,index3 = 0;
    String auth = "Authorization";
    String basic,temp;

    index1 = baos.toString().indexOf(auth);
    index2 = baos.toString().indexOf("\r\n",index1);
    int index = index1+auth.length()+1;
    if(index1 == -1 || index2 == -1)
    {
      sendBasicReq(rsp);
      return false;
    }
    basic = baos.toString().substring(index,index2);

    if(basic.indexOf("Basic") == -1)
    {
      sendBasicReq(rsp);
      return false;
    }
    if(basic.indexOf(getBASE64(Config.getUserName() + ":" + Config.getPassword())) == -1)
    {
      sendBasicReq(rsp);
      return false;
    }

    return true;
  }

  private void sendBasicReq(HttpServletResponse rsp)
  {
    StringBuffer sb = new StringBuffer();
    ServletOutputStream sender = null;

    rsp.setStatus(401,"Authorization Required");
    rsp.addHeader("WWW-Authenticate","Basic realm=\"" + MMConstants.REALM + "\"");

    try
    {
      sender = rsp.getOutputStream();
      sender.write("".getBytes());
      sender.flush();
    }
    catch(Exception e)
    {
      System.err.println(e);
    }
  }

  private boolean digestAuth(HttpServletResponse rsp,ByteArrayOutputStream baos,int nc)
  {
    int index1 = 0,index2 = 0;
    String digest,temp;
    String username,realm,qop,nonce,ncValue,cnonce,response,uri;
    String MD5A1,MD5A2;
    index1 = baos.toString().indexOf("Authorization");
    index2 = baos.toString().indexOf("\r\n",index1);
    if(index1 == -1 || index2 == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1+"Authorization".length()+1;
    digest = baos.toString().substring(index1,index2);

    if(digest.indexOf("Digest") == -1)
    {
      sendDigestReq(rsp);
      return false;
    }

    /*校验username*/
    if((index1 = digest.indexOf("username=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("username=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    username = digest.substring(index1,index2);
    if(!username.equals(Config.getUserName()))
    {
      sendDigestReq(rsp);
      return false;
    }

    /*校验realm*/
    if((index1 = digest.indexOf("realm=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("realm=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    realm = digest.substring(index1,index2);
    if(!realm.equals(MMConstants.REALM))
    {
      //System.out.println("realm错误");
      sendDigestReq(rsp);
      return false;
    }

    /*校验qop*/
    if((index1 = digest.indexOf("qop=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("qop=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    qop = digest.substring(index1,index2);
    if(!qop.equals("auth"))
    {
      sendDigestReq(rsp);
      return false;
    }

    /*取得uri*/
    if((index1 = digest.indexOf("uri=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("uri=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    uri = digest.substring(index1,index2);
    /*校验nonce*/
    if((index1 = digest.indexOf("nonce=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("nonce=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    nonce = digest.substring(index1,index2);
    if(!nonce.equals(getBASE64("--NextPart_0_2817_24856")))
    {
      sendDigestReq(rsp);
      return false;
    }

    /*校验nc*/
    if((index1 = digest.indexOf("nc=")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("nc").length()+1;
    if((index2 = digest.indexOf(",",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    ncValue = digest.substring(index1,index2);
    if(Integer.parseInt(ncValue) != nc)
    {
      sendDigestReq(rsp);
      return false;
    }

    /*取得cnonce*/
    if((index1 = digest.indexOf("cnonce=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("cnonce=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    cnonce = digest.substring(index1,index2);
    /*校验response*/
    if((index1 = digest.indexOf("response=\"")) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    index1 = index1 + new String("response=\"").length();
    if((index2 = digest.indexOf("\"",index1)) == -1)
    {
      sendDigestReq(rsp);
      return false;
    }
    response = digest.substring(index1,index2).trim();
    MD5A1 = calcMD5(Config.getUserName() + ":" + MMConstants.REALM + ":" + Config.getPassword());
    MD5A2 = calcMD5("POST" + ":" + uri);
    temp = calcMD5(MD5A1 + ":" + nonce + ":" + ncValue + ":" + cnonce + ":" + qop + ":" + MD5A2);
    if(!temp.trim().equals(response))
    {
      sendDigestReq(rsp);
      return false;
    }

    return true;
  }


  private void sendDigestReq(HttpServletResponse rsp)
  {
    StringBuffer auth = new StringBuffer();
    ServletOutputStream sender = null;

    rsp.setStatus(401,"Authorization Required");
    auth.append("Digest ");
    auth.append("realm=\"" + MMConstants.REALM + "\", ");
    auth.append("nonce=\"" + getBASE64("--NextPart_0_2817_24856") + "\", ");
    auth.append("algorithm=MD5, qop=\"auth\"");
    rsp.setHeader("WWW-Authenticate:",auth.toString());

    try
    {
      sender = rsp.getOutputStream();
      sender.write("".getBytes());
      sender.flush();
    }
    catch(Exception e)
    {
      System.err.println(e);
    }
  }

  public static String getBASE64(String value)
  {
    if(value == null)
      return null;
    BASE64Encoder BaseEncode = new BASE64Encoder();
    return(BaseEncode.encode(value.getBytes()));
  }

  private String calcMD5(String str)
  {
    try {
      MessageDigest alga = MessageDigest.getInstance("MD5");
      alga.update(str.getBytes());
      byte[] digesta = alga.digest();
      return byte2hex(digesta);
    }
    catch (NoSuchAlgorithmException ex) {
      //System.out.println("出错了！！");
    }
    return "NULL";
  }

//byte[]数组转成字符串
  public String byte2hex(byte[] b)
  {
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
}
