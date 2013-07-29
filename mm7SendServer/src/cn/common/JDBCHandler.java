package cn.common;


/**
* @ClassName: JDBCHandler
* @Description: TODO(JDBC辅助工具)
* @author guopeng
* @date 2012-2-24 上午11:15:39
* 
*/
public abstract class JDBCHandler {

	protected java.sql.Connection conn = null;
	protected java.sql.PreparedStatement stat = null;
	protected java.sql.ResultSet rs = null;
	
	/**
	* @Title: execute
	* @Description: TODO(用于执行JDBC操作)
	* @param @return
	* @param @throws Exception?设定文件
	* @return Object?返回类型
	* @throws
	*/
	public Object execute() throws Exception {
		try {
			conn = SQLFactory.getConnection();
			return doExecute();
		} catch (Exception e) {
			try {
				if (rs != null)
					rs.close();
				if (stat != null)
					stat.close();
			} catch (java.sql.SQLException e2) {
				throw new java.lang.RuntimeException(e2.getMessage());
			}
			throw e;
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (java.sql.SQLException e2) {
				throw new java.lang.RuntimeException(e2.getMessage());
			}
		}
	}
	
	/**
	 * @throws Exception TODO
	 * @throws Exception 
	* @Title: doExecute
	* @Description: TODO(JDBC逻辑)
	* @param @return
	* @param @throws Exception?设定文件
	* @return Object?返回类型
	* @throws
	*/
	public abstract Object doExecute() throws Exception;
	
}
