package cn.server.bean;

public class SendTask {
	
	private int sendTaskId;
	private String name;
	private String subject;
	private String createTime;
	private int toCount;
	private String customTo;
	private int state;
	private int successCount;
	private int failCount;
	private String completeTime;
	public int getSendTaskId() {
		return sendTaskId;
	}
	public void setSendTaskId(int sendTaskId) {
		this.sendTaskId = sendTaskId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getToCount() {
		return toCount;
	}
	public void setToCount(int toCount) {
		this.toCount = toCount;
	}
	public String getCustomTo() {
		return customTo;
	}
	public void setCustomTo(String customTo) {
		this.customTo = customTo;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	

}
