package cn.common;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author guopeng
 * 数据源管理器
 */
public class DataSourceMgr {

	private static DataSourceMgr instance = null;
	private static DataSource dataSource = null;
	
	private DataSourceMgr() {
		Properties prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
			
			String driver = prop.getProperty("driverName");
			String url = prop.getProperty("url");
			String userName = prop.getProperty("username");
			String password = prop.getProperty("password");
			
			BasicDataSource bdds = new BasicDataSource();
			
			bdds.setDriverClassName(driver);
			bdds.setUrl(url);
			bdds.setUsername(userName);
			bdds.setPassword(password);
			bdds.setMaxWait(2000);
			
			dataSource = bdds;
		} catch (IOException e) {
		}
		
	}
	
	public static synchronized DataSourceMgr getInstance() {
		if ( null == instance ) {
			instance = new DataSourceMgr();
		}
		return instance;
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	
}
