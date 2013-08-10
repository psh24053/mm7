// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   VaspSendTest.java

package com.cmcc.mm7.vasp.conf;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.service.MM7Sender;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Random;

// Referenced classes of package com.cmcc.mm7.vasp.conf:
//            MM7Config

public class VaspSendTest
{

    public VaspSendTest()
    {
    }

    public static void main(String args[])
    {
        MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
        mm7Config.setConnConfigName("./config/ConnConfig.xml");
        try
        {
            Random rand = new Random(1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            MM7SubmitReq submit = new MM7SubmitReq();
            submit.setTransactionID("1");
            submit.addTo("13901580000");
            submit.setVASID("5555");
            submit.setVASPID("805555");
            submit.setServiceCode("000000");
            submit.setSenderAddress("1398");
            submit.setSubject("测试");
            MMContent content = new MMContent();
            content.setContentType(com.cmcc.mm7.vasp.common.MMConstants.ContentType.MULTIPART_RELATED);
            content.setContentID("mm7Test");
            MMContent sub1 = MMContent.createFromFile("C:\\image\\1.smil");
            sub1.setContentType(com.cmcc.mm7.vasp.common.MMConstants.ContentType.SMIL);
            sub1.setContentID("1.smil");
            content.addSubContent(sub1);
            MMContent sub2 = MMContent.createFromFile("C:\\image\\2.jpg");
            sub2.setContentType(com.cmcc.mm7.vasp.common.MMConstants.ContentType.JPEG);
            sub2.setContentID("2.jpg");
            content.addSubContent(sub2);
            submit.setContent(content);
            MM7Sender mm7Sender = new MM7Sender(mm7Config);
            System.out.println("发送前！");
            int i = 0;
            do
            {
                MM7RSRes res = mm7Sender.send(submit);
                if(res instanceof MM7SubmitRes)
                {
                    System.out.println("res.statuscode=" + res.getStatusCode() + ";res.statusText=" + res.getStatusText());
                    MM7SubmitRes subRes = (MM7SubmitRes)res;
                    System.out.println("messageid=" + subRes.getMessageID());
                } else
                {
                    System.out.println("失败消息");
                }
                i++;
            } while(true);
        }
        catch(Exception ee)
        {
            System.out.println(ee);
        }
    }
}
