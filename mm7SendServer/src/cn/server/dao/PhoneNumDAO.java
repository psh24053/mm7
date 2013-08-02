package cn.server.dao;

import java.util.ArrayList;
import java.util.List;

import cn.common.JDBCHandler;
import cn.common.MyException;
import cn.server.bean.Failnumber;
import cn.server.bean.MyLimit;

public class PhoneNumDAO {
	@SuppressWarnings("unchecked")
	public List<Failnumber> getFailPhoneNumList(final int sendTaskId,final MyLimit limit)throws Exception{
		
		return (List<Failnumber>) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				List<Failnumber> list = new ArrayList<Failnumber>();
				String sql = "SELECT * FROM mm7_failnumber WHERE sendTaskId = ? "+limit.getSQL();
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, sendTaskId);
					rs = stat.executeQuery();
					while(rs.next()){
						Failnumber number = new Failnumber();
						number.setFailNumberId(rs.getInt("failNumberId"));
						number.setNumber(rs.getString("number"));
						number.setSendTaskId(rs.getInt("sendTaskId"));
						list.add(number);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库根据任务ID取得失败号码列表异常", e);
				}
				return list;
			}
		}.execute();
	}
	
	public void insertFailPhoneNum(final int sendTaskId,final List<String> numberList)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "INSERT INTO mm7_failnumber (number,sendTaskId) VALUES ";
				for (int i = 0; i < numberList.size(); i++) {
					if(i ==numberList.size()-1){
						sql = sql+"("+numberList.get(i)+","+sendTaskId+");";
					}else{
						sql = sql+"("+numberList.get(i)+","+sendTaskId+"),";
					}
				}
				try {
					stat = conn.prepareStatement(sql);
					stat.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库批量插入失败号码异常", e);
				}
				return null;
			}
		}.execute();
	}
	public int getAllCount()throws Exception{
		return (int) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				int count = 0;
				String sql = "SELECT COUNT(*) FROM mm7_failnumber;";
				try {
					stat = conn.prepareStatement(sql);
					rs = stat.executeQuery();
					while(rs.next()){
						count = rs.getInt(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库取得失败号码总数异常", e);
				}
				return count;
			}
		}.execute();
	}
}
