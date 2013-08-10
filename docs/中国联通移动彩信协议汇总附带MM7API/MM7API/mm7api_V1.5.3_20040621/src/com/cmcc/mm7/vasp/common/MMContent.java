/**File Name:MMContent.java
 * Company:  中国移动集团公司
 * Date  :   2004-1-30
 * */

package com.cmcc.mm7.vasp.common;

import java.io.*;
import java.util.*;

public class MMContent implements Serializable, Cloneable
{
  private MMContentType ContentType;
  private String ContentID;
  private String ContentLocation;
  private MMContent PresentionContent;
  private String Charset;
  private boolean ContentIDExist;
  private boolean ContentLocationExist;
  private boolean PresentionContentExist;
  public List SubContents = new ArrayList();
  private boolean Multipart;
  private ByteArrayOutputStream byteOutput;

  /**
   * 构造方法
   * */
  public MMContent()
  {
    ContentType = new MMContentType();
    ContentID = "";
    ContentLocation = "";
    Charset = "UTF-8";
    ContentIDExist = false;
    ContentLocationExist = false;
    PresentionContentExist = false;
    //SubContents = new ArrayList();
    Multipart = false;
    byteOutput = new ByteArrayOutputStream();
  }
  /**
   * 该构造方法是为了几个create方法所创建的。外部并不能调用。所以申明成private。
   * */
  private MMContent(byte[] content)
  {
    byteOutput = new ByteArrayOutputStream();
    try{
      byteOutput.write(content);
    }catch(IOException e){
      System.err.println(e);
    }
  }
  /**
   * 获得此MMContent嵌套的子MMContent的List
   * */
  public List getSubContents()
  {
    return(SubContents);
  }
  /**
   * 是否存在嵌套媒体
   * */
  public boolean isMultipart()
  {
    return(Multipart);
  }
  /**
   * 获得内容类型
   * */
  public MMContentType getContentType()
  {
    return(ContentType);
  }
  /**
   * 设置内容类型
   * */
  public void setContentType(MMContentType contentType)
  {
    ContentType = contentType;
  }

  //////////////
  public void setContentType(String type)
  {
    MMContentType conType = new MMContentType(type);
    ContentType = conType;
  }

  /////////////////////

