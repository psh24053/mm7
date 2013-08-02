package cn.server.bean;

public class Content {
	private int contentId;
	private int contentType;
	private String contentByte;
	private int sendTaskId;
	private int sort;
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public String getContentByte() {
		return contentByte;
	}
	public void setContentByte(String contentByte) {
		this.contentByte = contentByte;
	}
	public int getSendTaskId() {
		return sendTaskId;
	}
	public void setSendTaskId(int sendTaskId) {
		this.sendTaskId = sendTaskId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	

}
