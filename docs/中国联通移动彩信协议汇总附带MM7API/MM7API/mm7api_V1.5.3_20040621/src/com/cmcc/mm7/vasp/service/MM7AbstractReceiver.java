/**File Name:MM7AbstractReceiver.java
 * Company:  中国移动集团公司
 * Date  :   2004-2-17
 * */

package com.cmcc.mm7.vasp.service;

import com.cmcc.mm7.vasp.message.*;
import java.lang.Exception;

public interface MM7AbstractReceiver
{
  //构造方法
 //public MM7AbstractReceiver() throws Exception;

  //抽象方法。处理到VASP的传送（deliver）多媒体消息。
  public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq) throws Exception;
  //抽象方法。处理到VASP的发送报告
  public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
  throws Exception;
  //抽象方法。处理到VASP的读后回复报告
  public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq) throws Exception;
  //抽象方法。处理所有请求消息
//  public MM7VASPRes service(MM7RSReq req) throws Exception;
}