  /**
   * 获得ContentID
   * */
  public String getContentID()
  {
    return(ContentID);
  }
  /**
   * 设置ContentID
   * */
  public void setContentID(String contentID)
  {
    ContentID = contentID;
    ContentIDExist = true;
  }
  /**
   * 是否存在ContentID
   * */
  public boolean isContentIDExist()
  {
    return(ContentIDExist);
  }
  /**
   * 获得ContentLocation
   * */
  public String getContentLocation()
  {
    return(ContentLocation);
  }
  /**
   * 设置ContentLocation
   * */
  public void setContentLocation(String contentLocation)
  {
    ContentLocation = contentLocation;
    ContentLocationExist = true;
  }
  /**
   * 是否存在ContentLocation
   * */
  public boolean isContentLocationExist()
  {
    return(ContentLocationExist);
  }
  /**
   * 获得Presentation部份的内容
   * */
  public MMContent getPresentionContent()
  {
    return(PresentionContent);
  }
  /**
   * 设置MMContent为Presentation部份
   * */
  public void setPresentionContent(MMContent presentionContent)
  {
    PresentionContent = presentionContent;
    PresentionContentExist = true;
  }
  /**
   * 是否存在Presentation部份
   * */
  public boolean isPresentionContentExist()
  {
    return(PresentionContentExist);
  }
  /**
   * 加入单个MMContent到MMContent（List）
   * */
  public void addSubContent(MMContent content)
  {
     SubContents.add(content);
     Multipart = true;
  }
  /**
   * 以二进制方式获得MMContent的内容
   * */
  public byte[] getContent()
  {
    return(byteOutput.toByteArray());
  }
  /**
   * 以String方式获得MMContent的内容
   * */
  public String getContentAsString()
  {
    String charset = getCharset();
    if(charset == null || charset.equals(""))
      charset = "UTF-8";
    try{
      return (byteOutput.toString(charset));
      //return (byteOutput.toString());
    }catch(IOException ioe){
      System.err.println(ioe);
      return null;
    }
  }
  /**
   * 通过ContentID获得MMContent类型的subContent
   * */
  public MMContent getSubContentByID(String contentID)
  {
    List subcontents = new ArrayList();
    subcontents = SubContents;
    MMContent subContent = new MMContent();
    for(int i=0;i<subcontents.size();i++)
    {
      MMContent subcontent = (MMContent)subcontents.get(i);
      if(subcontent.isContentIDExist())
      {
        if (contentID.equals(subcontent.getContentID())) {
          subContent = subcontent;
          break;
        }
      }
    }
    return(subContent);
  }
  /**
   * 通过ContentLocation获得MMContent类型的subContent
   * */
  public MMContent getSubContentByLocation(String contentLocation)
  {
    List subcontents = new ArrayList();
    subcontents = SubContents;
    MMContent subContent = new MMContent();
    for(int i=0;i<subcontents.size();i++)
    {
      MMContent subcontent = (MMContent)subcontents.get(i);
      if(subcontent.isContentLocationExist())
      {
        if (contentLocation.equals(subcontent.getContentLocation())) {
          subContent = subcontent;
          break;
        }
      }
    }
    return(subContent);
  }
  /**
   * 返回媒体内容的大小
   * */
  public int getSize()
  {
    return(byteOutput.toByteArray().length);
  }
  /**
   * 获得文本媒体内容的字符集
   * */
  public String getCharset()
  {
    return(Charset);
  }
  /**
   * 设置文本媒体内容的字符集
   * */
  public void setCharset(String charset)
  {
    Charset = charset;
  }
  /**
   * 通过输入String 类型建立MMContent
   * */
  public static MMContent createFromString(String content)
  {
    MMContent mmContent = new MMContent(content.getBytes());
    return(mmContent);
  }
  /**
   * 通过输入InputStream类型建立MMContent
   * */
  public static MMContent createFromStream(InputStream in)
  {
    DataInputStream input = new DataInputStream(in);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data = null;
    try{
      while (input.available() != 0) {
        output.write(input.readByte());
      }
      data = output.toByteArray();
    }catch(IOException ioe){
      System.err.println(ioe);
    }
    MMContent mmContent = new MMContent(data);
    return(mmContent);
  }
  /*
   *通过输入InputStream类型和length建立MMContent
   */
  public static MMContent createFromStream(InputStream in, int length)
  {
    DataInputStream input = new DataInputStream(in);
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data = null;
    try{
      while (input.available() != 0) {
        byteout.write(input.readByte());
      }
      data = byteout.toByteArray();
      output.write(data,0,length);
    }catch(IOException ioe){
      System.err.println(ioe);
    }
    data = output.toByteArray();
    MMContent mmContent = new MMContent(data);
    return(mmContent);
  }
  /**
   * 通过输入byte[]类型建立MMContent
   * */
  public static MMContent createFromBytes(byte[] data)
  {
    MMContent mmContent = new MMContent(data);
    return(mmContent);
  }
  /**
   * 通过输入文件的绝对路径建立MMContent
   * */
  public static MMContent createFromFile(String filename)
  {
    try{
      DataInputStream input = new DataInputStream(new FileInputStream(filename));
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      byte[] data = null;
      while (input.available() != 0) {
        output.write(input.readByte());
      }
      data = output.toByteArray();
      MMContent mmContent = new MMContent(data);
      return (mmContent);
    }catch(IOException ioe){
      System.err.println(ioe);
      return null;
    }
  }
  /**
   * 返回对象的文本表示
   * */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("ContentType=" + ContentType+"\n");
    sb.append("ContentID=" + ContentID+"\n");
    sb.append("ContentLocation=" + ContentLocation+"\n");
    sb.append("PresentionContent=" + PresentionContent+"\n");
    sb.append("Charset=" + Charset+"\n");
    sb.append("ContentIDExist=" + ContentIDExist+"\n");
    sb.append("ContentLocationExist=" + ContentLocationExist+"\n");
    sb.append("PresentionContentExist=" + PresentionContentExist+"\n");
    if(!SubContents.isEmpty())
    {
      for(int i=0;i<SubContents.size();i++)
        sb.append("SubContents["+i+"]=" + SubContents.get(i) + "\n");
    }
    sb.append("Multipart=" + Multipart+"\n");
    sb.append("byteOutput=" + byteOutput+"\n");
    return sb.toString();
  }
}