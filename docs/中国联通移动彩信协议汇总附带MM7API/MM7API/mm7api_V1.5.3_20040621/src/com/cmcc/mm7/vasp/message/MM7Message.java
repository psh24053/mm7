/**File Name:MM7Message.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-2
 * */

package com.cmcc.mm7.vasp.message;

import java.io.*;

public class MM7Message implements Serializable, Cloneable
{
  private String TransactionID;
  private boolean TransactionIDExist;
  private String MM7Version="6.3.0";
  private boolean MM7VersionExist=true;
  public MM7Message()
  {
    TransactionID = "";
    MM7Version = "6.3.0";
  }
  public void setTransactionID(String transactionid)
  {
    TransactionID = transactionid;
    TransactionIDExist = true;
  }
  public String getTransactionID()
  {
    return(TransactionID);
  }
  public boolean isTransactionIDExist()
  {
    return(TransactionIDExist);
  }
  public String getMM7Version()
  {
    return(MM7Version);
  }
  public boolean isMM7VersionExist()
  {
    return(MM7VersionExist);
  }
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("TransactionID=" + TransactionID+"\n");
    sb.append("TransactionIDExist=" + TransactionIDExist+"\n");
    sb.append("MM7Version=" + MM7Version+"\n");
    sb.append("MM7VersionExist=" + MM7VersionExist+"\n");
    return sb.toString();
  }
}