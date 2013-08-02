package cn.server.bean;

public class MyLimit {
	private int pageNum;
	private int countLimit;
	
	public MyLimit(int pageNum,int countLimit){
		this.pageNum = pageNum;
		this.countLimit = countLimit;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getCountLimit() {
		return countLimit;
	}
	public void setCountLimit(int countLimit) {
		this.countLimit = countLimit;
	}
	
	public String getSQL(){
		return " LIMIT "+((pageNum-1)*countLimit)+" , "+countLimit+" ";
	}
	
}
