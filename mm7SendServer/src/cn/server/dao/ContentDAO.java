package cn.server.dao;

import java.util.ArrayList;
import java.util.List;

import cn.common.JDBCHandler;
import cn.common.MyException;
import cn.server.bean.Content;

public class ContentDAO {
	
	public void insertContent(final Content content)throws Exception{
		new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				String sql = "INSERT INTO mm7_content (contentType,contentByte,sendTaskId,sort) VALUES (?,?,?,?);";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, content.getContentType());
					stat.setObject(2, content.getContentByte());
					stat.setObject(3, content.getSendTaskId());
					stat.setObject(4, content.getSort());
					
					return stat.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库插入信息内容出现异常！",e);
				}
			}
		}.execute();
	}
	
	@SuppressWarnings("unchecked")
	public List<Content> getContentListBySendTaskId(final int sendTaskId)throws Exception{
		return (List<Content>) new JDBCHandler() {
			
			@Override
			public Object doExecute() throws Exception {
				List<Content> list = new ArrayList<Content>();
				String sql ="SELECT * FROM mm7_content WHERE sendTaskId = ?;";
				try {
					stat = conn.prepareStatement(sql);
					stat.setObject(1, sendTaskId);
					rs = stat.executeQuery();
					while(rs.next()){
						Content content = new Content();
						content.setContentId(rs.getInt("contentId"));
						content.setContentType(rs.getInt("contentType"));
						content.setContentByte(rs.getString("contentByte"));
						content.setSendTaskId(rs.getInt("sendTaskId"));
						content.setSort(rs.getInt("sort"));
						list.add(content);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MyException("数据库根据任务ID取得任务内容异常！！！",e);
				}
				return list;
			}
		}.execute();
	}
	
	

}
