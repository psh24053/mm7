/**File Name:MMContentType.java
 * Company:  中国移动集团公司
 * Date  :   2004-1-31
 * */

package com.cmcc.mm7.vasp.common;

import java.io.*;
import java.util.*;
import java.util.Hashtable;

public class MMContentType implements Serializable, Cloneable
{
  private String strPrimaryType;
  private String strSubType;
  private boolean bLock;
  private Hashtable ParmeterList;

  /**
   * 构造方法
   * */
  public MMContentType()
  {
    strPrimaryType = "";
    strSubType = "";
    bLock = false;
    ParmeterList = new Hashtable();
  }
  /**
   * 构造方法
   * */
  public MMContentType(String type)
  {
    strPrimaryType = "";
    strSubType = "";
    bLock = false;
    ParmeterList = new Hashtable();

   int index = type.indexOf("/");
    if(index>0)
    {
      String strPraType = type.substring(0,index);
      String strSubType = type.substring(index+1);
      setPrimaryType(strPraType);
      if(strSubType.indexOf(";")>0)
        strSubType = strSubType.substring(0,strSubType.indexOf(";"));
      setSubType(strSubType);
    }
    else
      System.err.println("该类型不是标准类型！type="+type);
  }
  /**
   * 设置参数
   * */
  public void setParameter(String name,String value)
  {
    ParmeterList.put(name,value);
  }
  /**
   * 获得参数
   * */
  public String getParameter(String name)
  {
    return((String)ParmeterList.get(name));
  }
  /**
   * 返回所有参数的列表
   * */
  public Hashtable getParameterList()
  {
    return(ParmeterList);
  }
  /**
   * 获得主类型
   * */
  public String getPrimaryType()
  {
    return(this.strPrimaryType);
  }
  /**
   * 设置主类型
   * */
  public void setPrimaryType(String primaryType)
  {
    this.strPrimaryType = primaryType;
  }
  /**
   * 锁定对象类型
   * */
  public MMContentType lock()
  {
    bLock = true;
    return this;
  }
  /**
   * 对象类型是否锁定
   * */
  public boolean isLock()
  {
    return(bLock);
  }
  /**
   * 设置子类型
   * */
  public void setSubType(String subType)
  {
    this.strSubType = subType;
  }
  /**
   * 获得子类型
   * */
  public String getSubType()
  {
    return(this.strSubType);
  }
  /**
   * 比较主类型与子类型是否一致
   * */
  public boolean match(MMContentType type)
  {
    String strPrimaryType = type.getPrimaryType().trim();
    String strSubType = type.getSubType().trim();
    if(strPrimaryType.equals(strSubType))
      return true;
    else
      return false;
  }
  /**
   * 比较主类型与子类型是否一致
   * */
  public boolean match(String type)
  {
    int index = type.indexOf("/");
    if(index>0)
    {
      String strParType = type.substring(0,index);
      String strSubType = type.substring(index+1);
      if(strParType.trim().equals(strPrimaryType))
      {
        if(strSubType.trim().equals(strSubType))
          return true;
        else
          return false;
      }
      else
        return false;
    }
    else{
      System.err.println("该类型不是标准类型！type="+type);
      return (false);
    }
  }
  /**
   * 返回对象的文本表示
   * */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("strPrimaryType=" + strPrimaryType+"\n");
    sb.append("strSubType=" + strSubType+"\n");
    sb.append("bLock="+bLock+"\n");
    System.out.println("ParmeterList="+ParmeterList);
    if(ParmeterList != null)
    {
      Enumeration strParmeterList = ParmeterList.elements();
      int i = ParmeterList.size()-1;
      while(strParmeterList.hasMoreElements())
      {
        sb.append("ParmeterList["+i+"]="+(String)strParmeterList.nextElement()+"\n");
        i--;
      }
    }
    return sb.toString();
  }
}