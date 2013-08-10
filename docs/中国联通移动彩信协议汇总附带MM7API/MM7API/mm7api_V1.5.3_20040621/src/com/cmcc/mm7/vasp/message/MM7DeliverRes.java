/**File Name:MM7DeliverRes.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-2
 * */

package com.cmcc.mm7.vasp.message;

public class MM7DeliverRes extends MM7VASPRes
{
  private String ServiceCode;
  private boolean ServiceCodeExist = false;
  public void setServiceCode(String serviceCode)  //设置业务代码
  {
    this.ServiceCode = serviceCode;
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
  public String toString()  //返回对象的文本表示
  {
    StringBuffer sb = new StringBuffer();
    sb.append("ServiceCode=" + ServiceCode+"\n");
    sb.append("ServiceCodeExist=" + ServiceCodeExist + "\n");
    return sb.toString();
  }
}