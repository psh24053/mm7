/**File Name:MM7SubmitReq.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-2
 * */

package com.cmcc.mm7.vasp.message;

import java.util.List;
import java.util.Date;
import java.util.*;
import com.cmcc.mm7.vasp.common.MMContent;

public class MM7SubmitReq extends MM7VASPReq
{
  private String VASPID;
  private String VASID;
  private String ServiceCode;
  private String SenderAddress;
  private List To = new ArrayList();
  private List Cc = new ArrayList();
  private List Bcc = new ArrayList();
  private String LinkedID;
  private String MessageClass;
  private Date TimeStamp;
  private Date ExpiryDate;
  private long ExpiryDateDuration;
  private Date EarliestDeliveryTime;
  private long EarliestTimeDuration;
  private boolean DeliveryReport;
  private boolean ReadReply;
  private boolean ReplyCharging;
  private Date ReplyDeadline;
  private long ReplyDeadlineDuration;
  private int ReplyChargingSize;
  private byte Priority;
  private String Subject;
  private boolean AllowAdaptations=true;
  private byte ChargedParty;
  private String ChargedPartyID;
  private MMContent Content = new MMContent();
  private boolean DistributionIndicator;
  private boolean VASPIDExist;
  private boolean VASIDExist;
  private boolean ServiceCodeExist;
  private boolean SenderAddressExist;
  private boolean ToExist;
  private boolean CcExist;
  private boolean BccExist;
  private boolean LinkedIDExist;
  private boolean MessageClassExist;
  private boolean TimeStampExist;
  private boolean ExpiryDateExist;
  private boolean ExpiryDateAbsoluteExist;
  private boolean EarliestDeliveryTimeExist;
  private boolean EarliestDeliveryTimeAbsoluteExist;
  private boolean DeliveryReportExist;
  private boolean ReadReplyExist;
  private boolean ReplyChargingExist;
  private boolean ReplyDeadlineExist;
  private boolean ReplyDeadlineAbsoluteExist;
  private boolean ReplyChargingSizeExist;
  private boolean PriorityExist;
  private boolean SubjectExist ;
  private boolean AllowAdaptationsExist=true;
  private boolean ChargedPartyExist;
  private boolean ChargedPartyIDExist;
  private boolean ContentExist;
  private boolean DistributionIndicatorExist;

 /* public MM7SubmitReq()
  {
    To = new ArrayList();
    Cc = new ArrayList();
    Bcc = new ArrayList();
    DeliveryReport = false;
    ReadReply = false;
    ReplyCharging = false;
    Priority = 0;
    AllowAdaptations = false;
    ChargedParty = 0;*/
    //Content = new MMContent();
    /*DistributionIndicator = false;
    VASPIDExist = false;
    VASIDExist = false;
    ServiceCodeExist  = false;
    SenderAddressExist  = false;
    ToExist = false;
    CcExist = false;
    BccExist = false;
    LinkedIDExist = false;
    MessageClassExist = false;
    TimeStampExist = false;
    ExpiryDateExist = false;
    ExpiryDateAbsoluteExist = false;
    EarliestDeliveryTimeExist = false;
    EarliestDeliveryTimeAbsoluteExist = false;
    DeliveryReportExist = false;
    ReadReplyExist = false;
    ReplyChargingExist = false;
    ReplyDeadlineExist = false;
    ReplyDeadlineAbsoluteExist = false;
    ReplyChargingSizeExist = false;
    PriorityExist = false;
    SubjectExist = false;
    AllowAdaptationsExist = false;
    ChargedPartyExist = false;
    FeeTerminalIDExist = false;
    Content = new MMContent();
    ContentExist = false;
    DistributionIndicatorExist = false;*/
  //}

