package cn.test;



import java.util.ArrayList;
import java.util.List;

import cn.common.JDBCHandler;
import cn.common.MyException;
import cn.server.bean.User;


public class TestDB {


	public void adduser()throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql ="INSERT INTO mm7_user(username,password,lastLoginTime,grade) VALUES (?,?,?,?)";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, "testuser");
					stat.setObject(2, "testpass");
					stat.setObject(3, null);
					stat.setObject(4, 1);
					stat.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("添加用户测试异常！！", e, "hah", "jaja");
				}
				return null;
			}
		}.execute();
		
	}
	@SuppressWarnings("unchecked")
	public List<User> getUserList()throws Exception{
		return (List<User>) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				List<User> list = new ArrayList<User>();
				String sql = "SELECT * FROM mm7_user";
				try {
					stat = conn.prepareStatement(sql);
					rs = stat.executeQuery();
					while(rs.next()){
						User user = new User();
						user.setUsername(rs.getString("username"));
						user.setId(rs.getInt("userId"));
						user.setGrade(rs.getInt("grade"));
						user.setLastLoginTime(rs.getString("lastLoginTime"));
						list.add(user);
					}
				} catch (Exception e) {
					
					e.printStackTrace();
					throw new MyException("取得用户列表异常",e);
				}
				return list;
			}
		}.execute();
	}
}
