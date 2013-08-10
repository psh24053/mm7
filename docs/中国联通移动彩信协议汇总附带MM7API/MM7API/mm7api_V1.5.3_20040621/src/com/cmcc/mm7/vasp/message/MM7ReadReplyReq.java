/**File Name:MM7ReadReplyReq.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-2
 * */

package com.cmcc.mm7.vasp.message;

import java.util.Date;

public class MM7ReadReplyReq extends MM7RSReq
{
  private String MMSReplayServerID;
  private boolean MMSReplayServerIDExist;
  private String MessageID;
  private boolean MessageIDExist;
  private String RecipientAddress;
  private boolean RecipientAddressExist;
  private String SenderAddress;
  private boolean SenderAddressExist;
  private Date TimeStamp;
  private boolean TimeStampExist;
  private byte MMStatus;
  private boolean MMStatusExist;
  private String StatusText;
  private boolean StatusTextExist;

  public void setMMSRelayServerID (String mmscID)  //设置MMSRelay/ServerRelay/Server的标识符
  {
    MMSReplayServerID = mmscID;
    MMSReplayServerIDExist = true;
  }
  public String getMMSRelayServerID ()  //获得MMSRelay/ServerRelay/Server的标识符
  {
    return(MMSReplayServerID);
  }
  public boolean isMMSRelayServerID ()  //是否存在MMSRelay/ServerRelay/Server的标识符
  {
    return(MMSReplayServerIDExist);
  }
  public void setMessageID(String messageID)  //设置messageID
  {
    MessageID = messageID;
    MessageIDExist = true;
  }
  public String getMessageID()  //获得messageID
  {
    return(MessageID);
  }
  public boolean isMessageIDExist()  //是否存在messageID
  {
    return(MessageIDExist);
  }
  public void setRecipient(String address)  //设置读后回复通报的状态对应的接收方地址
  {
    RecipientAddress = address;
    RecipientAddressExist = true;
  }
  public String getRecipient()  //获取读后回复所通报的状态对应的接收方地址
  {
    return(RecipientAddress);
  }
  public boolean isRecipientExist()  //是否存在接收方地址
  {
    return(RecipientAddressExist);
  }
  public void setSender(String senderAddress)  //设置MM始发方的地址
  {
    SenderAddress = senderAddress;
    SenderAddressExist = true;
  }
  public String getSender()  //获得MM始发方的地址
  {
    return(SenderAddress);
  }
  public boolean isSenderExist()  //是否存在MM始发方的地址
  {
    return(SenderAddressExist);
  }
  public void setTimeStamp(Date timeStamp)  //设置提交MM的时间和日期
  {
    TimeStamp = timeStamp;
    TimeStampExist = true;
  }
  public Date getTimeStamp ()  //获得提交MM的时间和日期
  {
    return(TimeStamp);
  }
  public boolean isTimeStampExist()  //是否存在提交MM的时间和日期
  {
    return(TimeStampExist);
  }
  public void setMMStatus(byte readStatus)  //设置MMStatus
  {
    MMStatus = readStatus;
    MMStatusExist = true;
  }
  public byte getMMStatus()  //获得MMStatus
  {
    return(MMStatus);
  }
  public boolean isMMStatusExist()  //是否存在MMStatus
  {
    return(MMStatusExist);
  }
  public void setStatusText (String readStatusText)  //设置StatusText
  {
    StatusText = readStatusText;
    StatusTextExist = true;
  }
  public String getStatusText()  //获得StatusText
  {
    return(StatusText);
  }
  public boolean isStatusTextExist()  //是否存在StatusText
  {
    return(StatusTextExist);
  }
  public String toString()  //返回对象的文本表示
  {
    StringBuffer sb = new StringBuffer();
    sb.append("MMSReplayServerID=" + MMSReplayServerID+"\n");
    sb.append("MMSReplayServerIDExist=" + MMSReplayServerIDExist+"\n");
    sb.append("MessageID=" + MessageID+"\n");
    sb.append("MessageIDExist=" + MessageIDExist+"\n");
    sb.append("Recipient=" + RecipientAddress+"\n");
    sb.append("RecipientExist=" + RecipientAddressExist+"\n");
    sb.append("Sender=" + SenderAddress+"\n");
    sb.append("SenderExist=" + SenderAddressExist+"\n");
    sb.append("TimeStamp=" + TimeStamp+"\n");
    sb.append("TimeStampExist=" + TimeStampExist+"\n");
    sb.append("MMStatus=" + MMStatus+"\n");
    sb.append("MMStatusExist=" + MMStatusExist+"\n");
    sb.append("StatusText=" + StatusText+"\n");
    sb.append("StatusTextExist=" + StatusTextExist+"\n");
    return sb.toString();
  }
}