package cn.server.dao;

import cn.common.JDBCHandler;
import cn.common.MyException;

public class UserDAO {

	public void adduser(final String userName,final String passWord,final int grade)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql ="INSERT INTO mm7_user(username,password,lastLoginTime,grade) VALUES (?,?,?,?)";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, userName);
					stat.setObject(2, passWord);
					stat.setObject(3, null);
					stat.setObject(4, grade);
					stat.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("添加用户测试异常！！", e, this.getClass().getName(), "jaja");
				}
				return null;
			}
		}.execute();
		
	}
	
	public boolean checkUserExist(final String userName)throws Exception{
		return (boolean) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				
				String sql = "SELECT * FROM mm7_user WHERE username = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1,userName);
					rs = stat.executeQuery();
					while(rs.next()){
						return true;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("检测用户是否存在出现异常！！！", this.getClass().getName(), "checkUserExist");
				}
				return false;
			}
		}.execute();
	}

}
