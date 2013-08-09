package cn.server.dao;

import java.util.ArrayList;
import java.util.List;

import cn.common.JDBCHandler;
import cn.common.MyException;
import cn.server.bean.MyLimit;

public class DBNumber {

	@SuppressWarnings("unchecked")
	public List<String> getToNumberList(final MyLimit limit)throws Exception{
		return (List<String>)new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				List<String> list = new ArrayList<String>();
				String sql = "SELECT number FROM mm7_number"+limit.getSQL();
				try {
					stat = conn.prepareStatement(sql);
					rs = stat.executeQuery();
					while(rs.next()){
						list.add(rs.getString("number"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("取得数据库接收号码出现异常！", e); 
				}
				return list;
			}
		}.execute();
	}
	
	public int getAllCount()throws Exception{
		return (int) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				int count = 0;
				String sql = "SELECT COUNT(*) FROM mm7_number";
				try {
					stat = conn.prepareStatement(sql);
					rs = stat.executeQuery();
					while(rs.next()){
						count = rs.getInt(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库取得发送号码总数异常！", e);
				}
				return count;
			}
		}.execute();
	}
}
