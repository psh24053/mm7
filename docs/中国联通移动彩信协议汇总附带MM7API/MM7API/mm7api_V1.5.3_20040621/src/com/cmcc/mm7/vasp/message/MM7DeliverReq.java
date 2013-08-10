/**File Name:MM7DeliverReq.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-1
 * */

package com.cmcc.mm7.vasp.message;

import java.util.List;
import java.util.Date;
import java.util.*;
import com.cmcc.mm7.vasp.common.MMContent;

public class MM7DeliverReq extends MM7RSReq
{
  private String MMSRelayServerID;
  private boolean MMSRelayServerIDExist;
  private String LinkedID;
  private boolean LinkedIDExist;
  private String SenderAddress;
  private boolean SenderAddressExist;
  private List To = new ArrayList();
  private boolean ToExist;
  private List Cc = new ArrayList();
  private boolean CcExist;
  private List Bcc = new ArrayList();
  private boolean BccExist;
  private Date TimeStamp;
  private boolean TimeStampExist;
  private String ReplyChargingID;
  private boolean ReplyChargingIDExist;
  private byte Priority;
  private boolean PriorityExist;
  private String Subject;
  private boolean SubjectExist;
  private MMContent Content = new MMContent();
  private boolean ContentExist;

  public void setMMSRelayServerID(String mmscID)  //设置MMSC的标识符
  {
    MMSRelayServerID = mmscID;
    MMSRelayServerIDExist = true;
  }
  public String getMMSRelayServerID ()  //获得MMSC的标识符
  {
    return(MMSRelayServerID);
  }
  public boolean isMMSRelayServerIDExist()  //是否存在MMSC的标识符
  {
    return(MMSRelayServerIDExist);
  }
  public void setLinkedID(String linkedID)  //设置linkedID
  {
    LinkedID = linkedID;
    LinkedIDExist = true;
  }
  public String getLinkedID()  //获得linkedID
  {
    return(LinkedID);
  }
  public boolean isLinkedIDExist()  //是否存在linkedID
  {
    return(LinkedIDExist);
  }
  public void setSender(String senderAddress)  //设置MM始发方的地址
  {
    SenderAddress = senderAddress;
    SenderAddressExist = true;
  }
  public String getSender()   //获得MM始发方的地址
  {
    return(SenderAddress);
  }
  public boolean isSenderExist()  //是否存在MM始发方的地址
  {
    return(SenderAddressExist);
  }
  public  void setTo(List addressList)  //批量设置接收方MM的地址
  {
    To = addressList;
    ToExist = true;
  }
  public  void  addTo(String address)  //增加单个地址
  {
    To.add(address);
    ToExist = true;
  }
  public List getTo ()  //获得接收方MM的地址
  {
    return(To);
  }
  public boolean isToExist()  //是否存在接收方MM的地址
  {
    return(ToExist);
  }
  public  void setCc (List ccAddressList)  //设置抄送方MM的地址
  {
    Cc = ccAddressList;
    CcExist = true;
  }
  public  void addCc (String ccAddress)  //增加单个抄送地址
  {
    Cc.add(ccAddress);
    CcExist = true;
  }
  public List getCc()  //获得抄送方MM的地址
  {
    return(Cc);
  }
  public boolean isCcExist()  //是否存在抄送方MM的地址
  {
    return(CcExist);
  }
  public  void setBcc (List bccAddressList)  //设置密送方MM的地址
  {
    Bcc = bccAddressList;
    BccExist = true;
  }
  public  void addBcc (String bccAddress)  //增加单个密送地址
  {
    Bcc.add(bccAddress);
    BccExist = true;
  }
  public List getBcc()  //获得密送方MM的地址
  {
    return(Bcc);
  }
  public boolean isBccExist()  //是否存在密送方MM的地址
  {
    return(BccExist);
  }
  public void setTimeStamp(Date timeStamp)  //设置提交MM的时间和日期
  {
    TimeStamp = timeStamp;
    TimeStampExist = true;
  }
  public Date getTimeStamp()  //获得提交MM的时间和日期
  {
    return(TimeStamp);
  }
  public boolean isTimeStampExist()  //是否存在提交MM的时间和日期
  {
    return(TimeStampExist);
  }
  public void setReplyChargingID(String replyChargingID)  //设置ReplyChargingID
  {
    ReplyChargingID = replyChargingID;
    ReplyChargingIDExist = true;
  }
  public String getReplyChargingID()  //获得ReplyChargingID
  {
    return(ReplyChargingID);
  }
  public boolean isReplyChargingIDExist()   //是否存在ReplyChargingID
  {
    return(ReplyChargingIDExist);
  }
  public void setPriority(byte priority)  //设置Priority
  {
    Priority = priority;
    PriorityExist = true;
  }
  public byte getPriority()  //获得Priority
  {
    return(Priority);
  }
  public boolean isPriorityExist()  //是否存在Priority。消息的优先级（重要性）
  {                                 //（0=最低优先级，1=正常，2=紧急）
    return(PriorityExist);
  }
  public void setSubject(String subject)  //设置subject
  {
    Subject = subject;
    SubjectExist = true;
  }
  public String getSubject()  //获得subject
  {
    return(Subject);
  }
  public boolean isSubjectExist()  //是否存在subject
  {
    return(SubjectExist);
  }
  public void setContent(MMContent ammContent)  //设置MMContent
  {
    Content = ammContent;
    ContentExist = true;
  }
  public MMContent getContent()  //获得MMContent
  {
    return(Content);
  }
  public boolean isContentExist()  //是否存在MMContent
  {
    return(ContentExist);
  }
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("MMSRelayServerID=" + MMSRelayServerID+"\n");
    sb.append("MMSRelayServerIDExist=" + MMSRelayServerIDExist+"\n");
    sb.append("LinkedID=" + LinkedID+"\n");
    sb.append("LinkedIDExist=" + LinkedIDExist+"\n");
    sb.append("SenderAddress=" + SenderAddress+"\n");
    sb.append("SenderAddressExist=" + SenderAddressExist+"\n");
    if(!To.isEmpty())
    {
      for(int i=0;i<To.size();i++)
        sb.append("To["+i+"]=" + To.get(i)+"\n");
    }
    sb.append("ToExist=" + ToExist+"\n");
    if(!Cc.isEmpty())
    {
      for(int i=0;i<Cc.size();i++)
        sb.append("Cc["+i+"]=" + Cc.get(i) + "\n");
    }
    sb.append("CcExist=" + CcExist+"\n");
    if(!Bcc.isEmpty())
    {
      for(int i=0;i<Bcc.size();i++)
        sb.append("Bcc["+i+"]=" + Bcc.get(i) + "\n");
    }
    sb.append("BccExist=" + BccExist+"\n");
    sb.append("TimeStamp=" + TimeStamp+"\n");
    sb.append("TimeStampExist=" + TimeStampExist+"\n");
    sb.append("ReplyChargingID=" + ReplyChargingID+"\n");
    sb.append("ReplyChargingIDExist=" + ReplyChargingIDExist+"\n");
    sb.append("Priority=" + Priority+"\n");
    sb.append("PriorityExist=" + PriorityExist+"\n");
    sb.append("Subject=" + Subject+"\n");
    sb.append("SubjectExist=" + SubjectExist+"\n");
    sb.append("Content=" + Content+"\n");
    sb.append("ContentExist=" + ContentExist+"\n");
    return sb.toString();
  }
}