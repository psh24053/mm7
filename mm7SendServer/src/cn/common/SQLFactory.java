package cn.common;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SQLFactory {

	private static ComboPooledDataSource ds = null;
	
	private static String url = "jdbc:mysql://localhost:3306/mm7?useUnicode=true&amp;characterEncoding=UTF-8";
	private static String user = "root";
	private static String pass = "root";
	private static int min_size = 5;
	private static int idle_test_period = 120;
	private static int acquire_increment = 1;
	@SuppressWarnings("unused")
	private static boolean validate = false;
	private static String driver = "com.mysql.jdbc.Driver";
	private static int max_size = 30;
	@SuppressWarnings("unused")
	private static int init_size = 3;
	private static int timeout = 18000;
	@SuppressWarnings("unused")
	private static int max_statements = 50;
	@SuppressWarnings("unused")
	private static int maxIdleTime = 60;
	
	static{
		//getDbProperties();
		ds = new ComboPooledDataSource();
		try {
			ds.setDriverClass(driver);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		System.out.println("设置连接超时时间：" + timeout);
		ds.setJdbcUrl(url);
		ds.setUser(user);
		ds.setPassword(pass);
		ds.setMaxPoolSize(max_size);
//		ds.setInitialPoolSize(init_size);
//		ds.setMaxIdleTime(maxIdleTime);
		ds.setMinPoolSize(min_size);
		ds.setAcquireIncrement(acquire_increment);
//		ds.setMaxStatements(max_statements);
//		ds.setCheckoutTimeout(timeout);
		ds.setIdleConnectionTestPeriod(idle_test_period);
		ds.setTestConnectionOnCheckin(true);
//		ds.setTestConnectionOnCheckout(true);
	}
	
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void getDbProperties(){
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
		Properties prop = new Properties();
		try {
			prop.load(input);
			url = prop.getProperty("url");
			user = prop.getProperty("user");
			pass = prop.getProperty("pass");
			min_size = Integer.parseInt(prop.getProperty("min_size"));
			idle_test_period = Integer.parseInt(prop.getProperty("idle_test_period"));
			acquire_increment = Integer.parseInt(prop.getProperty("acquire_increment"));
			validate = new Boolean(prop.getProperty("validate"));
			driver = prop.getProperty("driver");
			max_size = Integer.parseInt(prop.getProperty("max_size"));
			init_size = Integer.parseInt(prop.getProperty("init_size"));
			timeout = Integer.parseInt(prop.getProperty("timeout"));
			max_statements = Integer.parseInt(prop.getProperty("max_statements"));
			maxIdleTime = Integer.parseInt(prop.getProperty("maxIdleTime"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeDS() {
		if(ds != null) {
			ds.close();
		}
	}
	
}