  public void setVASPID(String vaspid)  //设置SP代码
  {
    this.VASPID = vaspid;
    VASPIDExist = true;
  }
  public String getVASPID()  //获得SP代码
  {
    return(this.VASPID);
  }
  public boolean isVASPIDExist()  //是否存在SP代码
  {
    return(VASPIDExist);
  }
  public void setVASID(String vasid)  //设置服务代码
  {
    this.VASID = vasid;
    VASIDExist = true;
  }
  public String getVASID()  //获得服务代码
  {
    return(this.VASID);
  }
  public boolean isVASIDExist()  //是否存在服务代码
  {
    return(VASIDExist);
  }
  public void setServiceCode(String servicecode)  //设置业务代码
  {
    this.ServiceCode = servicecode;
    ServiceCodeExist = true;
  }
  public String getServiceCode()  //获得业务代码
  {
    return(this.ServiceCode);
  }
  public boolean isServiceCodeExist()  //是否存在业务代码
  {
    return(ServiceCodeExist);
  }
  public void setSenderAddress(String senderAddress)  //设置MM始发方的地址
  {
    this.SenderAddress = senderAddress;
    SenderAddressExist = true;
  }
  public String getSenderAddress()  //获得MM始发方的地址
  {
    return(this.SenderAddress);
  }
  public boolean isSenderAddressExist()  //是否存在MM始发方的地址
  {
    return(SenderAddressExist);
  }
  public  void setTo(List addressList)  //批量设置接收方MM的地址
  {
    this.To = addressList;
    ToExist = true;
  }
  public  void  addTo(String address)  //增加单个地址
  {
    To.add(address);
    ToExist = true;
  }
  public List getTo ()  //获得接收方MM的地址
  {
    return(this.To);
  }
  public boolean isToExist()  //是否存在接收方MM的地址
  {
    return(ToExist);
  }
  public  void setCc (List ccAddressList)  //设置抄送方MM的地址
  {
    this.Cc = ccAddressList;
    CcExist = true;
  }
  public  void addCc (String ccAddress)  //增加单个抄送地址
  {
    Cc.add(ccAddress);
    CcExist = true;
  }
  public List getCc()  //获得抄送方MM的地址
  {
    return(this.Cc);
  }
  public boolean isCcExist()  //是否存在抄送方MM的地址
  {
    return(CcExist);
  }

