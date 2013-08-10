/**File Name:ConnectionPool.java
 * Company:  中国移动集团公司
 * Date  :   2004-1-9
 * */

package com.cmcc.mm7.vasp.common;

import java.util.*;
import java.util.HashMap;
import com.cmcc.mm7.vasp.conf.*;
import com.cmcc.mm7.vasp.common.ConnectionWrap;

public class ConnectionPool implements Runnable
{
  //public static List ClientList;
  public List ClientList;
  private static boolean isCreate;
  public HashMap hashmap;
  private long time;
  private int IPCount;
  private MM7Config Mm7Config;
  private String NonceCount;
  private int ServerMaxSize;
  private String KeepAlive;
  private static final ConnectionPool m_instance = new ConnectionPool();

  public ConnectionPool()
  {
    hashmap = new HashMap();
    isCreate = false;
    ClientList = null;
    IPCount = 0;
    Mm7Config = null;
    NonceCount = "00000001";
    ServerMaxSize = 0;
    KeepAlive = "off";
  }
  public static ConnectionPool getInstance()
  {
    return m_instance;
  }
  public void setConfig(MM7Config mm7config)
  {
    Mm7Config = mm7config;
    if(ClientList == null)
      init();
    if(!isCreate)
    {
      Thread thread = new Thread(this);
      thread.run();
      isCreate = true;
    }
  }
  //主要是为了实现MMSCIP的平均分配而设置的顺序。
  public void setIPCount(int count)
  {
    IPCount = count;
  }
  //得到当前应该分配给第几个MMSCIP。
  public int getIPCount()
  {
    return(IPCount);
  }
  public void setNonceCount(String nc)
  {
    NonceCount = nc;
  }
  public String getNonceCount()
  {
    return NonceCount;
  }
  public void setInitNonceCount()
  {
    setNonceCount("00000001");
  }
  public MM7Config getConfig()
  {
    return Mm7Config;
  }
  private void setServerMaxSize(int size)
  {
    ServerMaxSize = size;
  }
  public int getServerMaxSize()
  {
    return ServerMaxSize;
  }
  private void setKeepAlive(String conn)
  {
    KeepAlive = conn;
  }
  public String getKeepAlive()
  {
    return KeepAlive;
  }

  // 从配置文件中获得一些基本信息
  private void init()
  {
    hashmap.clear();
    MM7ConfigManager confManager = new MM7ConfigManager();
    String name = Mm7Config.getConnConfigName();
    if(!name.equals(""))
    {
      confManager.load(name);
      hashmap = confManager.hashmap;
      if(!hashmap.isEmpty())
      {
        this.setKeepAlive((String)hashmap.get("KeepAlive"));
        this.setServerMaxSize(Integer.parseInt((String)hashmap.get("ServerMaxKeepAlive")));
      }
    }
    if(this.getKeepAlive().equals("on"))
    {
      /**若支持长连接，则建最小长连接数，若不支持，则建一条短连接*/
      addURL(Integer.parseInt( (String) hashmap.get("MinKeepAliveRequests")));
    }
  }
  //获得空闲的连接
  public synchronized ConnectionWrap getConnWrap()
  {
    if(ClientList == null)
    {
      addURL(1);
      ConnectionWrap connWrap;
      if(ClientList.isEmpty())
      {
        return null;
      }
      else
      {
          connWrap = (ConnectionWrap) ClientList.get(0);
          connWrap.setFree(false);
          connWrap.setConnectIndex(0);
          return connWrap;
      }
    }
    else
    {
        //寻找空闲的连接
        for (int i = 0; i < ClientList.size(); i++) {
          ConnectionWrap conn = (ConnectionWrap) ClientList.get(i);
          if (conn != null && conn.getFree()) {
            conn.setFree(false);
            conn.setConnectIndex(i);
            conn.start = System.currentTimeMillis();
            return conn;
          }
          else if(conn == null){
            continue;
          }
        }
        //没有空闲连接的话，若size小于最大连接数，则建step步长的连接。
        int MaxCount = Integer.parseInt( (String) hashmap.get(
            "MaxKeepAliveRequests"));
        if (ClientList.size() < MaxCount) {
          int step = Integer.parseInt((String) hashmap.get("step"));
          /**判断目前已有连接加入要新建的连接是否超过最大连接数，若不超，则建setp个连接，否则
           * 新建（最大连接数－现有连接数）个连接。
           * */
          if (ClientList.size() + step <= MaxCount)
            addURL(step);
          else
            addURL(MaxCount - ClientList.size());
          if(ClientList.isEmpty()==true)
          {
            //addURL(step);
            return null;
          }
          else
          {
            ConnectionWrap conn = (ConnectionWrap) ClientList.get(ClientList.size() -
                step);
            conn.setFree(false);
            conn.setConnectIndex(ClientList.size() - step);
            return conn;
          }
        }
        //建一条短连接
        else {
          try {
            ConnectionWrap conn = new ConnectionWrap(Mm7Config);
            if(conn.BuidLink())
              return conn;
            else
              return null;
          }
          catch (Exception e) {
            System.err.println(e);
            return null;
          }
        }
    }
  }

  //增加count个新的URL连接
  public void addURL(int count)
  {
    //System.out.println("addURL"+count);
    if(ClientList == null)
      ClientList = new ArrayList(count);
    try{
      for(int i=0;i<count;i++)
      {
        ConnectionWrap conn = new ConnectionWrap(Mm7Config);
        if(conn.BuidLink())
        {
          ClientList.add(conn);
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  //删除一个连接
  public boolean deleteURL(ConnectionWrap connwrap)
  {
    if(connwrap != null)
    {
      int index = connwrap.getConnectIndex();
      //System.out.println("connwrap != null!index="+index);
      if (ClientList.isEmpty() == true)
        return false;
      else {
        //System.out.println("size="+ClientList.size());
        for (int i = 0; i < ClientList.size(); i++) {
          if (index == i) {
            ClientList.remove(index);
            //重新排序
            Collections.reverse(ClientList);
            return true;
          }
        }
        return false;
      }
    }else
      return false;
  }

  public void run () {
    long interval = Long.parseLong((String)hashmap.get("KeepAliveTimeout"));
    //while (true) {
      try {
          Thread.sleep(interval);
      } catch (Exception e) {
          e.printStackTrace();
      }
      scan ();
  }
  //管理连接。一些超时的连接刷新
  private void scan () {
    int timeout = Integer.parseInt((String)hashmap.get("KeepAliveTimeout")); //取得最长连接时间
    if(ClientList != null)
    {
      if(!ClientList.isEmpty())
      {
        for (int i = 0; i < ClientList.size(); i++) {
          ConnectionWrap conn = (ConnectionWrap) ClientList.get(i);
          if (conn.start > 0) {
            time = System.currentTimeMillis() - conn.start;
            if (time >= timeout)
              conn.setFree(true);
          }
        }
      }
    }
  }
}
