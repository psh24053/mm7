/**File Name:MM7CancelReq.java
 * Company:  中国移动集团公司
 * Date  :   2003-12-30
 * */
package com.cmcc.mm7.vasp.message;



public class MM7CancelReq extends MM7VASPReq
{
  private String VASPID;
  private boolean VASPIDExist = false;
  private String VASID;
  private boolean VASIDExist = false;
  private String SenderAddress;
  private boolean SenderAddressExist = false;
  private String MessageID;
  private boolean MessageIDExist = false;

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
  public void setSenderAddress(String senderAddress)  //设置MM始发方的地址
  {
    SenderAddress = senderAddress;
    SenderAddressExist = true;
  }
  public String getSenderAddress()  //获得MM始发方的地址
  {
    return(SenderAddress);
  }
  public boolean isSenderAddressExist()  //是否存在MM始发方的地址
  {
    return(SenderAddressExist);
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
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("VASPID=" + VASPID+"\n");
    sb.append("VASPIDExist=" + VASPIDExist+"\n");
    sb.append("VASID=" + VASID+"\n");
    sb.append("VASIDExist=" + VASIDExist+"\n");
    sb.append("SenderAddress=" + SenderAddress+"\n");
    sb.append("SenderAddressExist=" + SenderAddressExist+"\n");
    sb.append("MessageID=" + MessageID+"\n");
    sb.append("MessageIDExist=" + MessageIDExist+"\n");
    return sb.toString();
  }
}