package cn.common;


/**
 * @author guopeng
 * 数据库语句执行的抽象类
 */
public abstract class JDBCHandlersqlserver {

	protected java.sql.Connection conn = null;
	protected java.sql.PreparedStatement stat = null;
	protected java.sql.ResultSet rs = null;
	
	public Object execute() throws Exception {
		try {
			DataSourceMgr.getInstance();
			conn = DataSourceMgr.getDataSource().getConnection();
			return doExecute();
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			try {
				if (rs != null)
					rs.close();
				if (stat != null)
					stat.close();
			} catch (java.sql.SQLException e2) {
				throw new java.lang.RuntimeException(e2.getMessage());
			}
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (java.sql.SQLException e2) {
				throw new java.lang.RuntimeException(e2.getMessage());
			}
		}
		return null;
	}
	
	public abstract Object doExecute() throws Exception;
	
}