  public  void setBcc (List bccAddressList)  //设置密送方MM的地址
  {
    this.Bcc = bccAddressList;
    BccExist = true;
  }
  public  void addBcc (String bccAddress)  //增加单个密送地址
  {
    Bcc.add(bccAddress);
    BccExist = true;
  }
  public List getBcc()  //获得密送方MM的地址
  {
    return(this.Bcc);
  }
  public boolean isBccExist()  //是否存在密送方MM的地址
  {
    return(BccExist);
  }
  public void setLinkedID(String linkedID)  //设置链接标识
  {
    this.LinkedID = linkedID;
    LinkedIDExist = true;
  }
  public String getLinkedID()   //获得链接标识
  {
    return(this.LinkedID);
  }
  public boolean isLinkedIDExist()  //是否存在链接标识
  {
    return(LinkedIDExist);
  }
  public void setMessageClass(String messageclass)  //设置MM的类别
  {
    this.MessageClass = messageclass;
    MessageClassExist = true;
  }
  public String getMessageClass()  //获得MM的类别
  {
    return(this.MessageClass);
  }
  public boolean isMessageClassExist()  //是否存在MM的类别
  {
    return(MessageClassExist);
  }
  public void setTimeStamp(Date timeStamp)  //设置提交MM的时间和日期
  {
    this.TimeStamp = timeStamp;
    TimeStampExist = true;
  }
  public Date getTimeStamp()  //获得提交MM的时间和日期
  {
    return(this.TimeStamp);
  }
  public boolean isTimeStampExist()  //是否存在提交MM的时间和日期
  {
    return(TimeStampExist);
  }
  public void setExpiryDate(Date expiryDate)  //设置MM的指定超时时间（绝对时间）
  {
    this.ExpiryDate = expiryDate;
    ExpiryDateAbsoluteExist = true;
  }
  public void setExpiryDate(long duration)  //duration提供相对时间，以秒为单位
  {
    this.ExpiryDateDuration = duration;
    ExpiryDateExist = true;
  }
  public long getExpiryDateRelative()  //获得MM的指定超时时间
  {
    return(this.ExpiryDateDuration);
  }
  public Date getExpiryDateAbsolute()
  {
    return(this.ExpiryDate);
  }
  public boolean isExpiryDateExist()  //是否存在MM的指定超时时间
  {
    return(ExpiryDateExist);
  }
  public boolean isExpiryDateAbsolute()  //是否绝对时间格式
  {
    return(ExpiryDateAbsoluteExist);
  }
  public void setEarliestDeliveryTime(Date time)  //设置将MM传送给接收方的最早理想时间（绝对时间）
  {
    this.EarliestDeliveryTime = time;
    EarliestDeliveryTimeAbsoluteExist = true;
  }
  public void setEarliestDeliveryTime(long duration)  //duration提供相对时间，以秒为单位
  {
    this.EarliestTimeDuration = duration;
    EarliestDeliveryTimeExist = true;
  }
  public long getEarliestDeliveryTimeRelative()  //获得将MM传送给接收方的最早相对理想时间
  {
    return(this.EarliestTimeDuration);
  }
  public Date getEarliestDeliveryTimeAbsolute()  //获得将MM传送给接收方的最早绝对理想时间
  {
    return(this.EarliestDeliveryTime);
  }
  public boolean isEarliestDeliveryTimeExist()  //是否存在将MM传送给接收方的最早理想时间。
  {
    return(EarliestDeliveryTimeExist);
  }
  public boolean isEarliestDeliveryTimeAbsolute()  //是否绝对时间格式
  {
    return(EarliestDeliveryTimeAbsoluteExist);
  }
  public void setDeliveryReport(boolean deliveryreport)  //设置是否需要发送报告的请求
  {
    this.DeliveryReport = deliveryreport;
    DeliveryReportExist = true;
  }
  public boolean getDeliveryReport()  //获得是否需要发送报告的请求
  {
    return(this.DeliveryReport);
  }
  public boolean isDeliveryReportExist()  //是否存在是否需要发送报告的请求
  {
    return(DeliveryReportExist);
  }
  public void setReadReply(boolean readreply)  //设置是否需要读取报告
  {
    this.ReadReply = readreply;
    ReadReplyExist = true;
  }
  public boolean getReadReply()  //获得是否需要读取报告
  {
    return(this.ReadReply);
  }
  public boolean isReadReplyExist()  //是否存在是否需要读取报告
  {
    return(ReadReplyExist);
  }
  public void setReplyCharging(boolean replyCharging)  //设置应答计费的请求
  {
    this.ReplyCharging = replyCharging;
    ReplyChargingExist = true;
  }
  public boolean getReplyCharging()  //获得应答计费的请求
  {
    return(this.ReplyCharging);
  }
  public boolean isReplyChargingExist()  //是否存在应答计费的请求
  {
    return(ReplyChargingExist);
  }
  public void setReplyDeadline(Date replyDeadline)  //设置ReplyChargingDeadline（绝对时间）
  {
    this.ReplyDeadline = replyDeadline;
    ReplyDeadlineAbsoluteExist = true;
  }
  public void setReplyDeadline(long duration)  //duration提供相对时间 、以秒为单位
  {
    this.ReplyDeadlineDuration = duration;
    ReplyDeadlineExist = true;
  }
  public long getReplyDeadlineRelative()  //获得ReplyChargingDeadline
  {
    return(this.ReplyDeadlineDuration);
  }
  public Date getReplyDeadlineAbsolute()
  {
    return(this.ReplyDeadline);
  }
  public boolean isReplyDeadlineExist()  //是否存在ReplyChargingDeadline
  {
    return(ReplyDeadlineExist);
  }
  public boolean isReplyDeadlineAbsoluteExist()  //是否绝对时间格式
  {
    return(ReplyDeadlineAbsoluteExist);
  }
  public void setReplyChargingSize(int replyChargingSize)  //设置ReplyChargingSize
  {
    this.ReplyChargingSize = replyChargingSize;
    ReplyChargingSizeExist = true;
  }
  public int getReplyChargingSize()  //获得ReplyChargingSize
  {
    return(this.ReplyChargingSize);
  }
  public boolean isReplyChargingSizeExist()  //是否存在ReplyChargingSize
  {
    return(ReplyChargingSizeExist);
  }
  public void setPriority(byte priority)  //设置Priority
  {
    this.Priority = priority;
    PriorityExist = true;
  }
  public byte getPriority()  //获得Priority
  {
    return(this.Priority);
  }
  public boolean isPriorityExist()  //是否存在Priority。消息的优先级（重要性）
  {                                 //（0=最低优先级，1=正常，2=紧急）
    return(PriorityExist);
  }
  public void setSubject(String subject)  //设置subject
  {
    this.Subject = subject;
    SubjectExist = true;
  }
  public String getSubject()  //获得subject
  {
    return(this.Subject);
  }
  public boolean isSubjectExist()  //是否存在subject
  {
    return(SubjectExist);
  }
  public void setAllowAdaptations(boolean allowAdaptations)  //设置allowAdaptations
  {
    this.AllowAdaptations = allowAdaptations;
    AllowAdaptationsExist = true;
  }
  public boolean getAllowAdaptations()  //获得allowAdaptations
  {
    return(this.AllowAdaptations);
  }
  public boolean isAllowAdaptationsExist()  //是否存在allowAdaptations
  {
    return(AllowAdaptationsExist);
  }
  public void setChargedParty(byte chargedParty)  //设置chargedParty
  {
    this.ChargedParty = chargedParty;
    ChargedPartyExist = true;
  }
  public byte getChargedParty()  //获得chargedParty
  {
    return(this.ChargedParty);
  }
  public boolean isChargedPartyExist()  //是否存在chargedParty,
  {                                     //指明VASP所提交MM的付费方，例如，发送方(0)、接收方(1)、
    return(ChargedPartyExist);                       //发送方和接收方(2)或两方均不付费(3)
  }
/*  public void setFeeTerminalID(String feeTerminalID)  //设置feeTerminalID
  {
    this.FeeTerminalID = feeTerminalID;
    FeeTerminalIDExist = true;
  }
  public String getFeeTerminalID()  //获得feeTerminalID
  {
    return(this.FeeTerminalID);
  }
  public boolean isFeeTerminalIDExist()  //是否存在feeTerminalID
  {                                      //在付费方既不是发送方，也不是接收方的情况下，
    return(FeeTerminalIDExist);                        //需要使用该字段标识付费方的E.164号码
  }*/
  public void setChargedPartyID(String chargedPartyID)  //设置feeTerminalID
  {
    this.ChargedPartyID = chargedPartyID;
    ChargedPartyIDExist = true;
  }
  public String getChargedPartyID()  //获得feeTerminalID
  {
    return(this.ChargedPartyID);
  }
  public boolean isChargedPartyIDExist()  //是否存在feeTerminalID
  {                                      //在付费方既不是发送方，也不是接收方的情况下，
    return(ChargedPartyIDExist);                        //需要使用该字段标识付费方的E.164号码
  }
  public void setContent(MMContent ammContent)  //设置MMContent
  {
    this.Content = ammContent;
    ContentExist = true;
  }
  public MMContent getContent()  //获得MMContent
  {
    return(this.Content);
  }
  public boolean isContentExist()  //是否存在MMContent
  {
    return(ContentExist);
  }
  public void setDistributionIndicator (boolean MDI)  //设置DistributionIndicator
  {
    this.DistributionIndicator = MDI;
    DistributionIndicatorExist = true;
  }
  public boolean getDistributionIndicator()  //获得DistributionIndicator
  {
    return(this.DistributionIndicator);
  }
  public boolean isDistributionIndicatorExist()  //是否存在DistributionIndicator
  {
    return(DistributionIndicatorExist);
  }
  //返回对象的文本表示
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("VASPID=" + VASPID+"\n");
    sb.append("VASID=" + VASID+"\n");
    sb.append("ServiceCode=" + ServiceCode+"\n");
    sb.append("SenderAddress=" + SenderAddress+"\n");
    if(!To.isEmpty())
    {
      for(int i=0;i<To.size();i++)
        sb.append("To["+i+"]=" + To.get(i) + "\n");
    }
    if(!Cc.isEmpty())
    {
      for(int i=0;i<Cc.size();i++)
        sb.append("Cc["+i+"]=" + Cc.get(i) + "\n");
    }
    if(!Bcc.isEmpty())
    {
      for(int i=0;i<Bcc.size();i++)
        sb.append("Bcc["+i+"]=" + Bcc.get(i) + "\n");
    }
    sb.append("LinkedID=" + LinkedID+"\n");
    sb.append("MessageClass=" + MessageClass+"\n");
    sb.append("TimeStamp=" + TimeStamp+"\n");
    sb.append("ExpiryDate=" + ExpiryDate+"\n");
    sb.append("ExpiryDateDuration=" + ExpiryDateDuration+"\n");
    sb.append("EarliestDeliveryTime=" + EarliestDeliveryTime+"\n");
    sb.append("EarliestTimeDuration=" + EarliestTimeDuration+"\n");
    sb.append("DeliveryReport=" + DeliveryReport+"\n");
    sb.append("ReadReply=" + ReadReply+"\n");
    sb.append("ReplyCharging=" + ReplyCharging+"\n");
    sb.append("ReplyDeadline=" + ReplyDeadline+"\n");
    sb.append("ReplyDeadlineDuration=" + ReplyDeadlineDuration+"\n");
    sb.append("ReplyChargingSize=" + ReplyChargingSize+"\n");
    sb.append("Priority=" + Priority+"\n");
    sb.append("Subject=" + Subject+"\n");
    sb.append("AllowAdaptations=" + AllowAdaptations+"\n");
    sb.append("ChargedParty=" + ChargedParty+"\n");
    sb.append("ChargedPartyID=" + ChargedPartyID + "\n");
    sb.append("Content=" + Content+"\n");
    sb.append("DistributionIndicator=" + DistributionIndicator+"\n");
    sb.append("VASPIDExist=" + VASPIDExist+"\n");
    sb.append("VASIDExist=" + VASIDExist+"\n");
    sb.append("ServiceCodeExist=" + ServiceCodeExist+"\n");
    sb.append("SenderAddressExist=" + SenderAddressExist+"\n");
    sb.append("ToExist=" + ToExist+"\n");
    sb.append("CcExist=" + CcExist+"\n");
    sb.append("BccExist=" + BccExist+"\n");
    sb.append("LinkedIDExist=" + LinkedIDExist+"\n");
    sb.append("MessageClassExist=" + MessageClassExist+"\n");
    sb.append("TimeStampExist=" + TimeStampExist+"\n");
    sb.append("ExpiryDateExist=" + ExpiryDateExist+"\n");
    sb.append("ExpiryDateAbsoluteExist=" + ExpiryDateAbsoluteExist+"\n");
    sb.append("EarliestDeliveryTimeExist=" + EarliestDeliveryTimeExist+"\n");
    sb.append("EarliestDeliveryTimeAbsoluteExist=" +
              EarliestDeliveryTimeAbsoluteExist+"\n");
    sb.append("DeliveryReportExist=" + DeliveryReportExist+"\n");
    sb.append("ReadReplyExist=" + ReadReplyExist+"\n");
    sb.append("ReplyChargingExist=" + ReplyChargingExist+"\n");
    sb.append("ReplyDeadlineExist=" + ReplyDeadlineExist+"\n");
    sb.append("ReplyDeadlineAbsoluteExist=" + ReplyDeadlineAbsoluteExist+"\n");
    sb.append("ReplyChargingSizeExist=" + ReplyChargingSizeExist+"\n");
    sb.append("PriorityExist=" + PriorityExist+"\n");
    sb.append("SubjectExist=" + SubjectExist+"\n");
    sb.append("AllowAdaptationsExist=" + AllowAdaptationsExist+"\n");
    sb.append("ChargedPartyExist=" + ChargedPartyExist+"\n");
    sb.append("FeeTerminalIDExist=" + ChargedPartyIDExist+"\n");
    sb.append("ContentExist=" + ContentExist+"\n");
    sb.append("DistributionIndicatorExist=" + DistributionIndicatorExist+"\n");
    return sb.toString();
  }
}