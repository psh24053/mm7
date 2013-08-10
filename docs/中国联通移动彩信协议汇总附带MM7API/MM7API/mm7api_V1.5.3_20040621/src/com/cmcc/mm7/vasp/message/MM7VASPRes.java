/**File Name:MM7VASPRes.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-2
 * */

package com.cmcc.mm7.vasp.message;

public class MM7VASPRes extends MM7Message
{
  private int StatusCode;
  private boolean StatusCodeExist;
  private String StatusText;
  private boolean StatusTextExist;
  private String StatusDetail;
  private boolean StatusDetailExist;

  public MM7VASPRes()
  {
    StatusCode = 0;
  }
  public void setStatusCode(int statusCode)   //设置StatusCode
  {
    StatusCode = statusCode;
    StatusCodeExist = true;
  }
  public int getStatusCode()  //获得StatusCode
  {
    return(StatusCode );
  }
  public boolean isStatusCodeExist()  //是否存在StatusCode
  {
    return(StatusCodeExist);
  }
  public void setStatusText(String statusText)  //设置StatusText
  {
    StatusText = statusText;
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
  public void setStatusDetail(String statusDetail)  //设置StatusDetail
  {
    StatusDetail = statusDetail;
  }
  public String getStatusDetail()  //获得StatusDetail
  {
    return(StatusDetail);
  }
  public boolean isStatusDetailExist()  //是否存在StatusDetail
  {
    return(StatusDetailExist);
  }
  public String toString()  //返回对象的文本表示
  {
    StringBuffer sb = new StringBuffer();
    sb.append("StatusCode=" + StatusCode+"\n");
    sb.append("StatusCodeExist=" + StatusCodeExist+"\n");
    sb.append("StatusText=" + StatusText+"\n");
    sb.append("StatusTextExist=" + StatusTextExist+"\n");
    sb.append("StatusDetail=" + StatusDetail+"\n");
    sb.append("StatusDetailExist=" + StatusDetailExist+"\n");
    return sb.toString();
  }
}