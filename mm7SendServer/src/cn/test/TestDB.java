package cn.test;



import cn.common.JDBCHandler;
import cn.common.MyException;


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

}
