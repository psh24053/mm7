/**File Name:MM7ReplaceReq.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-2
 * */

package com.cmcc.mm7.vasp.message;

import java.util.Date;
import com.cmcc.mm7.vasp.common.MMContent;

public class MM7ReplaceReq extends MM7VASPReq
{
  private String VASPID;
  private boolean VASPIDExist;
  private String VASID;
  private boolean VASIDExist;
  private String MessageID;
  private boolean MessageIDExist;
  private String ServiceCode;
  private boolean ServiceCodeExist;
  private Date TimeStamp;
  private boolean TimeStampExist;
  private Date EarliestDeliveryTime;
  private boolean EarliestDeliveryTimeExist;
  private boolean EarliestDeliveryTimeAbsoluteExist;
  private long EarliestTimeDuration;
  private boolean ReadReply;
  private boolean ReadReplyExist;
  private boolean AllowAdaptations;
  private boolean AllowAdaptationsExist;
  private MMContent Content = new MMContent();
  private boolean ContentExist;
  private boolean DistributionIndicator;
  private boolean DistributionIndicatorExist;

  public void setVASPID(String vaspID)  //设置SP代码
  {
    VASPID = vaspID;
    VASPIDExist = true;
  }
  public String getVASPID()  //获得SP代码
  {
    return(VASPID);
  }
  public boolean isVASPIDExist()  //是否存在SP代码
  {
    return(VASPIDExist);
  }
  public void setVASID(String vasID)  //设置服务代码
  {
    VASID = vasID;
    VASIDExist = true;
  }
  public String getVASID()  //获得服务代码
  {
    return(VASID);
  }
  public boolean isVASIDExist()  //是否存在服务代码
  {
    return(VASIDExist);
  }
  public void setMessageID(String messageID)  //设置MessageID
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
  public void setServiceCode(String serviceCode)  //设置业务代码
  {
    ServiceCode = serviceCode;
    ServiceCodeExist = true;
  }
  public String getServiceCode()  //获得业务代码
  {
    return(ServiceCode);
  }
  public boolean isServiceCodeExist()  //是否存在业务代码
  {
    return(ServiceCodeExist);
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
  public void setEarliestDeliveryTime(Date time)  //设置将MM传送给接收方的最早理想时间（绝对时间）
  {
    EarliestDeliveryTime = time;
    EarliestDeliveryTimeAbsoluteExist = true;
  }
  public void setEarliestDeliveryTime(long duration)  //duration提供相对时间，以秒为单位
  {
    EarliestTimeDuration = duration;
    EarliestDeliveryTimeExist = true;
  }
  public long getEarliestDeliveryTimeRelative()  //获得将MM传送给接收方的最早相对理想时间
  {
    return(EarliestTimeDuration);
  }
  public Date getEarliestDeliveryTimeAbsolute()  //获得将MM传送给接收方的最早绝对理想时间
  {
    return(EarliestDeliveryTime);
  }
  public boolean isEarliestDeliveryTimeExist()  //是否存在将MM传送给接收方的最早理想时间。
  {
    return(EarliestDeliveryTimeExist);
  }
  public boolean isEarliestDeliveryTimeAbsoluteExist()  //是否绝对时间格式
  {
    return(EarliestDeliveryTimeAbsoluteExist);
  }
  public void setReadReply(boolean readreply)  //设置是否需要读取报告
  {
    ReadReply = readreply;
    ReadReplyExist = true;
  }
  public boolean getReadReply()  //获得是否需要读取报告
  {
    return(ReadReply);
  }
  public boolean isReadReplyExist()  //是否存在是否需要读取报告
  {
    return(ReadReplyExist);
  }
  public void setAllowAdaptations(boolean allowAdaptations)  //设置allowAdaptations
  {
    AllowAdaptations = allowAdaptations;
    AllowAdaptationsExist = true;
  }
  public boolean getAllowAdaptations()  //获得allowAdaptations
  {
    return(AllowAdaptations);
  }
  public boolean isAllowAdaptationsExist()  //是否存在allowAdaptations
  {
    return(AllowAdaptationsExist);
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
  public void setDistributionIndicator (boolean MDI)  //设置DistributionIndicator
  {
    DistributionIndicator = MDI;
    DistributionIndicatorExist = true;
  }
  public boolean getDistributionIndicator()  //获得DistributionIndicator
  {
    return(DistributionIndicator);
  }
  public boolean isDistributionIndicatorExist ()  //是否存在DistributionIndicator
  {
    return(DistributionIndicatorExist);
  }
  //返回对象的文本表示
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("VASPID=" + VASPID+"\n");
    sb.append("VASPIDExist=" + VASPIDExist+"\n");
    sb.append("VASID=" + VASID+"\n");
    sb.append("VASIDExist=" + VASIDExist+"\n");
    sb.append("MessageID=" + MessageID+"\n");
    sb.append("MessageIDExist=" + MessageIDExist+"\n");
    sb.append("ServiceCode=" + ServiceCode+"\n");
    sb.append("ServiceCodeExist=" + ServiceCodeExist+"\n");
    sb.append("TimeStamp=" + TimeStamp+"\n");
    sb.append("TimeStampExist=" + TimeStampExist+"\n");
    sb.append("EarliestDeliveryTime=" + EarliestDeliveryTime+"\n");
    sb.append("EarliestDeliveryTimeExist=" + EarliestDeliveryTimeExist+"\n");
    sb.append("EarliestDeliveryTimeAbsoluteExist=" +EarliestDeliveryTimeAbsoluteExist+"\n");
    sb.append("EarliestTimeDuration=" + EarliestTimeDuration+"\n");
    sb.append("ReadReply=" + ReadReply+"\n");
    sb.append("ReadReplyExist=" + ReadReplyExist+"\n");
    sb.append("AllowAdaptations=" + AllowAdaptations+"\n");
    sb.append("AllowAdaptationsExist=" + AllowAdaptationsExist+"\n");
    sb.append("Content=" + Content+"\n");
    sb.append("ContentExist=" + ContentExist+"\n");
    sb.append("DistributionIndicator=" + DistributionIndicator+"\n");
    sb.append("DistributionIndicatorExist=" + DistributionIndicatorExist+"\n");
    return sb.toString();
  }
}