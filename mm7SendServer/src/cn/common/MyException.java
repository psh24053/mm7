package cn.common;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * @author guopeng
 * 自定义异常类
 */
public class MyException extends Exception {

	private static final long serialVersionUID = 720L;
	
	private boolean IS_DEBUG = true;
	private String className = null;
	private String methord = null;
	
	
	@Override
	public void printStackTrace() {
		if (IS_DEBUG) {
			super.printStackTrace();
		}
	}
	
	public MyException(String message, Throwable e, String className, String methord) {
		super(message, e);
		this.className = className;
		this.methord = methord;
	}
	
	public MyException(String message, String className, String methord) {
		super(message);
		this.className = className;
		this.methord = methord;
	}
	public MyException(String message) {
		super(message);
	}
	public MyException(String message,Throwable e) {
		super(message,e);
	}
	
	public JSONObject getMsgObj() {
		JSONObject msgObj = new JSONObject();
		try {
			msgObj.put("Message", this.getMessage());
			if (className != null)
				msgObj.put("ClassName", className);
			if (methord != null)
				msgObj.put("Methord", methord);
		} catch (JSONException e) {
		}
		return msgObj;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
