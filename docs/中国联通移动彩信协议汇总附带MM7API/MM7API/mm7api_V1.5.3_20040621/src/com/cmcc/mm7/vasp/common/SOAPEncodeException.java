/**File Name:SOAPEncodeException.java
 * Company:  中国移动集团公司
 * Date  :   2004-1-8
 * */

package com.cmcc.mm7.vasp.common;

public class SOAPEncodeException extends Exception
{
  public SOAPEncodeException(String errorMessage){
    super(errorMessage);
  }
}