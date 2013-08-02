package cn.server.dao;

import java.util.ArrayList;
import java.util.List;

import cn.common.JDBCHandler;
import cn.common.MyException;
import cn.server.bean.MyLimit;
import cn.server.bean.SendTask;

public class SendTaskDAO {
	
	public int insertSendTask(final SendTask sendTask)throws Exception{
		return (int) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				int id = 0;
				String sql1 = "INSERT INTO mm7_sendtask(mm7_sendtask.name,createTime,toCount,customTo,state,successCount,failCount)VALUES(?,NOW(),?,?,?,?,?);";
				String sql2 = "SELECT LAST_INSERT_ID();";
				try {
					stat = conn.prepareStatement(sql1);
					stat.setObject(1, sendTask.getName());
					stat.setObject(2, sendTask.getToCount());
					stat.setObject(3, sendTask.getCustomTo());
					stat.setObject(4, sendTask.getState());
					stat.setObject(5, 0);
					stat.setObject(6, 0);
					stat.execute();
					stat = conn.prepareStatement(sql2);
					rs = stat.executeQuery();
					while(rs.next()){
						id = rs.getInt(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库插入任务并返回ID异常",e);
				}
				return id;
			}
		}.execute();
	}
	
	public void updateState(final int id,final int state)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "UPDATE mm7_sendtask SET state=? WHERE sendTaskId = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, state);
					stat.setObject(2,id);
					stat.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库修改任务状态出现异常", e);
				}
				return null;
			}
		}.execute();
	}
	public void updateTask(final int id,final int successCount,final int failCount,final int state)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "UPDATE mm7_sendtask SET state = ?, successCount=? ,failCount=?,completeTime=NOW() WHERE sendTaskId = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, state);
					stat.setObject(2, successCount);
					stat.setObject(3, failCount);
					stat.setObject(4, id);
					stat.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库任务完成后更新异常", e);
				}
				return null;
			}
		}.execute();
	}
	
	@SuppressWarnings("unchecked")
	public List<SendTask> getSendTaskList(final MyLimit limit)throws Exception{
		return (List<SendTask>) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				List<SendTask> list = new ArrayList<SendTask>();
				String sql = "SELECT * FROM mm7_sendtask "+limit.getSQL();
				try {
					stat = conn.prepareStatement(sql);
					rs = stat.executeQuery();
					while(rs.next()){
						SendTask task = new SendTask();
						task.setCompleteTime(rs.getDate("completeTime").toString());
						if(rs.getDate("completeTime") == null){
							task.setCompleteTime("未结束");
						}
						task.setCreateTime(rs.getDate("createTime").toString());
						task.setCustomTo(rs.getString("customTo"));
						task.setFailCount(rs.getInt("failCount"));
						task.setName(rs.getString("name"));
						task.setSendTaskId(rs.getInt("sendTaskId"));
						task.setState(rs.getInt("state"));
						task.setSuccessCount(rs.getInt("successCount"));
						task.setToCount(rs.getInt("toCount"));
						
						list.add(task);
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库取得任务列表异常", e);
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
				String sql = "SELECT COUNT(*) FROM mm7_sendtask;";
				try {
					stat = conn.prepareStatement(sql);
					rs = stat.executeQuery();
					while(rs.next()){
						count = rs.getInt(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库取得任务总数异常",e);
				}
				return count;
			}
		}.execute();
	}

}
