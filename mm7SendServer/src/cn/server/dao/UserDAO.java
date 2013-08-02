package cn.server.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.common.JDBCHandler;
import cn.common.MyException;
import cn.server.bean.User;

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
					
					e.printStackTrace();
					throw new MyException("数据库添加用户测试异常！！", e, this.getClass().getName(), "jaja");
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
					
					e.printStackTrace();
					throw new MyException("数据库检测用户是否存在出现异常！！！", this.getClass().getName(), "checkUserExist");
				}
				return false;
			}
		}.execute();
	}
	
	public User getUserInfoByUserName(final String userName)throws Exception{
		return (User) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				User user = null;
				String sql = "SELECT * FROM mm7_user WHERE username = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, userName);
					rs = stat.executeQuery();
					if(rs.next()){
						user.setId(rs.getInt("userId"));
						user.setUsername(rs.getString("username"));
						user.setPassword(rs.getString("password"));
						user.setLastLoginTime(rs.getString("lastLoginTime"));
						user.setGrade(rs.getInt("grade"));
					}
				} catch (Exception e) {
					
					e.printStackTrace();
					throw new MyException("数据库登陆根据用户名取得用户信息异常！",e);
				}
				return user;
			}
		}.execute();
	}
	
	public void removeUser(final int id)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "DELETE FROM mm7_user WHERE userId = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, id);
				} catch (Exception e) {
					
					e.printStackTrace();
					throw new MyException("数据库删除用户出现异常！", e);
				}
				return stat.execute();
			}
		}.execute();
	}
	
	public void editPassWord(final int id,final String newPass)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "UPDATE mm7_user SET PASSWORD = ? WHERE userid = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, newPass);
					stat.setObject(2, id);
					return stat.execute();
				} catch (Exception e) {
					
					e.printStackTrace();
					throw new MyException("数据库修改用户密码异常！", e);
				}
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
					throw new MyException("数据库取得用户列表异常",e);
				}
				return list;
			}
		}.execute();
	}
	
	public void updateLoginTime(final int id )throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "UPDATE mm7_user SET lastLoginTime = NOW() WHERE userid = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, id);
					return stat.execute();
				} catch (Exception e) {
					e.printStackTrace();
					throw new MyException("数据库更新登陆时间出现异常！",e);
				}
			}
		}.execute();
	}

}
